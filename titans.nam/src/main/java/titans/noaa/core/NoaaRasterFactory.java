package titans.noaa.core;

import java.time.ZonedDateTime;
import javax.measure.unit.Unit;
import rm.titansdata.Parameter;
import rm.titansdata.plugin.RasterFactory;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.raster.Raster;
import rm.titansdata.raster.RasterObj;
import titans.nam.NoaaParameter;
import titans.nam.utils.InvalidArgumentTypeException;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class NoaaRasterFactory implements RasterFactory {

  /**
   *
   * @param p
   * @param bounds
   * @param dims
   * @return
   */
  @Override
  public final Raster create(int projectId, Parameter p, Bounds bounds, Dimensions dims) {
    if (p instanceof NoaaParameter) {
      NoaaParameter namparam = (NoaaParameter) p;
      int fcststep = namparam.fcststep;
      ZonedDateTime datetime = namparam.datetime;
      String varName = namparam.noaaVar;
      Unit<?> unit = namparam.getUnit();
      NoaaVariable var = new NoaaVariable(varName, unit);
      NoaaImporter importer = this.getImporter(projectId);
      RasterObj rasterObj = importer.getRaster(var, datetime, fcststep);
      Raster result = rasterObj.getRaster();
      return result;
    } else {
      throw new InvalidArgumentTypeException(p, NoaaParameter.class);
    }
  }

  /**
   *
   * @param projectId
   * @return
   */
  protected abstract NoaaImporter getImporter(int projectId);
}
