package rm.titansdata;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.apache.sis.geometry.DirectPosition2D;
import org.apache.sis.referencing.CRS;
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
    GeometryFactory factory = new GeometryFactory(geometry.getPrecisionModel(), targetSrid);
    Geometry result;
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
    return result;
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
