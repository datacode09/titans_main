package titans.nam;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import javax.measure.Measure;
import javax.measure.quantity.Length;
import javax.measure.unit.SI;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestContextManager;
import rm.titansdata.SridUtils;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.raster.Raster;
import titans.nam.grib.ForecastTimeReference;

/**
 *
 * @author Ricardo Marquez
 */
@RunWith(JUnitParamsRunner.class)
@ContextHierarchy({
  @ContextConfiguration(
    locations = {
      "/spring.xml"
    }
  )
})
public class NamRasterFactoryIT {

  @Autowired
  private NamRasterFactory factory;

  @Before
  public void setup() throws Exception {
    TestContextManager testContextManager = new TestContextManager(getClass());
    testContextManager.prepareTestInstance(this);
    SridUtils.init();
  }

  @Test
  @Parameters({
    "0", "-1"
  })
  public void getRasters(String userId) {
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
    ZonedDateTime datetime = ZonedDateTime.now(utc)
      .truncatedTo(ChronoUnit.DAYS)
      .minusDays(1);
    ForecastTimeReference d = new ForecastTimeReference(0, 0);
    String var = "TMP_2-HTGL";
    NamParameter p = new NamParameter("NAM", datetime, d, var);
    Raster r = factory.create(p, b, dims);
    Point point = geomfactory.createPoint(new Coordinate(-120.43, 43.26));
    double value = r.getValue(SridUtils.transform(point, targetSrid));
    System.out.println("value = " + value);
    System.out.println("elapsed time: " + (System.currentTimeMillis() - ms)/1000.0);
    
  }
}
