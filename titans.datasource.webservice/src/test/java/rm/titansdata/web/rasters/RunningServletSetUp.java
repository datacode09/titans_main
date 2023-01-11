package rm.titansdata.web.rasters;

import common.http.RmHttpReader;
import java.net.URL;
import org.junit.Test;

/**
 *
 * @author Ricardo Marquez
 */
public class RunningServletSetUp {
  
  @Test
  public void jarfilesetup() throws Exception {
    URL url = new URL("http://localhost:8081/titansdata.web/registerJar");
    new RmHttpReader.Builder(url)
      .setRequestParam("jarfile", "C:\\Dev\\applications.personal\\titans\\test_source\\target\\test_source-1.0-SNAPSHOT.jar")
      .setRequestParam("class", "rm.test_source.TestSourceRasterFactory")
      .post()
      ; 
    
    
  }
}
