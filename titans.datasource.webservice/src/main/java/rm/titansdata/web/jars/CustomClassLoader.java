package rm.titansdata.web.jars;

import common.RmExceptions;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import org.locationtech.jts.awt.PointShapeFactory.X;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import rm.titansdata.plugin.ColorMapProvider;
import rm.titansdata.plugin.ParameterFactory;
import rm.titansdata.plugin.RasterFactory;
import rm.titansdata.web.rasters.AbstractParameterFactory;
import rm.titansdata.web.rasters.RasterModelsRegistry;
import rm.titansdata.web.rasters.RastersSourceService;
import rm.titansdata.web.rasters.colormap.ColorMapProviderFactory;

@Component
public class CustomClassLoader {

  private final Map<File, String> springXmls = new HashMap<>();

  @Autowired
  private ConfigurableApplicationContext applicationContext;

  @Autowired
  private RasterModelsRegistry rasterModelsRegistry;

  @Autowired
  private RastersSourceService service;

  @Autowired
  private AbstractParameterFactory parametersFactory;

  @Autowired
  private ColorMapProviderFactory cmProviderFactory;

  /**
   *
   * @param jar
   * @param classes
   */
  public synchronized void loadLibrary(File jar, String... classes) {
    try {
      URL url = jar.toURI().toURL();
      Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
      method.setAccessible(true);
      method.invoke(Thread.currentThread().getContextClassLoader(), new Object[]{url});
      DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) this.applicationContext.getBeanFactory();
      for (String classe : classes) {
        if (classe.endsWith(".xml")) {
          this.loadSpringXml(jar, classe);
        } else {
          this.loadByClass(classe, beanFactory);
        }
      }
    } catch (Exception ex) {
      throw new RuntimeException(//     
        String.format("Cannot load library from jar file '%s'. Reason: %s",
          jar.getAbsolutePath(), ex.getMessage()), ex);
    }
  }

  /**
   *
   * @throws BeansException
   */
  void postLoad() throws BeansException {
    this.springXmls.entrySet().forEach(e -> {
      File jar = e.getKey();
      String xml = e.getValue();
      this.loadBeans(jar, xml);
    });
    this.applicationContext.getBeansOfType(RasterFactory.class).values().forEach(bean -> {
      String key = bean.key();
      this.rasterModelsRegistry.put(key, bean);
    });
    this.applicationContext.getBeansOfType(ParameterFactory.class).values().forEach(bean -> {
      String key = bean.key();
      this.parametersFactory.add(key, bean);
    });
    this.applicationContext.getBeansOfType(ColorMapProvider.class).entrySet()
      .forEach(entry -> {
        String name = entry.getKey();
        ColorMapProvider cmProvider = entry.getValue();
        RasterFactory rasterBean = this.getRasterFactory(name);
        String key = rasterBean.key();
        long rasterId = this.service.getRasterIdByKey(key);
        this.cmProviderFactory.put(rasterId, cmProvider);
      });
  }

  /**
   *
   * @param beanname
   * @return
   * @throws BeansException
   */
  private RasterFactory getRasterFactory(String beanname) {
    ConfigurableListableBeanFactory factory = this.applicationContext.getBeanFactory();
    BeanDefinition def = factory.getBeanDefinition(beanname);
    String[] deps = def.getDependsOn() == null ? new String[0] : def.getDependsOn();
    if (deps == null) {
      RmExceptions.throwException("Bean '%s' does not specifiy RasterFactory dependency.", beanname);
    }
    RasterFactory rasterBean = null;
    for (String dep : deps) {
      Object blah = this.applicationContext.getBean(dep);
      if ((blah) instanceof RasterFactory) {
        rasterBean = (RasterFactory) blah;
      }
    }
    if (rasterBean == null) {
      RmExceptions.throwException("Bean '%s' does not specifiy RasterFactory dependency.", beanname);
    }
    return rasterBean;
  }

  /**
   *
   * @param classe
   * @param beanFactory
   */
  private void loadByClass(String classe, ConfigurableListableBeanFactory beanFactory) {
    try {
      Class<?> c = Class.forName(classe);
      if (c.getAnnotation(Configuration.class) != null) {
        this.loadConfiguration(c);
      } else {
        beanFactory.createBean(c, AutowireCapableBeanFactory.AUTOWIRE_NO, true);
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   *
   * @param c
   * @throws Exception
   */
  private void loadConfiguration(Class<?> c) throws Exception {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(c);
    context.setParent(this.applicationContext);
    String className = c.getSimpleName();
    className = className.substring(0, 1).toLowerCase() + className.substring(1);
    DefaultListableBeanFactory factory = (DefaultListableBeanFactory) this.applicationContext.getBeanFactory();
    factory.registerBeanDefinition(className, context.getBeanDefinition(className));
    Method[] m = c.getDeclaredMethods();
    for (Method method : m) {
      Bean beanannotation = method.getDeclaredAnnotation(Bean.class);
      if (beanannotation != null) {
        String[] beannames = beanannotation.value();
        for (String beanname : beannames) {
          BeanDefinition beanDefinition = context.getBeanDefinition(beanname);
          factory.registerBeanDefinition(beanname, beanDefinition);
        }
      }
    }
  }

  /**
   *
   * @param jar
   * @param beansXml
   */
  private void loadSpringXml(File jar, String beansXml) {
    GenericApplicationContext createdContext
      = new GenericApplicationContext();
    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(createdContext);
    reader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_XSD);
    InputSource inputSource = this.getSpringXmlInputSource(jar, beansXml);
    int num = reader.loadBeanDefinitions(inputSource);
    String[] names = createdContext.getBeanDefinitionNames();
    ConfigurableApplicationContext genericAppContext = this.applicationContext;
    DefaultListableBeanFactory factory = (DefaultListableBeanFactory) genericAppContext.getBeanFactory();
    Stream.of(names).filter(n -> !n.contains("org.spring"))
      .forEach(beanname -> {
        BeanDefinition definition = createdContext.getBeanDefinition(beanname);
        factory.registerBeanDefinition(beanname, definition);

      });
    this.springXmls.put(jar, beansXml);
  }

  /**
   *
   * @param jar
   * @param beansXml
   * @return
   * @throws IOException
   * @throws X
   * @throws MalformedURLException
   */
  private InputSource getSpringXmlInputSource(File jar, String beansXml) {
    try {
      String someUniqueResourceInBJar = new JarFile(jar).stream()
        .map(e -> e.getName())
        .filter(n -> n.endsWith(".class"))
        .findFirst()
        .orElseThrow(() -> new RuntimeException());
      Class<?> Bclass = Class.forName(someUniqueResourceInBJar.replace("/", ".").replace(".class", ""));
      URL url = Bclass.getResource("/" + beansXml);
      InputStream stream = url.openStream();
      InputSource inputSource = new InputSource(new InputStreamReader(stream));
      return inputSource;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
  
  /**
   * 
   * @param jar
   * @param beansXml 
   */
  private void loadBeans(File jar, String beansXml) {
    GenericApplicationContext createdContext
      = new GenericApplicationContext();
    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(createdContext);
    reader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_XSD);
    InputSource inputSource = this.getSpringXmlInputSource(jar, beansXml);
    reader.loadBeanDefinitions(inputSource);
    String[] names = createdContext.getBeanDefinitionNames();
    ConfigurableApplicationContext genericAppContext = this.applicationContext;
    DefaultListableBeanFactory factory = (DefaultListableBeanFactory) genericAppContext.getBeanFactory();
    Stream.of(names).filter(n -> !n.contains("org.spring"))
      .forEach(beanname -> {
        Object bean = factory.getBean(beanname);
      });
  }
}
