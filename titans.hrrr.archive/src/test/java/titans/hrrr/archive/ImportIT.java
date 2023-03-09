package titans.hrrr.archive;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import javax.measure.unit.SI;
import junitparams.JUnitParamsRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import titans.hrrr.archive.core.HrrrArchiveGribSource;
import titans.noaa.core.NoaaGribImporter;
import titans.noaa.core.NoaaVariable;
import titans.noaa.grib.GribFile;

/**
 *
 * @author Ricardo Marquez
 */
@RunWith(JUnitParamsRunner.class)
public class ImportIT extends BaseSpringITest {

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
    NoaaGribImporter importer = this.factory.getImporter(1);
    ZonedDateTime datetimeref = ZonedDateTime.now(ZoneId.of("UTC"))
      .minusMonths(2)
      .truncatedTo(ChronoUnit.DAYS);
    NoaaVariable var = new NoaaVariable("TMP_5000-ISBL", SI.CELSIUS);
    GribFile gribfile = importer.getGribFile(var, datetimeref, 0);
    HrrrArchiveGribSource source = new HrrrArchiveGribSource();
    source.download(gribfile);
  }
}
