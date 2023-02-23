package titans.nam.downloads;

import java.io.File;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import titans.nam.BaseSpringTest;
import titans.nam.NoaaParameter;
import titans.nam.core.NamImporter;
import titans.nam.core.NamInventoryReader;
import titans.nam.core.NoaaVariable;
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
    String parentKey = "NAM";
    long minusDays = 0L; 
    List<NoaaParameter> params = source.getCurrentNamParameters(minusDays, parentKey); 
    String varName = "TMP_2-HTGL";
    NoaaVariable namVariable = new NoaaVariable(varName, new NamInventoryReader().getUnit(varName));
    params.stream().forEach(p -> {
      System.out.println("p = " + p);
      long tic = System.currentTimeMillis();
      NamImporter importer = new NamImporter(gribRootFolder, netCdfRootFolder, 0, degribExe);  
      importer.getRaster(namVariable, p.datetime, p.fcststep);
      System.out.println("elapsed time = " + (System.currentTimeMillis() - tic));
    });

  }
}
