package titans.nam;

import java.io.File;
import java.time.ZonedDateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import rm.titansdata.RasterFactory;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.raster.Raster;
import rm.titansdata.raster.RasterObj;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Scope("prototype")
public class NamRasterFactory implements RasterFactory {
  private final NamImporter namImporter;
  
  /**
   *
   * @param forecaststep
   * @param datetimeref
   */
  public NamRasterFactory(
    @Qualifier("nam.gribRootFolder") File gribRootFolder, 
    @Qualifier("nam.degribExe") File degribExe) {
    this.namImporter = new NamImporter(gribRootFolder, degribExe); 
  }
  
  /**
   *
   * @return
   */
  @Override
  public String key(Parameter p) {
    String key = "North American Model (Forecast) 01";
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
      RasterObj rasterObj = this.namImporter.getRaster(fcststep, datetime);
      Raster result = rasterObj.getRaster();
      return result;
    } else {
      throw new InvalidArgumentTypeException(p, NamParameter.class); 
    }
  }
}
