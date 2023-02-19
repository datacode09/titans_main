package rm.titansdata.raster;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;



/**
 *
 * @author Ricardo Marquez
 */
public interface Raster {
  
  /**
   * 
   */
  public Unit<? extends Quantity> getUnits(); 
  
  /**
   * 
   * @param point 
   */  
  public double getValue(Point point); 
  
  /**
   * 
   * @param envelope
   * @return 
   */
  public double getMeanValue(Geometry envelope);
}
