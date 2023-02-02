package titans.nam.netcdf;

import org.locationtech.jts.geom.Point;
import java.io.Closeable;
import java.io.IOException;
import rm.titansdata.SridUtils;
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
public class NetCdfRaster implements Closeable {

  private GridDataset gds;

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
    this.initGridDataset(netCdfFile);
    GridDatatype grid = getGridDatatype(netCdfFile);
    int[] xyIndex = this.getQueryIndices(grid, point);
    double result = this.readValueForIndices(grid, xyIndex);
    return result;
  }

  /**
   *
   * @param grid
   * @param xyIndex
   * @return
   * @throws IOException
   */
  private double readValueForIndices(GridDatatype grid, int[] xyIndex) {
    double value;
    if (xyIndex[0] == -1 || xyIndex[1] == -1) {
      value = Double.NaN;
    } else {
      try {
        Array arr = grid.readDataSlice(1, 1, xyIndex[1], xyIndex[0]);
        value = arr.getDouble(0);
        return value;
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
    return value;
  }

  /**
   *
   * @param grid
   * @param argpoint
   * @return
   */
  private int[] getQueryIndices(GridDatatype grid, Point argpoint) {
    Point point = SridUtils.transform(argpoint, 4326);
    GridCoordSystem gcs = grid.getCoordinateSystem();
    int[] xyIndex = gcs.findXYindexFromLatLon(point.getY(), point.getX(), null);
    return xyIndex;
  }

  /**
   *
   * @param netCdfFile
   * @return
   * @throws NullPointerException
   */
  private GridDatatype getGridDatatype(NetCdfFile netCdfFile) throws NullPointerException {
    String varName = netCdfFile.getVarName();
    GridDatatype grid = this.gds.getGrids().stream()
      .filter((g) -> g.getName().equals(varName))
      .findFirst()
      .orElse(gds.getGrids().get(0));
    if (grid == null) {
      throw new NullPointerException("Netcdf file '"
        + gds.getNetcdfFile() + "' does not contain variable " + varName);
    }
    return grid;
  }

  /**
   *
   * @param netCdfFile
   * @throws RuntimeException
   */
  private void initGridDataset(NetCdfFile netCdfFile) throws RuntimeException {
    if (this.gds == null) {
      String filepath = netCdfFile.file.getAbsolutePath();
      try {
        this.gds = GridDataset.open(filepath);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  @Override
  public void close() throws IOException {
    if (this.gds == null) {
      this.gds.close();
      this.gds = null;
    }
  }

}
