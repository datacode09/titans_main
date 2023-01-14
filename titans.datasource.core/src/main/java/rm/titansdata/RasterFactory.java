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
  public String key(Parameter param); 
  
  /**
   * 
   * @param bounds
   * @param dims 
   */
  public Raster create(Parameter param, Bounds bounds, Dimensions dims); 
}
