package titans.hrrr;

import java.io.File;
import java.time.ZonedDateTime;
import javax.measure.unit.Unit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import rm.titansdata.plugin.RasterFactory;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.raster.Raster;
import rm.titansdata.raster.RasterObj;
import titans.hrrr.core.HrrrImporter;
import titans.hrrr.core.grib.HrrrInventoryReader;
import titans.nam.NoaaParameter;
import titans.nam.core.NoaaVariable;
import titans.nam.utils.InvalidArgumentTypeException;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@DependsOn({"hrrr.gribRootFolder", "hrrr.degribExe"})
public class HrrrRasterFactory implements RasterFactory {

  private final HrrrImporter importer;

  public HrrrRasterFactory(
    @Qualifier("hrrr.gribRootFolder") File gribRootFolder,
    @Qualifier("hrrr.netCdfRootFolder") File netCdfRootFolder,
    @Qualifier("hrrr.degribExe") File degribExe
  ) {
    this.importer = new HrrrImporter(gribRootFolder, netCdfRootFolder, degribExe);
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public String key() {
    String key = "High Resolution Rapid Refresh";
    return key;
  }
  
  /**
   * 
   * @param p
   * @param bounds
   * @param dims
   * @return 
   */
  @Override
  public Raster create(Parameter p, Bounds bounds, Dimensions dims) {
    if (p instanceof NoaaParameter) {
      NoaaParameter namparam = (NoaaParameter) p;
      int fcststep = namparam.fcststep;
      ZonedDateTime datetime = namparam.datetime;
      String name = "TMP_2-HTGL";
      Unit<?> unit = new HrrrInventoryReader().getUnit(name);
      NoaaVariable var = new NoaaVariable(name, unit);
      RasterObj rasterObj = this.importer.getRaster(var, datetime, fcststep);
      Raster result = rasterObj.getRaster();
      return result;
    } else {
      throw new InvalidArgumentTypeException(p, NoaaParameter.class);
    }
  }

}
