package rm.titansdata;

import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.raster.Raster;

/**
 *
 * @author Ricardo Marquez
 */
public interface RasterFactory {
      
  /**
   * 
   * @return 
   */
  public String key(); 
  
  /**
   * 
   * @param bounds
   * @param dims 
   */
  public Raster create(Bounds bounds, Dimensions dims); 
}
