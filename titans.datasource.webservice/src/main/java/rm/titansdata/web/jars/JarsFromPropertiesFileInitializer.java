package rm.titansdata.web.jars;

import java.io.File;
import java.util.Properties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class JarsFromPropertiesFileInitializer implements InitializingBean {

  @Autowired
  @Qualifier("appProps")
  Properties appProps;
  @Autowired
  private CustomClassLoader jarClassLoader;

  @Override
  public void afterPropertiesSet() throws Exception {
    System.out.println("appProps = " + appProps);
    String text = this.appProps.getProperty("raster.jars");
    String[] parts = text.split(";", -1); 
    for (String part : parts) {   
      String[] jarAndClass = part.replace("(", "").replace(")", "").split(","); 
      File jar = new File(jarAndClass[1].replaceAll("\"", "")); 
      String classe = jarAndClass[0].replaceAll("\"", ""); 
      this.jarClassLoader.loadLibrary(jar, classe);
    }
  }

}
