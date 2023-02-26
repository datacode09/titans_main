package rm.titansdata.properties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import rm.titansdata.SridUtils;

/**
 *
 * @author Ricardo Marquez
 */
public class Bounds implements Serializable {

  private final Point lowerleft, upperright;
  private final Map<Integer, Point> centers = new HashMap<>();

  public Bounds(Point lowerleft, Point upperright) {
    this.lowerleft = lowerleft;
    this.upperright = upperright;
  } 
  
  /**
   * 
   * @return 
   */
  public Polygon toPolygon() {
    Polygon result = this.getFactory().createPolygon(new Coordinate[]{
      new Coordinate(this.lowerleft.getX(), this.lowerleft.getY()),
      new Coordinate(this.upperright.getX(), this.lowerleft.getY()),
      new Coordinate(this.upperright.getX(), this.upperright.getY()),
      new Coordinate(this.lowerleft.getX(), this.upperright.getY()),
      new Coordinate(this.lowerleft.getX(), this.lowerleft.getY()),});
    return result;
  }
  
  /**
   * 
   * @param point
   * @return 
   */
  public double getDistanceFromCenter(Point point) {
    int srid = point.getSRID();
    Point center = this.getCenter(srid);
    double squareX = Math.pow(center.getX() - point.getX(), 2);
    double squareY = Math.pow(center.getY() - point.getY(), 2);
    double result = Math.sqrt(squareX + squareY);
    return result;
  }

  public Point lowerleft() {
    return lowerleft;
  }

  public Point upperright() {
    return upperright;
  }

  public double getMinX() {
    return this.lowerleft.getX();
  }

  public double getMaxX() {
    return this.upperright.getX();
  }

  public double getMinY() {
    return this.lowerleft.getY();
  }

  public double getMaxY() {
    return this.upperright.getY();
  }

  public double getLengthX() {
    return this.upperright.getX() - this.lowerleft.getX();
  }

  public double getLengthY() {
    return this.upperright.getY() - this.lowerleft.getY();
  }

  /**
   *
   * @param p
   * @return
   */
  public boolean contains(Point p) {
    Polygon envelope = this.getEnvelope();
    p = this.transformPoint(p);
    boolean result = envelope.contains(p);
    return result;
  }

  /**
   *
   * @param envelope
   * @param p
   * @return
   */
  private Point transformPoint(Point p) {
    int srid = this.getFactory().getSRID();
    Point result = SridUtils.transform(p, srid);
    return result;
  }

  /**
   *
   * @return
   */
  private Polygon getEnvelope() {
    GeometryFactory factory = this.lowerleft.getFactory();
    Coordinate[] coords = new Coordinate[]{
      new Coordinate(this.lowerleft.getX(), this.lowerleft.getY()),
      new Coordinate(this.upperright.getX(), this.lowerleft.getY()),
      new Coordinate(this.upperright.getX(), this.upperright.getY()),
      new Coordinate(this.lowerleft.getX(), this.upperright.getY()),
      new Coordinate(this.lowerleft.getX(), this.lowerleft.getY()),};
    Polygon envelope = factory.createPolygon(coords);
    return envelope;
  }

  /**
   *
   * @param p1
   * @param p2
   * @return
   */
  public static Bounds fromPoints(Point p1, Point p2) {
    if (!Objects.equals(p1.getFactory().getSRID(), p2.getFactory().getSRID())) {
      throw new RuntimeException("Invalid points. Different geometry factories.");
    }
    GeometryFactory factory = p1.getFactory();
    Point lowerleft = factory.createPoint(getMin(p1, p2));
    Point upperright = factory.createPoint(getMax(p1, p2));
    Bounds result = new Bounds(lowerleft, upperright);
    return result;
  }
  
  /**
   *
   * @param p1
   * @param p2
   * @return
   */
  public static Bounds fromPoints(Coordinate p1, Coordinate p2, int srid) {
    GeometryFactory factory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), srid);
    Point lowerleft = factory.createPoint(p1);
    Point upperright = factory.createPoint(p2);
    Bounds result = new Bounds(lowerleft, upperright);
    return result;
  }

  /**
   *
   * @param envelope
   * @return
   */
  static Bounds fromEnvelope(GeometryFactory gf, Envelope envelope) {
    Coordinate centre = envelope.centre();
    double height = envelope.getHeight();
    double width = envelope.getWidth();
    double x = centre.x;
    double y = centre.y;
    Point lowerleft = gf.createPoint(new Coordinate(x - 0.5 * width, y - 0.5 * height));
    Point upperright = gf.createPoint(new Coordinate(x + 0.5 * width, y + 0.5 * height));
    Bounds result = new Bounds(lowerleft, upperright);
    return result;
  }

  /**
   *
   * @param p1
   * @param p2
   * @return
   */
  private static Coordinate getMax(Point p1, Point p2) {
    double x = Math.max(p1.getX(), p2.getX());
    double y = Math.max(p1.getY(), p2.getY());
    Coordinate result = new Coordinate(x, y);
    return result;
  }

  /**
   *
   * @param p1
   * @param p2
   * @return
   */
  private static Coordinate getMin(Point p1, Point p2) {
    double x = Math.min(p1.getX(), p2.getX());
    double y = Math.min(p1.getY(), p2.getY());
    Coordinate result = new Coordinate(x, y);
    return result;
  }

  /**
   *
   * @return
   */
  public GeometryFactory getFactory() {
    return this.lowerleft.getFactory();
  }

  private Point getCenter(int srid) {
    if (!this.centers.containsKey(srid)) {
      double x = 0.5 * (this.getMaxX() + this.getMinX());
      double y = 0.5 * (this.getMaxY() + this.getMinY());
      Coordinate coordinate = new Coordinate(x, y);
      Point center = this.getFactory().createPoint(coordinate);
      Point centerTransformed = SridUtils.transform(center, srid);
      this.centers.put(srid, centerTransformed);
    }
    Point result = this.centers.get(srid);
    return result;
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {
    return "{" + "lowerleft=" + lowerleft + ", upperright=" + upperright + '}';
  }
  
  /**
   * 
   * @param srid
   * @return 
   */
  public Bounds transform(int srid) {
    Point p1 = SridUtils.transform(lowerleft, srid); 
    Point p2 = SridUtils.transform(upperright, srid);
    Bounds result = new Bounds(p1, p2); 
    return result; 
  }

  /**
   * 
   * @return 
   */
  public Point getCenter() {
    return this.getEnvelope().getCentroid();
  }
}
