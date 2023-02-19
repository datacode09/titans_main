package rm.titansdata.raster;

import java.util.List;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class BasicRaster implements Raster {
  private Unit<? extends Quantity> units;
  private final Bounds bounds;
  private final Dimensions dims;
  
  
  /**
   * 
   * @param units
   * @param bounds
   * @param dims 
   */
  public BasicRaster(Unit<? extends Quantity> units, Bounds bounds, Dimensions dims) {
    this.bounds = bounds;
    this.dims = dims;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public Unit<? extends Quantity> getUnits() {
    return this.units;
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
