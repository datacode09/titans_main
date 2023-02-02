package rm.titansdata.raster;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import java.util.List;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.raster.Cell;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class BasicRaster implements Raster {
  private final Bounds bounds;
  private final Dimensions dims;
  
  
  public BasicRaster(Bounds bounds, Dimensions dims) {
    this.bounds = bounds;
    this.dims = dims;
  }
  
  /**
   * 
   * @param point
   * @return 
   */
  @Override
  public abstract double getValue(Point point); 
    
  /**
   * 
   * @param envelope
   * @return 
   */
  @Override
  public final double getMeanValue(Geometry envelope) {
    if (envelope.getGeometryType().equals("Point")) {
      return this.getValue(envelope.getCentroid());
    }
    List<Cell> cells = this.getCells(envelope);
    double result = cells.stream()
      .mapToDouble(c->c.getValue())
      .average()
      .orElse(Double.NaN); 
    return result;
  }
  
  /**
   * 
   * @param envelope
   * @return 
   */
  private List<Cell> getCells(Geometry envelope) {
    RasterSearch helper = new RasterSearch(bounds, dims);
    List<Cell> result = helper.getCells(envelope, this::getValue);
    return result;
  }
  
}
