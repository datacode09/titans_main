package titans.hrrr.archive;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.measure.unit.SI;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
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
  @Parameters({
    "TMP_2-HTGL, 2022-12-03T10:00:00",
    "TMP_5000-ISBL, 2022-12-03T10:00:00", 
  })
  public void test(String varname, String datetext) {
    NoaaGribImporter importer = this.factory.getImporter(1);
    ZonedDateTime datetimeref = ZonedDateTime
        .of(LocalDateTime.parse(datetext, DateTimeFormatter.ISO_DATE_TIME), ZoneId.of("UTC"));
    NoaaVariable var = new NoaaVariable(varname, SI.CELSIUS);
    GribFile gribfile = importer.getGribFile(var, datetimeref, 0);
    HrrrArchiveGribSource source = new HrrrArchiveGribSource();
    source.download(gribfile);
  }
}
