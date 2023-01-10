package rm.titansdata.raster;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 *
 * @author Ricardo Marquez
 */
public class NullRaster implements Raster {

  @Override
  public double getValue(Point point) {
    return Double.NaN;
  }

  @Override
  public double getMeanValue(Geometry envelope) {
    return Double.NaN;
  }
  
  
  
}
