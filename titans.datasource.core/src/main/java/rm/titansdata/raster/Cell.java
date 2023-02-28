package rm.titansdata.raster;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import javax.measure.Measure;
import javax.measure.quantity.Length;
import javax.measure.unit.SI;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 *
 * @author Ricardo Marquez
 */
public class Cell {

  private final Function<Point, Double> host;
  public final Point point;
  public final int[] ij;
  private final Map<Lengths, Polygon> map = new HashMap<>();
  

  protected Cell(Function<Point, Double> host, Point point, int[] ij) {
    this.host = host;
    this.point = point;
    this.ij = ij;
  }

  /**
   *
   * @return
   */
  public double getValue() {
    double result = this.host.apply(this.point);
    return result;
  }

  /**
   *
   * @param geometry
   * @param dx
   * @param dy
   * @return
   */
  boolean contains(Geometry geometry, Measure<Length> lenx, Measure<Length> leny) {
    Polygon envelope = this.getGeometry(lenx, leny);
    boolean result = envelope.contains(geometry);
    return result;
  }
  
  /**
   * 
   * @param lenx
   * @param leny
   * @return 
   */
  private Polygon getGeometry(Measure<Length> lenx, Measure<Length> leny) {
    Lengths lengths = new Lengths(lenx, leny);
    if (!map.containsKey(lengths)) {
      GeometryFactory factory = point.getFactory();
      double dx = lenx.doubleValue(SI.METRE);
      double dy = leny.doubleValue(SI.METRE);
      Polygon envelope = factory.createPolygon(new Coordinate[]{
        new Coordinate(this.point.getX() - dx, this.point.getY() - dy),
        new Coordinate(this.point.getX() - dx, this.point.getY() + dy),
        new Coordinate(this.point.getX() + dx, this.point.getY() + dy),
        new Coordinate(this.point.getX() + dx, this.point.getY() - dy),
        new Coordinate(this.point.getX() - dx, this.point.getY() - dy),});
      this.map.put(lengths, envelope);
    }
    Polygon result = this.map.get(lengths);
    return result;
  }

  @Override
  public String toString() {
    return "{" + " point=" + point + ", ij=" + Arrays.toString(ij) + ", map=" + map + '}';
  }

  private static class Lengths {
    
    private final Measure<Length> lenx;
    private final Measure<Length> leny;

    public Lengths(Measure<Length> lenx, Measure<Length> leny) {
      this.lenx = lenx;
      this.leny = leny;
    }

    @Override
    public int hashCode() {
      int hash = 3;
      hash = 23 * hash + Objects.hashCode(this.lenx);
      hash = 23 * hash + Objects.hashCode(this.leny);
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final Lengths other = (Lengths) obj;
      if (!Objects.equals(this.lenx, other.lenx)) {
        return false;
      }
      if (!Objects.equals(this.leny, other.leny)) {
        return false;
      }
      return true;
    }

  }
}
