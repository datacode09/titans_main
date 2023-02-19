package rm.titansdata.raster;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

/**
 *
 * @author Ricardo Marquez
 */
public class NullRaster implements Raster {

  public NullRaster() {
  }

  /**
   * 
   * @return 
   */
  @Override
  public Unit<? extends Quantity> getUnits() {
    return null;
  }

  /**
   * 
   * @param point
   * @return 
   */
  @Override
  public double getValue(Point point) {
    return Double.NaN;
  }
  
  /**
   * 
   * @param envelope
   * @return 
   */
  @Override
  public double getMeanValue(Geometry envelope) {
    return Double.NaN;
  }
  
  
  
}
