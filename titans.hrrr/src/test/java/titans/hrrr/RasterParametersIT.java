package titans.hrrr;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.measure.unit.SI;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import rm.titansdata.Parameter;
import rm.titansdata.plugin.Clazz;
import rm.titansdata.plugin.classes.ForecastStepClazz;
import titans.noaa.core.NoaaDateClazz;
import titans.noaa.core.NoaaVarClazz;

/**
 *
 * @author Ricardo Marquez
 */
@RunWith(JUnitParamsRunner.class)
public class RasterParametersIT extends BaseSpringITest {

  @Autowired
  private HrrrParametersFactory factory;

  /**
   *
   */
  @Test
  @Parameters({
    ", -1, 49",
    ", 0, 1",
    "2022-12-03T10:15:30, 0, 0"
  })
  public void test(String datetext, int fcststep, int expsize) {
    ZonedDateTime datetime;
    if (datetext.isEmpty()) {
      datetime = ZonedDateTime
        .now(ZoneId.of("UTC"))
        .truncatedTo(ChronoUnit.DAYS);
    } else {
      datetime = ZonedDateTime
        .of(LocalDateTime.parse(datetext, DateTimeFormatter.ISO_DATE_TIME), ZoneId.of("UTC"))
        .truncatedTo(ChronoUnit.DAYS);
    }
    String var = "TMP_2-HGTL";
    Clazz[] clazzes = new Clazz[]{
      new NoaaVarClazz(var, SI.KELVIN),
      new NoaaDateClazz(datetime),
      new ForecastStepClazz(fcststep)
    };
    List<Parameter> params = factory.getParameters(clazzes);
    Assert.assertEquals("# of parameters", expsize, params.size());
  }
}
