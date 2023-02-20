package rm.titansdata.plugin;

import rm.titansdata.Parameter;
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
  public Raster create(int projectId, Parameter param, Bounds bounds, Dimensions dims); 
}
