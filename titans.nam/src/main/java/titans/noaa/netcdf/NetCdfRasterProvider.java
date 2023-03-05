package titans.noaa.netcdf;

import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.properties.Properties;
import rm.titansdata.raster.Raster;
import rm.titansdata.raster.RasterObj;

/**
 *
 * @author Ricardo Marquez
 */
public class NetCdfRasterProvider {
  
  public NetCdfRasterProvider() {
  }

  /**
   *
   * @param netCdfFile
   * @return
   */
  public RasterObj getRaster(NetCdfFile netCdfFile) {
    Bounds bounds = netCdfFile.getBounds();
    Dimensions dims = netCdfFile.getDimensions();
    return getRaster(netCdfFile, bounds, dims);
  }

  /**
   *
   * @param netCdfFile
   * @param bounds
   * @param dims
   * @return
   */
  public RasterObj getRaster(NetCdfFile netCdfFile, Bounds bounds, Dimensions dims) {
    Raster raster = new NetCdfRaster(netCdfFile, bounds, dims);
    Properties properties = new Properties(bounds, dims.x.length, dims.y.length);
    String varName = netCdfFile.getVarName();
    RasterObj result = new RasterObj(varName, properties, raster);
    return result;
  }
}
