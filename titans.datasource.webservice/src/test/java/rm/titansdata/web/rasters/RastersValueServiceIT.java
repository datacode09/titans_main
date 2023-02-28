package rm.titansdata.web.rasters;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import javax.measure.unit.SI;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.web.WebAppConfiguration;
import rm.titansdata.Parameter;
import rm.titansdata.properties.Bounds;
import rm.titansdata.raster.RasterCells;
import rm.titansdata.web.project.ProjectEntity;
import rm.titansdata.web.project.ProjectService;
import titans.nam.NoaaParameter;

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
  @Autowired
  private ProjectService projservice;
  
  @Before
  public void setup() throws Exception {
    TestContextManager testContextManager = new TestContextManager(getClass());
    testContextManager.prepareTestInstance(this);
  }

  @Test
  @Parameters({
    "3, 32610, POLYGON ((766962 4099080%2C 633468 4095923%2C 628042 4428834%2C 756099 4432069%2C 766962 4099080))"
  })
  public void test(long rasterId, int srid, String wkt) throws Exception {
    PrecisionModel pm = new PrecisionModel(PrecisionModel.FLOATING);
    GeometryFactory factory = new GeometryFactory(pm, srid);
    String decodeWkt = URLDecoder.decode(wkt, StandardCharsets.UTF_8.toString());
    Geometry geom = new WKTReader(factory).read(decodeWkt);
    ZonedDateTime datetime = ZonedDateTime
      .now(ZoneId.of("UTC")).minusDays(3).truncatedTo(ChronoUnit.DAYS); 
    Parameter p = new NoaaParameter("HRRR", datetime, 0, "TMP_2-HTGL", SI.CELSIUS); 
    int projectId = 790;
    ProjectEntity project = this.projservice.getProject(projectId);
    Bounds bounds = project.getBounds();
    RasterCells values = this.service.getRasterValues(rasterId, projectId, p, geom, bounds);
    System.out.println("values = " + values);
  }
}
