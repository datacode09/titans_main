package titan.goes;

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
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import rm.titansdata.Parameter;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.raster.Raster;
import titans.goes.GoesRasterFactory;
import titans.nam.NoaaParameter;

/**
 *
 * @author Ricardo Marquez
 */
public class GeosRasterFactoryIT extends BaseSpringITest {
  
  
  @Autowired
  private GoesRasterFactory factory;
  
  /**
   * 
   */
  @Test
  public void test() {
    Measure<Length> dx = Measure.valueOf(1000, SI.METRE);
    Bounds bounds = this.getBounds();
    Dimensions dims = Dimensions.create(bounds, dx, dx);
    String datetext = "2023-01-03T10:00:00";
    ZonedDateTime datetime = this.getZonedDateTime(datetext); 
    String noaaVar = "ABI-L1b-RadC$M6C01";
    Parameter p = new NoaaParameter("GOES-18", datetime, -1, noaaVar, Unit.ONE);
    Raster raster = this.factory.create(0, p, bounds, dims);
    Point point = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326)
      .createPoint(new Coordinate(-120.43, 37.36));
    double value = raster.getValue(point);
    System.out.println("value = " + value);
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
