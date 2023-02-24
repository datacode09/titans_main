package titans.hrrr.downloads;

import java.io.File;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import titans.hrrr.BaseSpringITest;
import titans.hrrr.core.HrrrImporter;
import titans.hrrr.core.grib.HrrrGribSource;
import titans.nam.NoaaParameter;
import titans.noaa.grib.GribFile;

/**
 *
 * @author Ricardo Marquez
 */
public class DownloadGribsIT extends BaseSpringITest {

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
    int minusDays = 0;
    int projectId = 0;
    HrrrImporter importer = new HrrrImporter(gribRootFolder, netCdfRootFolder, projectId, degribExe);
    String parentKey = "HRRR";
    List<NoaaParameter> params = source.getParameters(parentKey, minusDays);
    params.stream().forEach(p -> {
      System.out.println("p = " + p);
      long tic = System.currentTimeMillis();
      GribFile gribfile = importer.getGribFile(p.datetime, p.fcststep);
      if (gribfile.notExists()) {
        source.download(gribfile);
      }
      System.out.println("elapsed time = " + (System.currentTimeMillis() - tic));
    });
  }
}
