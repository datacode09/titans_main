package rm.titansdata.raster;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;



/**
 *
 * @author Ricardo Marquez
 */
public interface Raster {
  
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
