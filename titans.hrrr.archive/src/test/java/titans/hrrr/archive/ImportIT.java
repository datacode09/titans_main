package titans.hrrr.archive;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import junitparams.JUnitParamsRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import titans.hrrr.archive.core.HrrrArchiveGribSource;
import titans.noaa.core.NoaaImporter;
import titans.noaa.grib.GribFile;

/**
 *
 * @author Ricardo Marquez
 */
@RunWith(JUnitParamsRunner.class)
public class ImportIT extends BaseSpringITest{
  
  /**
   * 
   */
  @Autowired
  private HrrrArchiveRasterFactory factory;
  
  /**
   * 
   */
  @Test
  public void test() {
    NoaaImporter importer = this.factory.getImporter(1);
    ZonedDateTime datetimeref = ZonedDateTime.now(ZoneId.of("UTC"))
      .minusMonths(1)
      .truncatedTo(ChronoUnit.DAYS);
    GribFile gribfile = importer.getGribFile(datetimeref, 0);
    HrrrArchiveGribSource source = new HrrrArchiveGribSource();
    source.download(gribfile);
  }
}
