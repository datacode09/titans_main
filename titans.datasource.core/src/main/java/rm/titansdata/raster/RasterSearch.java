package rm.titansdata.raster;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import javafx.util.Pair;
import javax.measure.unit.SI;
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
  public Stream<Pair<Integer,Cell>> stream(Function<Point, Double> host) {
    double deltax = this.dims.getDeltaX(SI.METRE);
    double deltay = this.dims.getDeltaY(SI.METRE);
    double halfdeltax = 0.5 * deltax;
    double maxX = this.bounds.getMaxX();
    double minX = this.bounds.getMinX();
    Stream.Builder<Pair<Integer,Cell>> builder = Stream.builder();
    int i = -1;
    int ii = -1;
    for (double x = minX + halfdeltax; x <= maxX - halfdeltax; x = x + deltax) {
      double halfdeltay = 0.5 * deltay;
      double minY = this.bounds.getMinY();
      double maxY = this.bounds.getMaxY();
      int jj = -1;
      ii++;
      for (double y = minY + halfdeltay; y <= maxY - halfdeltay; y = y + deltay) {
        i++; 
        jj++;
        int[] ij = new int[]{ii, jj}; 
        Point point = this.toEnvelope(x, halfdeltax, y, halfdeltay).getCentroid();
        Pair<Integer, Cell> pair = new Pair<>(i, new Cell(host, point, ij)); 
        builder.add(pair); 
      }
      
    }
    return builder.build();
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
    for (double x = minX + halfdeltax; x <= maxX - halfdeltax; x = x + deltax) {
      double halfdeltay = 0.5 * deltay;
      double minY = this.bounds.getMinY();
      double maxY = this.bounds.getMaxY();
      int j = -1;
      for (double y = minY + halfdeltay; y <= maxY - halfdeltay; y = y + deltay) {
        i++;
        j++; 
        int[] ij = new int[]{i, j}; 
        Polygon cellEnvelope = this.toEnvelope(x, halfdeltax, y, halfdeltay);
        if (targetGeometry.intersects(cellEnvelope)) {
          result.add(new Cell(supplier, cellEnvelope.getCentroid(), ij));
        }
      }
    }
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
    GeometryFactory f = this.bounds.getFactory();
    Polygon cellEnvelope = f.createPolygon(new Coordinate[]{
      new Coordinate(x - halfdeltax, y - halfdeltay),
      new Coordinate(x - halfdeltax, y + halfdeltay),
      new Coordinate(x + halfdeltax, y + halfdeltay),
      new Coordinate(x + halfdeltax, y - halfdeltay),
      new Coordinate(x - halfdeltax, y - halfdeltay),});
    return cellEnvelope;
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
    int numx = Double.valueOf((maxX - minX)/this.dims.getDeltaX(SI.METRE)).intValue(); 
    int numy = Double.valueOf((maxY - minY)/this.dims.getDeltaY(SI.METRE)).intValue(); 
    return numx*numy;
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
    int numx = Double.valueOf((maxX - minX)/this.dims.getDeltaX(SI.METRE)).intValue(); 
    int numy = Double.valueOf((maxY - minY)/this.dims.getDeltaY(SI.METRE)).intValue(); 
    int[] result = new int[]{numx, numy};
    return result;
  }

}
