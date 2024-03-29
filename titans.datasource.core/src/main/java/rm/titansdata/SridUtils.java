package rm.titansdata;

import org.apache.sis.geometry.DirectPosition2D;
import org.apache.sis.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;

/**
 *
 * @author Ricardo Marquez
 */
public class SridUtils {

  private SridUtils() {
  }

  /**
   *
   * @param geometry
   * @param targetSrid
   * @return
   */
  public static Geometry transform(Geometry geometry, int targetSrid) {
    Geometry result;
    GeometryFactory factory = new GeometryFactory(geometry.getPrecisionModel(), targetSrid);
    if (geometry.getGeometryType().equals("MultiPolygon")) {
      MultiPolygon mpolygon = (MultiPolygon) geometry;
      int numPolygons = mpolygon.getNumGeometries();
      Polygon[] polygons = new Polygon[numPolygons]; 
      for (int i = 0; i < numPolygons; i++) {
        Geometry a = mpolygon.getGeometryN(i); 
        polygons[i] = (Polygon) transform(a, targetSrid); 
      }
      result = factory.createMultiPolygon(polygons); 
    } else {
      Coordinate[] coords = geometry.getCoordinates();
      Coordinate[] transormedCoords = new Coordinate[coords.length];
      int i = -1;
      for (Coordinate coord : coords) {
        i++;
        Point p = geometry.getFactory().createPoint(coord);
        Point transformedPoint = transform(p, targetSrid);
        Coordinate transformedCoordinate = transformedPoint.getCoordinate();
        transormedCoords[i] = transformedCoordinate;
      }
      String geometryType = geometry.getGeometryType();
      switch (geometryType) {
        case "Point":
          result = factory.createPoint(transormedCoords[0]);
          break;
        case "MultiPoint":
          result = factory.createMultiPoint(transormedCoords);
          break;
        case "Polygon":
          result = factory.createPolygon(transormedCoords);
          break;
        case "LineString":
          result = factory.createLineString(transormedCoords);
          break;
        case "LinearRing":
          result = factory.createLinearRing(transormedCoords);
          break;
        default:
          throw new RuntimeException( //
            String.format("Unsupported geometry type: '%s'", geometryType));
      }
    }
    return result;
  }

  /**
   *
   */
  public static void init() {
    try {
      GeometryFactory f = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
      Point p = f.createPoint(new Coordinate(-121.43, 36.37));
      transform(p, 3857);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public static Point transform(Point p, int targetSrid) {
    if (targetSrid != p.getSRID()) {
      try {
        int sourceSrid = p.getSRID();
        if (targetSrid == 4326) {

          CoordinateReferenceSystem target = CRS.forCode("EPSG:" + targetSrid);
          CoordinateReferenceSystem source = CRS.forCode("EPSG:" + sourceSrid);
          CoordinateOperation op = CRS.findOperation(target, source, null);
          DirectPosition ptSrc = new DirectPosition2D(p.getX(), p.getY());
          DirectPosition ptDst = op.getMathTransform().inverse().transform(ptSrc, null);
          p = createPoint(ptDst.getCoordinate()[1], ptDst.getCoordinate()[0], targetSrid);
        } else if (sourceSrid == 4326) {
          CoordinateReferenceSystem target = CRS.forCode("EPSG:" + targetSrid);
          CoordinateReferenceSystem source = CRS.forCode("EPSG:" + sourceSrid);
          CoordinateOperation op = CRS.findOperation(target, source, null);
          DirectPosition ptSrc = new DirectPosition2D(p.getY(), p.getX());
          DirectPosition ptDst = op.getMathTransform().inverse().transform(ptSrc, null);
          p = createPoint(ptDst.getCoordinate()[0], ptDst.getCoordinate()[1], targetSrid);
        } else {
          CoordinateReferenceSystem target = CRS.forCode("EPSG:" + targetSrid);
          CoordinateReferenceSystem source = CRS.forCode("EPSG:" + sourceSrid);
          CoordinateOperation op = CRS.findOperation(source, target, null);
          DirectPosition ptSrc = new DirectPosition2D(p.getX(), p.getY());
          DirectPosition ptDst = op.getMathTransform().transform(ptSrc, null);
          p = createPoint(ptDst.getCoordinate()[0], ptDst.getCoordinate()[1], targetSrid);
        }
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
    return p;
  }

  private static Point createPoint(double x, double y, int srid) {
    GeometryFactory f = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), srid);
    Point result = f.createPoint(new Coordinate(x, y));
    return result;
  }
}
