package rm.titansdata.raster;

import java.util.List;
import java.util.Objects;
import javax.measure.Measure;
import javax.measure.quantity.Length;
import javax.measure.quantity.Quantity;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import rm.titansdata.properties.Bounds;

/**
 *
 * @author Ricardo Marquez
 */
public class CellsRaster implements Raster {

  private final Bounds bounds;
  private final Measure<Length> dx, dy;
  private final double[][] data;
  private final int Nx, Ny;
  private final List<Cell> cells;
  private final Unit<? extends Quantity> units;

  public CellsRaster(List<Cell> cells, //
    Unit<? extends Quantity> units, Bounds bounds, // 
    Measure<Length> dx, Measure<Length> dy) {
    this.cells = cells;
    this.units = Objects.requireNonNull(units);
    this.bounds = Objects.requireNonNull(bounds);
    this.dx = Objects.requireNonNull(dx);
    this.dy = Objects.requireNonNull(dy);
    double lengthX = bounds.getLengthX();
    this.Nx = Double.valueOf(lengthX / dx.doubleValue(SI.METRE)).intValue() - 1;
    double lengthY = bounds.getLengthY();
    this.Ny = Double.valueOf(lengthY / dy.doubleValue(SI.METRE)).intValue() - 1;
    this.data = new double[this.Nx][this.Ny];
    for (int i = 0; i < this.Nx; i++) {
      for (int j = 0; j < this.Ny; j++) {
        this.data[i][j] = Double.NaN;
      }
    }
    double minX = this.bounds.getMinX() + dx.doubleValue(SI.METRE) * 0.5;
    double minY = this.bounds.getMinY() + dy.doubleValue(SI.METRE) * 0.5;
    for (Cell cell : cells) {
      int i = Double.valueOf((cell.point.getX() - minX) / dx.doubleValue(SI.METRE)).intValue();
      int j = Double.valueOf((cell.point.getY() - minY) / dy.doubleValue(SI.METRE)).intValue();
      if (this.isValidIndexRange(i, j)) {
        this.data[i][j] = cell.getValue();
      }
    }
  }

  /**
   *
   * @return
   */
  @Override
  public Unit<? extends Quantity> getUnits() {
    return this.units;
  }

  /**
   *
   * @param point
   * @return
   */
  @Override
  public double getValue(Point point) {
    double minX = bounds.getMinX();
    double minY = bounds.getMinY();
    int i = Double.valueOf((point.getX() - minX) / dx.doubleValue(SI.METRE)).intValue();
    int j = Double.valueOf((point.getY() - minY) / dy.doubleValue(SI.METRE)).intValue();
    double result = this.getValue(i, j);
    return result;
  }

  /**
   *
   * @param i
   * @param j
   * @return
   */
  private double getValue(int i, int j) {
    double result;
    if (isValidIndexRange(i, j)) {
      result = this.data[i][j];
    } else {
      result = Double.NaN;
    }
    return result;
  }

  /**
   *
   * @param geometry
   * @return
   */
  @Override
  public double getMeanValue(Geometry geometry) {
    double sum = Double.NaN;
    int count = 0;
    for (Cell cell : this.cells) {
      if (cell.contains(geometry, dx, dy)) {
        count++;
        if (Double.isNaN(sum)) {
          sum = cell.getValue();
        } else {
          sum = sum + cell.getValue();
        }
      }
    }
    double result = count == 0 ? Double.NaN : sum / (double) count;
    return result;
  }

  private boolean isValidIndexRange(int i, int j) {
    boolean result = (0 <= i && i < this.Nx && 0 <= j && j < this.Ny);
    return result;
  }

}
