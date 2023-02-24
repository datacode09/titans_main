package titans.noaa.netcdf;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;
import org.locationtech.jts.geom.Point;
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

  private Map<NetCdfFile, GridDataset> gds = new HashMap<>();
  
  
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
    Unit<? extends Quantity> units = netCdfFile.getUnits();
    Raster raster = new BasicRaster(units, bounds, dims) {
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
    GridDatatype grid = this.getGridDatatype(netCdfFile);
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
    GridDatatype grid = this.gds.get(netCdfFile).getGrids().stream()
      .filter((g) -> g.getName().equals(varName))
      .findFirst()
      .orElse(gds.get(netCdfFile).getGrids().get(0));
    if (grid == null) {
      throw new NullPointerException("Netcdf file '"
        + gds.get(netCdfFile).getNetcdfFile() + "' does not contain variable " + varName);
    }
    return grid;
  }

  /**
   *
   * @param netCdfFile
   * @throws RuntimeException
   */
  private void initGridDataset(NetCdfFile netCdfFile) throws RuntimeException {
    if (!this.gds.containsKey(netCdfFile)) {
      String filepath = netCdfFile.file.getAbsolutePath();
      try {
        this.gds.put(netCdfFile, GridDataset.open(filepath)) ;
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
  }
  
  /**
   * 
   * @throws IOException 
   */
  @Override
  public void close() throws IOException {
    if (this.gds == null) {
      this.gds.forEach( (f, g)->{
        try {
          g.close();
        } catch (IOException ex) {
          Logger.getLogger(NetCdfRaster.class.getName()).log(Level.SEVERE, null, ex);
        }
      });
      this.gds.clear();
    }
  }

}
