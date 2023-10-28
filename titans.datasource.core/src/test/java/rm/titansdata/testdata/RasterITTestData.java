package rm.titansdata.testdata;


import javax.measure.Measure;
import javax.measure.quantity.Length;
import javax.measure.quantity.Temperature;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.properties.Properties;
import rm.titansdata.raster.BasicRaster;
import rm.titansdata.raster.Raster;
import rm.titansdata.raster.RasterObj;

/**
 *
 * @author Ricardo Marquez
 */
public class RasterITTestData {
  
  public static RasterObj getBasicRasterObj() {
    Bounds bounds = getBounds();
    Properties properties = getProperties(bounds);
    RasterObj result = new RasterObj("test", properties, getBasicRaster());
    return result;
  }
  
  
  private static Properties getProperties(Bounds bounds) {
    Measure<Length> dx = Measure.valueOf(1d, SI.KILOMETRE);
    Measure<Length> dy = Measure.valueOf(1d, SI.KILOMETRE);
    Properties properties = new Properties(bounds, dx, dy);
    return properties;
  }
  
  public static Raster getBasicRaster() { 
    Bounds b = getBounds();
    Properties p = getProperties(b); 
    Dimensions dims = p.getDimensions();
    Unit<Temperature> units = SI.KELVIN;
    Raster result = new BasicRaster(units, b, dims) {
      @Override
      public double getValue(Point point) {
        double result;
        if (b.contains(point)) {
          result = b.getDistanceFromCenter(point);
        } else {
          result = Double.NaN;
        }
        return result;
      }
    };
    return result;
  }
  
  /**
   * 
   * @return 
   */
  private static Bounds getBounds() {
    int srid = 32610;
    GeometryFactory factory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), srid);
    Point p1 = factory.createPoint(new Coordinate(59881, 4738362));
    Point p2 = factory.createPoint(new Coordinate(1566121, 3599665));
    Bounds b = Bounds.fromPoints(p1, p2);
    return b;
  }

  private RasterITTestData() {
  }

}
