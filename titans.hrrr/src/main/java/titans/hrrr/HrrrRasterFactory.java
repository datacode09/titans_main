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

  private final HrrrImporter.Builder hrrrImporterBuilder;

  public HrrrRasterFactory(
    @Qualifier("hrrr.gribRootFolder") File gribRootFolder,
    @Qualifier("hrrr.netCdfRootFolder") File netCdfRootFolder,
    @Qualifier("hrrr.degribExe") File degribExe
  ) {
    this.hrrrImporterBuilder = new HrrrImporter.Builder()
      .setDegribExe(degribExe).setGribRootFolder(gribRootFolder).setNetCdfRootFolder(netCdfRootFolder)
      ;
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
  public Raster create(int projectId, Parameter p, Bounds bounds, Dimensions dims) {
    if (p instanceof NoaaParameter) {
      NoaaParameter namparam = (NoaaParameter) p;
      int fcststep = namparam.fcststep;
      ZonedDateTime datetime = namparam.datetime;
      String varName = namparam.noaaVar;
      Unit<?> unit = namparam.getUnit();
      NoaaVariable var = new NoaaVariable(varName, unit);
      HrrrImporter importer = this.hrrrImporterBuilder
        .setSubfolderId(projectId).build();
      RasterObj rasterObj = importer.getRaster(var, datetime, fcststep);
      Raster result = rasterObj.getRaster();
      return result;
    } else {
      throw new InvalidArgumentTypeException(p, NoaaParameter.class);
    }
  }

}
