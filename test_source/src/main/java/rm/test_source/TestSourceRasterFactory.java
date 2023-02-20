package rm.test_source;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import rm.titansdata.plugin.RasterFactory;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.raster.BasicRaster;
import rm.titansdata.raster.Raster;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
public class TestSourceRasterFactory implements InitializingBean, RasterFactory {

  @Override
  public void afterPropertiesSet() throws Exception {
  }

  /**
   *
   * @return
   */
  @Override
  public String key() {
    return "test_source";
  }

  /**
   *
   * @param bounds
   * @param dims
   * @return
   */
  @Override
  public Raster create(int projectId, Parameter r, Bounds bounds, Dimensions dims) {
    BasicRaster result = new BasicRaster(Unit.ONE, bounds, dims) {
      @Override
      public double getValue(Point point) {
        if (bounds.contains(point)) {
          return 1.0;
        } else {
          return Double.NaN;
        }
      }

      @Override
      public Unit<? extends Quantity> getUnits() {
        return super.getUnits();
      }
      
    };
    return result;
  }

}
