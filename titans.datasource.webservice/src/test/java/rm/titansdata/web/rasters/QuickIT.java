package rm.titansdata.web.rasters;

import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.WKTReader;

/**
 *
 * @author Ricardo Marquez
 */
public class QuickIT {

  @Test
  public void test() throws Exception {
    GeometryFactory factory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
    WKTReader reader = new WKTReader(factory);
    String text = "MULTIPOLYGON(((2 3, 1 2, 2 3)))";
    Geometry result = reader.read(text);
  }

}
