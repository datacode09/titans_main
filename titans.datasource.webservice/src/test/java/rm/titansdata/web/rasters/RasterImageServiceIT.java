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
import titans.nam.NamParameter;
import titans.nam.grib.ForecastTimeReference;

/**
 *
 * @author Ricardo Marquez
 */
@RunWith(JUnitParamsRunner.class)
public class RasterImageServiceIT extends BaseSpringTest {
    
  @Autowired
  private RasterImageService imageService;
  
  @Test
  @Parameters({""})
  public void test(String a) {
    String varName = "TMP_2-HTGL";
    ZonedDateTime datetime = ZonedDateTime
      .now(ZoneId.of("UTC"))
      .truncatedTo(ChronoUnit.DAYS)
      ;
    ForecastTimeReference fcststep = new ForecastTimeReference(0, 0);
    String parentKey = "North American Model Forecasts";
    Parameter param = new NamParameter(parentKey, datetime, fcststep, varName);
    int rasterId = 1; 
    RasterImageResult img = this.imageService.getRasterImage(rasterId, param); 
    System.out.println("img = " + img);
    
  }
}
