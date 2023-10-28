package rm.titansdata.testdata;


import java.util.HashSet;
import java.util.Set;
import javax.measure.Measure;
import javax.measure.quantity.Length;
import javax.measure.unit.SI;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import rm.titansdata.heirarchy.Heirarchy;
import rm.titansdata.heirarchy.Node;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Properties;
import rm.titansdata.raster.NullRaster;
import rm.titansdata.raster.Raster;
import rm.titansdata.raster.RasterObj;

/**
 *
 * @author Ricardo Marquez
 */
public class HeirarchyITTestData {

  private HeirarchyITTestData() {
  }

  /**
   *
   * @return
   */
  public static Heirarchy getBasicHeirarchy() {
    Set<Node> children = new HashSet<>();
    int srid = 32610;
    GeometryFactory factory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), srid);
    Point p1 = factory.createPoint(new Coordinate(59881, 4738362));
    Point p2 = factory.createPoint(new Coordinate(1566121, 3599665));
    Bounds bounds = Bounds.fromPoints(p1, p2);
    Raster raster = new NullRaster();
    Measure<Length> dx = Measure.valueOf(1000d, SI.METRE);
    Measure<Length> dy = Measure.valueOf(1000d, SI.METRE);
    Properties properties = new Properties(bounds, dx, dy);
    children.add(Node.create(new RasterObj("a", properties, raster)));
    children.add(Node.create(new RasterObj("b", properties, raster)));
    children.add(Node.create(new RasterObj("c", properties, raster)));
    children.add(Node.create(new RasterObj("d", properties, raster)));
    children.add(Node.create(new RasterObj("e", properties, raster)));
    Heirarchy result = new Heirarchy("basic", children);
    return result;
  }

}
