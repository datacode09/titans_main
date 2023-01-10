package rm.titansdata.web.jars;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import rm.titansdata.RasterFactory;
import rm.titansdata.web.rasters.RasterModelsRegistry;

@Component
public class CustomClassLoader {
  
  @Autowired
  private ConfigurableApplicationContext applicationContext;
  
  @Autowired
  private RasterModelsRegistry rasterModelsRegistry;
  
  
  
  public synchronized void loadLibrary(File jar, String[] classes) {
    try {
      URL url = jar.toURI().toURL();
      Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
      method.setAccessible(true);
      method.invoke(Thread.currentThread().getContextClassLoader(), new Object[]{url});
      this.applicationContext.getBeansOfType(RasterFactory.class);
      for (String classe : classes) {
        Class<?> c = Class.forName(classe);
        if (RasterFactory.class.isAssignableFrom(c)) {
          RasterFactory bean = (RasterFactory) this.applicationContext.getBeanFactory()
            .createBean(c, AutowireCapableBeanFactory.AUTOWIRE_NO, true);
          this.rasterModelsRegistry.put(bean.key(), bean);   
        }
      }
    } catch (Exception ex) {
      throw new RuntimeException(//
        String.format("Cannot load library from jar file '%s'. Reason: %s",
          jar.getAbsolutePath(), ex.getMessage()));
    }
  }
}
