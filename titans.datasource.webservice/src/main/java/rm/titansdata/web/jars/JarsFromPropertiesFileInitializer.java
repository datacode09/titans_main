package rm.titansdata.web.jars;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;
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
      
      String[] classes = Arrays.stream(jarAndClass[0].split("/", -1))
        .map(s->s.replaceAll("\"", ""))
        .collect(Collectors.toList()).toArray(new String[0]);
      this.jarClassLoader.loadLibrary(jar, classes);
    }
  }

}
