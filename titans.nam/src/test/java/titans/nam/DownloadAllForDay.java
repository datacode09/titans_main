package titans.nam;

import java.io.File;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import titans.nam.core.NamImporter;
import titans.nam.core.NamVariable;
import titans.nam.grib.NamGribSource;

/**
 *
 * @author Ricardo Marquez
 */
public class DownloadAllForDay extends BaseSpringTest {

  @Autowired
  @Qualifier("nam.gribRootFolder")
  File gribRootFolder;
  @Autowired
  @Qualifier("nam.netCdfRootFolder")
  File netCdfRootFolder;
  @Autowired
  @Qualifier("nam.degribExe")
  File degribExe;

  @Test
  public void test() {
    NamGribSource source = new NamGribSource();
    String parentKey = "";
    List<NamParameter> params = source.getCurrentNamParameters(2L, parentKey); 
    NamVariable namVariable = new NamVariable("TMP_2-HTGL");
    params.stream().forEach(p -> {
      System.out.println("p = " + p);
      long tic = System.currentTimeMillis();
      NamImporter importer = new NamImporter(gribRootFolder, netCdfRootFolder, degribExe);  
      importer.getRaster(namVariable, p.datetime, p.fcststep);
      System.out.println("elapsed time = " + (System.currentTimeMillis() - tic));
    });

  }
}
