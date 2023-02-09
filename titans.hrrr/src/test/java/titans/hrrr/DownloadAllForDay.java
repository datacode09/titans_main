package titans.hrrr;

import java.io.File;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import titans.hrrr.core.HrrrImporter;
import titans.hrrr.core.grib.HrrrGribSource;
import titans.nam.NoaaParameter;
import titans.nam.core.NoaaVariable;

/**
 *
 * @author Ricardo Marquez
 */
public class DownloadAllForDay extends BaseSpringTest {

  @Autowired
  @Qualifier("hrrr.gribRootFolder")
  File gribRootFolder;
  @Autowired
  @Qualifier("hrrr.netCdfRootFolder")
  File netCdfRootFolder;
  @Autowired
  @Qualifier("hrrr.degribExe")
  File degribExe;

  @Test
  public void test() {
    HrrrGribSource source = new HrrrGribSource();
    String parentKey = "HRRR";
    int minusDays = 0; 
    List<NoaaParameter> params = source.getCurrentParameters(parentKey, minusDays); 
    NoaaVariable namVariable = new NoaaVariable("TMP_2-HTGL");
    params.stream().forEach(p -> {
      System.out.println("p = " + p);
      long tic = System.currentTimeMillis();
      HrrrImporter importer = new HrrrImporter(gribRootFolder, netCdfRootFolder, degribExe);  
      importer.getRaster(namVariable, p.datetime, p.fcststep);
      System.out.println("elapsed time = " + (System.currentTimeMillis() - tic));
    });
  }
}
