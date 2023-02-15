package rm.titansdata.web;

import common.types.DateTimeRange;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/**
 *
 * @author Ricardo Marquez
 */
public class RequestParser {

  private final HttpServletRequest request;

  public RequestParser(HttpServletRequest request) {
    this.request = request;
  }

  public double getDouble(String name) {
    String p = this.request.getParameter(name);
    if (p == null) {
      throw new NullPointerException(String.format("Parameter '%s' does not exists", name));
    }
    return Double.parseDouble(p);
  }

  public String getString(String name) {
    return this.request.getParameter(name);
  }

  /**
   *
   * @param name
   * @return
   */
  public int getInteger(String name) {
    return Integer.parseInt(this.request.getParameter(name));
  }

  /**
   *
   * @param name
   * @return
   */
  public Instant getInstant(String name, ZoneId zoneId) {
    long val = Long.parseLong(request.getParameter(name));
    Date parse = new Date(val);
    Instant systemInstance = parse.toInstant();
    Instant refdate = ZonedDateTime.ofInstant(systemInstance, ZoneId.of("UTC"))
      .toOffsetDateTime().atZoneSameInstant(zoneId)
      .toInstant();
    return refdate;
  }

  /**
   *
   * @param name
   * @return
   */
  public DateTimeRange getDateTimeRange(String name, ZoneId zoneId) {
    JSONObject json;
    try {
      json = new JSONObject(request.getParameter(name));
    } catch (JSONException ex) {
      throw new RuntimeException(ex);
    }
    ZonedDateTime startDt;
    ZonedDateTime endDt;
    try {
      startDt = this.toRefDate(json.getLong("startDt"), zoneId);
      endDt = this.toRefDate(json.getLong("endDt"), zoneId);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    DateTimeRange result = new DateTimeRange(startDt, endDt);
    return result;
  }

  /**
   *
   * @param val
   * @param zoneId
   */
  private ZonedDateTime toRefDate(long val, ZoneId zoneId) {
    Date parse = new Date(val);
    Instant systemInstance = parse.toInstant();
    int systemOffsetSeconds = ZoneId.systemDefault().getRules().getOffset(systemInstance).getTotalSeconds();
    int zoneOffsetSeconds = zoneId.getRules().getOffset(systemInstance).getTotalSeconds();
    long correction = systemOffsetSeconds - zoneOffsetSeconds;
    Instant refdate = systemInstance.plusSeconds(correction);
    return ZonedDateTime.ofInstant(refdate, zoneId);
  }

  /**
   *
   * @param referenceDateArg
   * @param zoneIdArg
   * @return
   */
  public ZonedDateTime getZonedDateTime(String referenceDateArg, String zoneIdArg) {
    String datetext = this.request.getParameter(referenceDateArg);
    String zoneId = this.request.getParameter(zoneIdArg);

    ZonedDateTime result = ZonedDateTime
      .parse(datetext, new DateTimeFormatterBuilder().appendPattern("yyyyMMddHHmm")
        .toFormatter().withZone(ZoneId.of(zoneId)))
      .toOffsetDateTime().atZoneSimilarLocal(ZoneId.of(zoneId));
    return result;
  }

  /**
   *
   * @param param
   * @return
   */
  public Long getLong(String param) {
    long result = Long.parseLong(this.request.getParameter(param));
    return result;
  }

  /**
   *
   * @param geometry
   * @param srid
   * @return
   */
  public Geometry parseGeometry(String geometry, int srid) {
    try {
      GeometryFactory factory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), srid);
      WKTReader reader = new WKTReader(factory);
      String text = this.request.getParameter(geometry);
      Geometry result = reader.read(text);
      return result;
    } catch (ParseException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   *
   * @param rastergroupids
   * @return
   */
  public long[] getLongArray(String rastergroupids) {  
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * 
   * @param paramname
   * @param srid
   * @return 
   */
  public Point getPoint(String paramname, int srid) { 
    String json = this.request.getParameter(paramname);
    Point result = RequestParser.getPointFromJsonText(json, srid);
    return result;
  }
  /**
   *
   * @param jsontext
   * @param srid
   * @return
   */
  public static Point getPointFromJsonText(String jsontext, int srid) {
    try {
      PrecisionModel model = new PrecisionModel(PrecisionModel.FLOATING);
      GeometryFactory factory = new GeometryFactory(model, srid);
      JSONObject obj = new JSONObject(jsontext);
      double x = obj.getDouble("x");
      double y = obj.getDouble("y");
      Coordinate coordinate = new Coordinate(x, y);
      Point point = factory.createPoint(coordinate);
      return point;
    } catch(Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
