package rm.titans.goeswest;

import com.rm.panzoomcanvas.Degrees;
import com.rm.panzoomcanvas.core.FxEnvelope;
import common.RmExceptions;
import java.io.File;
import java.io.IOException;
import ucar.ma2.Array;
import ucar.nc2.dt.GridDatatype;
import ucar.nc2.dt.grid.GridDataset;
import ucar.unidata.geoloc.LatLonRect;

/**
 *
 * @author Ricardo Marquez
 */
public class GoesImager {

  private final File file;
  
  /**
   * 
   * @param file 
   */
  public GoesImager(File file) {
    this.file = file;
  }

  /**
   *
   * @return
   */
  public FxEnvelope getBoundingBox() {
    FxEnvelope result;
    try (GridDataset dataset = GridDataset.open(this.file.getAbsolutePath())) {
      LatLonRect bbox = dataset.getBoundingBox();
      double latmin = bbox.getLatMin();
      double latmax = bbox.getLatMax();
      double lonmin = bbox.getLonMin();
      double lonmax = bbox.getLonMax();
      Degrees min = new Degrees(latmin, lonmin);
      Degrees max = new Degrees(latmax, lonmax);
      result = new FxEnvelope(min, max);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    return result;
  }

  /**
   *
   * @param point
   * @return
   */
  public double getValue(Degrees point) {
    double value;
    GridDataset dataset = null;
    try {
      dataset = GridDataset.open(this.file.getAbsolutePath());
      GridDatatype grid = dataset.getGrids().get(0);
      value = this.getValue(grid, point);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    } finally {
      RmExceptions.runAndEncloseException(dataset::close);
    }
    return value;
  }

  /**
   *
   * @param points
   * @return
   */
  public double[] getValue(Degrees[] points) {
    double[] result = new double[points.length];
    GridDataset dataset = null;
    try {
      dataset = GridDataset.open(this.file.getAbsolutePath());
      GridDatatype grid = dataset.getGrids().get(0);
      int i = -1;
      for (Degrees degrees : points) {
        i++;
        double value = this.getValue(grid, degrees);
        result[i] = value;
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    } finally {
      RmExceptions.runAndEncloseException(dataset::close);
    }
    return result;
  }
  
  /**
   * 
   * @param grid
   * @param degrees
   * @return 
   */
  private double getValue(GridDatatype grid, Degrees degrees) {
    try {
      int[] ints = new int[]{0, 1};
      int[] xyindex = grid.getCoordinateSystem()
        .findXYindexFromLatLon(degrees.getX(), degrees.getY(), ints);
      Array arr = grid.readDataSlice(xyindex[0], xyindex[1], 0, 1);
      double value = arr.getDouble(0);
      return value;
    } catch (IOException ex) {
      throw new RuntimeException(ex); 
    }
  }
}
