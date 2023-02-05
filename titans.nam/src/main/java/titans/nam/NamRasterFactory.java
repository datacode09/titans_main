package titans.nam;

import java.io.File;
import java.time.ZonedDateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import rm.titansdata.plugin.RasterFactory;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.raster.Raster;
import rm.titansdata.raster.RasterObj;
import titans.nam.core.NamImporter;
import titans.nam.core.NamVariable;
import titans.nam.utils.InvalidArgumentTypeException;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@DependsOn({"nam.gribRootFolder", "nam.degribExe"}) 
public class NamRasterFactory implements RasterFactory {
  private final NamImporter namImporter;
  
  /**
   *
   * @param forecaststep
   * @param datetimeref
   */
  public NamRasterFactory(
    @Qualifier("nam.gribRootFolder") File gribRootFolder, 
    @Qualifier("nam.netCdfRootFolder") File netCdfRootFolder, 
    @Qualifier("nam.degribExe") File degribExe) {
    this.namImporter = new NamImporter(gribRootFolder, netCdfRootFolder, degribExe); 
  }
  
  /**
   *
   * @return
   */
  @Override
  public String key() {
    String key = "North American Model Forecasts";
    return key;
  }

  /**
   *
   * @param bounds
   * @param dims
   * @return
   */
  @Override
  public Raster create(Parameter p, Bounds bounds, Dimensions dims) {
    if (p instanceof NamParameter) {
      NamParameter namparam = (NamParameter) p;
      int fcststep = namparam.fcststep;
      ZonedDateTime datetime = namparam.datetime;
      NamVariable var = new NamVariable("TMP_2-HTGL");
      RasterObj rasterObj = this.namImporter.getRaster(var, datetime, fcststep);
      Raster result = rasterObj.getRaster();
      return result;
    } else {
      throw new InvalidArgumentTypeException(p, NamParameter.class); 
    }
  }
}
