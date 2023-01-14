package rm.titansdata.web.rasters;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.web.WebAppConfiguration;
import rm.titansdata.raster.RasterCells;

/**
 *
 * @author Ricardo Marquez
 */
@RunWith(JUnitParamsRunner.class)
@WebAppConfiguration()
@ContextHierarchy({
  @ContextConfiguration(
    locations = {
      "/dispatcher-servlet.xml", "/applicationContext.xml"
    }
  ),})
public class RastersValueServiceIT {

  @Autowired
  private RastersValueService service;

  @Before
  public void setup() throws Exception {
    TestContextManager testContextManager = new TestContextManager(getClass());
    testContextManager.prepareTestInstance(this);
  }

  @Test
  @Parameters({
    "0, 32610, POLYGON ((766962 4099080%2C 633468 4095923%2C 628042 4428834%2C 756099 4432069%2C 766962 4099080))"
  })
  public void test(long rasterId, int srid, String wkt) throws Exception {
    PrecisionModel pm = new PrecisionModel(PrecisionModel.FLOATING);
    GeometryFactory factory = new GeometryFactory(pm, srid);
    String decodeWkt = URLDecoder.decode(wkt, StandardCharsets.UTF_8.toString());
    Geometry geom = new WKTReader(factory).read(decodeWkt);
    RasterCells values = this.service.getRasterValues(rasterId, geom);
    System.out.println("values = " + values);
  }
}
