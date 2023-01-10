package rm.titansdata.raster;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;



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
