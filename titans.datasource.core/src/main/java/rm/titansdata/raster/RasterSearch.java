package rm.titansdata.raster;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import javafx.util.Pair;
import javax.measure.unit.SI;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import rm.titansdata.SridUtils;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;

/**
 *
 * @author Ricardo Marquez
 */
public class RasterSearch {

  private final Bounds bounds;
  private final Dimensions dims;

  public RasterSearch(Bounds bounds, Dimensions dims) {
    this.bounds = bounds;
    this.dims = dims;
  }

  /**
   *
   * @param host
   * @return
   */
  public Stream<Pair<Integer, Cell>> stream(Function<Point, Double> host) {
    double deltax = this.dims.getDeltaX(SI.METRE);
    double deltay = this.dims.getDeltaY(SI.METRE);
    double halfdeltax = 0.5 * deltax;
    double maxX = this.bounds.getMaxX();
    double minX = this.bounds.getMinX();
    Stream.Builder<Pair<Integer, Cell>> builder = Stream.builder();
    int index = -1;
    int i = -1;
    GeometryIntersectHelper helper = new GeometryIntersectHelper(bounds, null);
    double halfdeltay = 0.5 * deltay;
    double minY = this.bounds.getMinY();
    double maxY = this.bounds.getMaxY();
    for (double x = minX + halfdeltax; x <= maxX - halfdeltax; x = x + deltax) {
      int jj = -1;
      i++;
      for (double y = minY + halfdeltay; y <= maxY - halfdeltay; y = y + deltay) {
        index++;
        jj++;
        int[] ij = new int[]{i, jj};
        Point point = helper.toEnvelope(x, halfdeltax, y, halfdeltay).getCentroid();
        Pair<Integer, Cell> pair = new Pair<>(index, new Cell(host, point, ij));
        builder.add(pair);
      }
    }
    Stream<Pair<Integer, Cell>> result = builder.build();
    return result;
  }

  /**
   *
   * @param geometry
   * @return
   */
  public List<Cell> getCells(Geometry geometry, Function<Point, Double> supplier) {
    Geometry targetGeometry = this.toTargetGeometry(geometry);
    List<Cell> result = new ArrayList<>();
    double deltax = this.dims.getDeltaX(SI.METRE);
    double deltay = this.dims.getDeltaY(SI.METRE);
    double halfdeltax = 0.5 * deltax;
    double maxX = this.bounds.getMaxX();
    double minX = this.bounds.getMinX();
    int i = -1;
    GeometryIntersectHelper helper = new GeometryIntersectHelper(this.bounds, targetGeometry);
    for (double x = minX + halfdeltax; x <= maxX - halfdeltax; x = x + deltax) {
      i++;
      double halfdeltay = 0.5 * deltay;
      double minY = this.bounds.getMinY();
      double maxY = this.bounds.getMaxY();
      int j = -1;
      for (double y = minY + halfdeltay; y <= maxY - halfdeltay; y = y + deltay) {
        j++;
        int[] ij = new int[]{i, j};
        boolean intersects = helper.intersects(x, halfdeltax, y, halfdeltay);
        if (intersects) {
          Point point = helper.getPoint(x, y);
          Cell cell = new Cell(supplier, point, ij);
          result.add(cell);
        }
      }
    }
    return result;
  }

  /**
   *
   * @param geometry
   * @return
   */
  private Geometry toTargetGeometry(Geometry geometry) {
    Geometry targetGeometry = SridUtils.transform(geometry, this.bounds.getFactory().getSRID());
    return targetGeometry;
  }

  /**
   *
   * @return
   */
  int size() {
    double maxX = this.bounds.getMaxX();
    double minX = this.bounds.getMinX();
    double maxY = this.bounds.getMaxY();
    double minY = this.bounds.getMinY();
    int numx = Double.valueOf((maxX - minX) / this.dims.getDeltaX(SI.METRE)).intValue();
    int numy = Double.valueOf((maxY - minY) / this.dims.getDeltaY(SI.METRE)).intValue();
    return numx * numy;
  }

  /**
   *
   * @return
   */
  public int[] maxIndices() {
    double maxX = this.bounds.getMaxX();
    double minX = this.bounds.getMinX();
    double maxY = this.bounds.getMaxY();
    double minY = this.bounds.getMinY();
    int numx = Double.valueOf((maxX - minX) / this.dims.getDeltaX(SI.METRE)).intValue();
    int numy = Double.valueOf((maxY - minY) / this.dims.getDeltaY(SI.METRE)).intValue();
    int[] result = new int[]{numx, numy};
    return result;
  }

  private static class GeometryIntersectHelper {

    private final Bounds bounds;
    private final GeometryFactory factory;
    private final Geometry geometry;
    private final Envelope outerEnvelope;
    private final Envelope internalEnvelope;

    private GeometryIntersectHelper(Bounds bounds, Geometry geometry) {
      this.bounds = bounds;
      this.factory = this.bounds.getFactory();
      this.geometry = geometry;
      if (geometry != null) {
        this.outerEnvelope = this.geometry.getEnvelopeInternal();
      } else {
        this.outerEnvelope = null;
      }
      if (geometry != null) {
        Envelope e = new Envelope(this.outerEnvelope.centre());
        e.expandBy(1000.0);
        Envelope last = e;
        while (this.geometry.covers(this.factory.createPolygon(toCoordinateArray(e)))) {
          last = e.copy();
          e.expandBy(1000.0);

        }
        this.internalEnvelope = last;
      } else {
        this.internalEnvelope = null;
      }
    }

    private static Coordinate[] toCoordinateArray(Envelope e) {
      Coordinate[] result = new Coordinate[]{
        new Coordinate(e.getMinX(), e.getMinY()),
        new Coordinate(e.getMaxX(), e.getMinY()),
        new Coordinate(e.getMaxX(), e.getMaxY()),
        new Coordinate(e.getMinX(), e.getMaxY()),
        new Coordinate(e.getMinX(), e.getMinY()),};
      return result;
    }

    /**
     *
     * @param x
     * @param halfdeltax
     * @param y
     * @param halfdeltay
     * @return
     */
    private boolean intersects(double x, double halfdeltax, double y, double halfdeltay) {
      double xminus = x - halfdeltax;
      double xplus = x + halfdeltax;
      double yminus = y - halfdeltay;
      double yplus = y + halfdeltay;
      if (xminus < this.outerEnvelope.getMinX()) {
        return false;
      } else if (xplus > this.outerEnvelope.getMaxX()) {
        return false;
      } else if (yminus < this.outerEnvelope.getMinY()) {
        return false;
      } else if (yplus > this.outerEnvelope.getMaxY()) {
        return false;
      }
      if ((this.internalEnvelope.getMinX() < xminus && xplus < this.internalEnvelope.getMaxX())
        && (this.internalEnvelope.getMinY() < yminus && yplus < this.internalEnvelope.getMaxY())) {
        return true;
      }
      Polygon e = this.toEnvelope(x, halfdeltax, y, halfdeltay);
      boolean result = this.geometry.intersects(e);
      return result;
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    private Point getPoint(double x, double y) {
      Point result = this.factory.createPoint(new Coordinate(x, y));
      return result;
    }

    /**
     *
     * @param x
     * @param halfdeltax
     * @param y
     * @param halfdeltay
     * @return
     */
    private Polygon toEnvelope(double x, double halfdeltax, double y, double halfdeltay) {
      Polygon cellEnvelope = this.factory.createPolygon(new Coordinate[]{
        new Coordinate(x - halfdeltax, y - halfdeltay),
        new Coordinate(x - halfdeltax, y + halfdeltay),
        new Coordinate(x + halfdeltax, y + halfdeltay),
        new Coordinate(x + halfdeltax, y - halfdeltay),
        new Coordinate(x - halfdeltax, y - halfdeltay),});
      return cellEnvelope;
    }

  }
}
