package titans.hrrr.archive;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import rm.titansdata.SridUtils;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.raster.Raster;
import titans.hrrr.core.grib.HrrrSfcInventoryReader;
import titans.nam.NoaaParameter;
import titans.noaa.grib.ForecastTimeReference;

/**
 *
 * @author Ricardo Marquez
 */
public class RasterFactoryIT extends BaseSpringITest {

  @Autowired
  private HrrrArchiveRasterFactory factory;
  
  /**
   * 
   */
  @Test
  public void test() {
    long ms = System.currentTimeMillis();
    GeometryFactory geomfactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
    Point p1 = geomfactory.createPoint(new Coordinate(-119.43, 42.26));
    Point p2 = geomfactory.createPoint(new Coordinate(-121.43, 44.26));
    int targetSrid = 32610;
    Bounds b = Bounds.fromPoints( //
      SridUtils.transform(p1, targetSrid), //
      SridUtils.transform(p2, targetSrid));
    Measure<Length> dx = Measure.valueOf(1000.0, SI.METRE);
    Measure<Length> dy = Measure.valueOf(1000.0, SI.METRE);
    
    Dimensions dims = Dimensions.create(b, dx, dy);
    ZoneId utc = ZoneId.of("UTC");
    ZonedDateTime datetime = ZonedDateTime.of(LocalDateTime.of(2023, Month.OCTOBER, 26, 18, 00), utc);
    
    ForecastTimeReference d = new ForecastTimeReference(0, 0);
    String var = "DSWRF_0-SFC";
    Unit<?> unit = new HrrrSfcInventoryReader().getUnit(var);
    NoaaParameter p = new NoaaParameter("HRRR", datetime, d, var, unit);
    Integer projectId = 0;
    Raster raster = factory.create(projectId, p, b, dims);
    Point point = geomfactory.createPoint(new Coordinate(-120.43, 43.26));
    double value = raster.getValue(SridUtils.transform(point, targetSrid));
    System.out.println("value = " + value);
    System.out.println("elapsed time: " + (System.currentTimeMillis() - ms) / 1000.0);
  }
}
