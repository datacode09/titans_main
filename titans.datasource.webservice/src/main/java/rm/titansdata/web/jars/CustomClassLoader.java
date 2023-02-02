package rm.titansdata.web.jars;

import org.locationtech.jts.awt.PointShapeFactory.X;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;
import java.util.stream.Stream;
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
import rm.titansdata.plugin.ParameterFactory;
import rm.titansdata.plugin.RasterFactory;
import rm.titansdata.web.rasters.AbstractParameterFactory;
import rm.titansdata.web.rasters.RasterModelsRegistry;

@Component
public class CustomClassLoader {

  @Autowired
  private ConfigurableApplicationContext applicationContext;

  @Autowired
  private RasterModelsRegistry rasterModelsRegistry;

  @Autowired
  private AbstractParameterFactory parametersFactory;

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
   * @param classe
   * @param beanFactory 
   */
  private void loadByClass(String classe, ConfigurableListableBeanFactory beanFactory) {
    try {
      Class<?> c = Class.forName(classe);
      if (c.getAnnotation(Configuration.class) != null) {
        this.loadConfiguration(c);
      } else if (RasterFactory.class.isAssignableFrom(c)) {
        RasterFactory bean = (RasterFactory) beanFactory
          .createBean(c, AutowireCapableBeanFactory.AUTOWIRE_NO, true);
        String key = bean.key();
        this.rasterModelsRegistry.put(key, bean);
      } else if (ParameterFactory.class.isAssignableFrom(c)) {
        ParameterFactory bean = (ParameterFactory) beanFactory
          .createBean(c, AutowireCapableBeanFactory.AUTOWIRE_NO, true);
        String key = bean.key();
        this.parametersFactory.add(key, bean);
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
    context.getBeanFactory().getBean("nam.parameters");

    
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
    reader.loadBeanDefinitions(inputSource);
    String[] names = createdContext.getBeanDefinitionNames();
    ConfigurableApplicationContext genericAppContext = this.applicationContext;
    Stream.of(names).filter(n -> !n.contains("org.spring"))
      .forEach(beanname -> {
        BeanDefinition definition = createdContext.getBeanDefinition(beanname);
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) genericAppContext.getBeanFactory();
        factory.registerBeanDefinition(beanname, definition);  
      });
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
}
