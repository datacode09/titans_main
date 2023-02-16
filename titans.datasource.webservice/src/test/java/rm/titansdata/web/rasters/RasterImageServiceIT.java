package rm.titansdata.web.rasters;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import rm.titansdata.Parameter;
import rm.titansdata.properties.Bounds;
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
    Coordinate p1 = new Coordinate(-121.43, 37.36);
    Coordinate p2 = new Coordinate(p1.x + 0.1, p1.y + 0.1);
    Bounds bounds = Bounds.fromPoints(p1, p2, 4326).transform(32610);
    RasterImageResult img = this.imageService.getRasterImage(rasterId, param, bounds); 
    System.out.println("img = " + img);
  }
}
