package titans.nam;

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
import titans.nam.core.NamImporter;
import titans.nam.core.NoaaVariable;
import titans.nam.utils.InvalidArgumentTypeException;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@DependsOn({"nam.gribRootFolder", "nam.degribExe"}) 
public class NamRasterFactory implements RasterFactory {
  private final NamImporter.Builder namImporterBuilder;
  
  /**
   *
   * @param forecaststep
   * @param datetimeref
   */
  public NamRasterFactory(
    @Qualifier("nam.gribRootFolder") File gribRootFolder, 
    @Qualifier("nam.netCdfRootFolder") File netCdfRootFolder, 
    @Qualifier("nam.degribExe") File degribExe) {
    this.namImporterBuilder = new NamImporter.Builder().setGribRootFolder(gribRootFolder).setNetCdfRootFolder(netCdfRootFolder).setDegribExe(degribExe);
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
  public Raster create(int projectId, Parameter p, Bounds bounds, Dimensions dims) {
    if (p instanceof NoaaParameter) {
      NoaaParameter namparam = (NoaaParameter) p;
      int fcststep = namparam.fcststep;
      ZonedDateTime datetime = namparam.datetime;
      String varName = namparam.noaaVar;
      Unit<?> unit = namparam.getUnit();
      NoaaVariable var = new NoaaVariable(varName, unit);
      NamImporter importer = this.namImporterBuilder.setSubfolderId(projectId).build();
      RasterObj rasterObj = importer.getRaster(var, datetime, fcststep);
      Raster result = rasterObj.getRaster();
      return result;
    } else {
      throw new InvalidArgumentTypeException(p, NoaaParameter.class); 
    }
  }
}
