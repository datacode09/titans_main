package titans.mrms;

import common.RmTimer;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.measure.Measure;
import javax.measure.quantity.Length;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.raster.Raster;
import titans.nam.NoaaParameter;


/**
 *
 * @author Ricardo Marquez
 */
public class RasterFactoryIT extends BaseSpringITest{
  
  @Autowired
  private MrmsRasterFactory factory;
    
  /**
   * 
   */
  @Test
  public void test() {
    String varName = "MultiSensor_QPE_01H_Pass2_00.00";
    String datetext = "2023-01-03T10:00:00";
    ZonedDateTime datetime = this.getZonedDateTime(datetext);
    int fcstStep = 0;
    String parentKey = "MRMS";
    NoaaParameter p = new NoaaParameter(parentKey, datetime, fcstStep, varName, Unit.ONE);
    Bounds bounds = this.getBounds();
    Measure<Length> dx = Measure.valueOf(1000d, SI.METRE);
    Dimensions dims = Dimensions.create(bounds, dx, dx);
    RmTimer timer = RmTimer.start(); 
    Raster raster = this.factory.create(0, p, bounds, dims);
    double value = raster.getValue(bounds.getCenter());
    System.out.println("value = " + value);
    timer.endAndPrint(); 
  }
  
  /**
   * 
   * @param datetext
   * @return 
   */
  private ZonedDateTime getZonedDateTime(String datetext) {
    DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    LocalDateTime localdatetime = LocalDateTime.parse(datetext, format);
    ZoneId zoneId = ZoneId.of("UTC");
    ZonedDateTime datetime = ZonedDateTime.of(localdatetime, zoneId);
    return datetime;
  }
    
  /**
   * 
   * @return 
   */
  private Bounds getBounds() {
    Coordinate lowerleft = new Coordinate(-121.43, 37.36);
    Coordinate upperright = new Coordinate(lowerleft.x + 0.5, lowerleft.y + 0.5);
    Bounds bounds = Bounds.fromPoints(lowerleft, upperright, 4326).transform(3857);
    return bounds;
  }
}
