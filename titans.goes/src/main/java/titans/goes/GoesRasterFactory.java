package titans.goes;

import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import rm.titansdata.plugin.RasterFactory;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.raster.Raster;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class GoesRasterFactory implements RasterFactory {
  
  @Override
  public String key() {
    
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Raster create(int projectId, Parameter param, Bounds bounds, Dimensions dims) {
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
}
