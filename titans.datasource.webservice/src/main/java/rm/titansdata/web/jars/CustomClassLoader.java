package rm.titansdata.web.jars;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
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

  public synchronized void loadLibrary(File jar, String... classes) {
    try {
      URL url = jar.toURI().toURL();
      Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
      method.setAccessible(true);
      method.invoke(Thread.currentThread().getContextClassLoader(), new Object[]{url});

      for (String classe : classes) {
        Class<?> c = Class.forName(classe);
        if (RasterFactory.class.isAssignableFrom(c)) {
          ConfigurableListableBeanFactory beanFactory = this.applicationContext.getBeanFactory();
          RasterFactory bean = (RasterFactory) beanFactory
            .createBean(c, AutowireCapableBeanFactory.AUTOWIRE_NO, true);
          String key = bean.key();
          this.rasterModelsRegistry.put(key, bean);
        } else if (ParameterFactory.class.isAssignableFrom(c)) {
          ConfigurableListableBeanFactory beanFactory = this.applicationContext.getBeanFactory();
          ParameterFactory bean = (ParameterFactory) beanFactory
            .createBean(c, AutowireCapableBeanFactory.AUTOWIRE_NO, true);
          String key = bean.key();
          this.parametersFactory.add(key, bean);
        }
      }
    } catch (Exception ex) {
      throw new RuntimeException(//
        String.format("Cannot load library from jar file '%s'. Reason: %s",
          jar.getAbsolutePath(), ex.getMessage()));
    }
  }
}
