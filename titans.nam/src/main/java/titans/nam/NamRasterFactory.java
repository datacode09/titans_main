package titans.nam;

import java.io.File;
import java.time.ZonedDateTime;
import rm.titansdata.RasterFactory;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.raster.Raster;
import rm.titansdata.raster.RasterObj;

/**
 *
 * @author Ricardo Marquez
 */
public class NamRasterFactory implements RasterFactory {

  private final int forecaststep;
  private final ZonedDateTime datetimeref;
  private final NamImporter namImporter;

  /**
   *
   * @param forecaststep
   * @param datetimeref
   */
  public NamRasterFactory(File gribRootFolder, File degribExe, int forecaststep, ZonedDateTime datetimeref) {
    this.forecaststep = forecaststep;
    this.datetimeref = datetimeref;
    this.namImporter = new NamImporter(gribRootFolder, degribExe); 
  }

  /**
   *
   * @return
   */
  @Override
  public String key() {
    String key = "North American Model (Forecasts)";
    return key;
  }

  /**
   *
   * @param bounds
   * @param dims
   * @return
   */
  @Override
  public Raster create(Bounds bounds, Dimensions dims) {
    RasterObj rasterObj = this.namImporter.getRaster(this.forecaststep, this.datetimeref);
    Raster result = rasterObj.getRaster();
    return result;
  }
}
