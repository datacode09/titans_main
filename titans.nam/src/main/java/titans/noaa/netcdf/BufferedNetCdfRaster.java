package titans.noaa.netcdf;

import org.locationtech.jts.geom.Point;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.raster.BasicRaster;

/**
 *
 * @author Ricardo Marquez
 */
public class BufferedNetCdfRaster extends BasicRaster {

  private final NetCdfFile netCdfFile;
  
  public BufferedNetCdfRaster(NetCdfFile netCdfFile, Bounds bounds, Dimensions dims) {
    super(netCdfFile.getUnits(), bounds, dims);
    this.netCdfFile = netCdfFile;
  }
  
  /**
   * 
   * @param point
   * @return 
   */
  @Override
  public double getValue(Point point) {
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
}
