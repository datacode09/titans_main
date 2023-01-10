package rm.titansdata.raster;

import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;

/**
 *
 * @author Ricardo Marquez
 */
public class RasterCells {

  private final Bounds bounds;
  private final Dimensions dims;
  private final double[] values;

  /**
   * 
   * @param bounds
   * @param dims
   * @param values 
   */
  public RasterCells(Bounds bounds, Dimensions dims, double[] values) {
    this.bounds = bounds;
    this.dims = dims;
    this.values = values;
  }
  
  /**
   * 
   * @return 
   */
  public double[] values() {
    return this.values;
  }
  
  
  /**
   * 
   * @return 
   */
  @Override
  public String toString() {
    return "RasterCells{" + "bounds=" + bounds + ", dims=" + dims + '}';
  }
}
