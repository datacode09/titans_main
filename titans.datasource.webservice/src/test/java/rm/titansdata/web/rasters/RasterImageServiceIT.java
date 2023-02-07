package rm.titansdata.web.rasters;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import rm.titansdata.Parameter;
import rm.titansdata.test.help.BaseSpringTest;
import titans.nam.NoaaParameter;
import titans.nam.grib.ForecastTimeReference;

/**
 *
 * @author Ricardo Marquez
 */
@RunWith(JUnitParamsRunner.class)
public class RasterImageServiceIT extends BaseSpringTest {
    
  @Autowired
  private RasterImageService imageService;
  
  /**
   * 
   * @param a 
   */
  @Test
  @Parameters({
    "1, North American Model Forecasts", 
    "2, High Resolution Rapid Refresh", 
  })
  public void test(int rasterId, String parentKey) {
    String varName = "TMP_2-HTGL";
    ZonedDateTime datetime = ZonedDateTime
      .now(ZoneId.of("UTC"))
      .minusDays(1l)
      .truncatedTo(ChronoUnit.DAYS);
    ForecastTimeReference fcststep = new ForecastTimeReference(0, 0);
    Parameter param = new NoaaParameter(parentKey, datetime, fcststep, varName);
    RasterImageResult img = this.imageService.getRasterImage(rasterId, param); 
    System.out.println("img = " + img);
    
  }
}
