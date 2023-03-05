package titans.noaa.netcdf;

import common.RmExceptions;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import org.locationtech.jts.geom.Point;
import rm.titansdata.SridUtils;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.raster.BasicRaster;
import ucar.ma2.Array;
import ucar.nc2.dt.GridCoordSystem;
import ucar.nc2.dt.GridDatatype;
import ucar.nc2.dt.grid.GridDataset;

/**
 *
 * @author Ricardo Marquez
 */
public final class NetCdfRaster extends BasicRaster implements Closeable {

  private final NetCdfFile netCdfFile;
  private GridDataset griddatasetsCache = null;
  private Array arr;

  public NetCdfRaster(NetCdfFile netCdfFile, Bounds bounds, Dimensions dims) {
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
    double result = this.getValue(netCdfFile, point);
    return result;
  }

  /**
   *
   * @param point
   * @return
   */
  private double getValue(NetCdfFile netCdfFile, Point point) {
    this.initGridDataset(netCdfFile);
    GridDatatype grid = this.getGridDatatype(netCdfFile);
    int[] xyIndex = this.getQueryIndices(grid, point);
    double result = this.readValueForIndices(grid, xyIndex);
    return result;
  }

  /**
   *
   * @throws IOException
   */
  @Override
  public void close() throws IOException {
    if (this.griddatasetsCache != null) {
      this.griddatasetsCache.close();
    }
  }

  /**
   *
   * @param netCdfFile
   */
  private synchronized void initGridDataset(NetCdfFile netCdfFile) {
    if (this.griddatasetsCache == null) {
      String filepath = netCdfFile.file.getAbsolutePath();
      try {
        this.griddatasetsCache = GridDataset.open(filepath);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
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
    double lat = point.getY();
    double lon = point.getX();
    int[] xyIndex = gcs.findXYindexFromLatLon(lat, lon, null);
    return xyIndex;
  }

  /**
   *
   * @param netCdfFile
   * @return
   * @throws NullPointerException
   */
  private GridDatatype getGridDatatype(NetCdfFile netCdfFile) {
    String varName = netCdfFile.getVarName();
    List<GridDatatype> list = getGrid();
    GridDatatype grid = list
      .stream() //
      .filter((g) -> g.getName().equals(varName)) //
      .findFirst() //
      .orElse(list.get(0));
    if (grid == null) {
      throw RmExceptions.create( //
        "Netcdf file '%s' does not contain variable %s", // 
        griddatasetsCache.getNetcdfFile(), varName);
    }
    return grid;
  }

  private List<GridDatatype> getGrid() {
    this.initGridDataset(netCdfFile);
    List<GridDatatype> result = this.griddatasetsCache.getGrids();
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
    int xindex = xyIndex[0];
    int yindex = xyIndex[1];
    Array array = this.getCache(grid, xyIndex);
    if (xindex == -1 || yindex == -1) {
      value = Double.NaN;
    } else {
      int index = this.getOneDimensionIndex(grid, yindex, xindex);
      value = array.getDouble(index);
      return value;
    }
    return value;
  }
  
  /**
   * 
   * @param grid
   * @param yindex
   * @param xindex
   * @return 
   */
  private int getOneDimensionIndex(GridDatatype grid, int yindex, int xindex) {
    int nx = grid.getXDimension().getLength();
    int index = nx * yindex + xindex;
    return index;
  }
  
  
  /**
   * 
   * @param grid
   * @param xyIndex
   * @return 
   */
  private synchronized Array getCache(GridDatatype grid, int[] xyIndex) {
    int xindex = xyIndex[0];
    int yindex = xyIndex[1];
    Array result;
    try {
      if (this.arr == null) {
        this.arr = grid.readDataSlice(0, 0, -1, -1);
      }
      result = this.arr;
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    return result;
  }

}
