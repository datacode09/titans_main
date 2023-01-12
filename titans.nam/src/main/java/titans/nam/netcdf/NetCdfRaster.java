package titans.nam.netcdf;

import com.vividsolutions.jts.geom.Point;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.properties.Properties;
import rm.titansdata.raster.BasicRaster;
import rm.titansdata.raster.Raster;
import rm.titansdata.raster.RasterObj;
import ucar.ma2.Array;
import ucar.nc2.dt.GridCoordSystem;
import ucar.nc2.dt.GridDatatype;
import ucar.nc2.dt.grid.GridDataset;

/**
 *
 * @author Ricardo Marquez
 */
public class NetCdfRaster {

  public NetCdfRaster() {
  }

  /**
   *
   * @param netCdfFile
   * @return
   */
  public RasterObj getRaster(NetCdfFile netCdfFile) {
    Bounds bounds = netCdfFile.getBounds();
    Dimensions dims = netCdfFile.getDimensions();
    NetCdfRaster self = this;
    Raster raster = new BasicRaster(bounds, dims) {
      @Override
      public double getValue(Point point) {
        double result = self.getValue(netCdfFile, point);
        return result;
      }
    };
    Properties properties = new Properties(bounds, dims.x.length, dims.y.length);
    String varName = netCdfFile.getVarName();
    RasterObj result = new RasterObj(varName, properties, raster);
    return result;
  }

  /**
   *
   * @param point
   * @return
   */
  public double getValue(NetCdfFile netCdfFile, Point point) {
    double value;
    String varName = netCdfFile.getVarName();
    String filepath = netCdfFile.file.getAbsolutePath();
    try (GridDataset gds = GridDataset.open(filepath)) {
      GridDatatype grid = gds.getGrids().stream()
        .filter((g) -> g.getName().equals(varName))
        .findFirst()
        .orElse(gds.getGrids().get(0));
      if (grid == null) {
        throw new NullPointerException("Netcdf file '"
          + gds.getNetcdfFile() + "' does not contain variable " + varName);
      } GridCoordSystem gcs = grid.getCoordinateSystem();
      int[] xyIndex = gcs.findXYindexFromLatLon(point.getY(), point.getX(), null);
      if (xyIndex[0] == -1 || xyIndex[1] == -1) {
        throw new RuntimeException("coordinate indices not found for lat, lon pair : '"
          + point.getY() + ", " + point.getX() + "'");
      } Array arr = grid.readDataSlice(1, 1, xyIndex[1], xyIndex[0]);
      value = arr.getDouble(0);
    } catch(Exception ex) {
      throw new RuntimeException(ex); 
    }
    return value;
  }
}
