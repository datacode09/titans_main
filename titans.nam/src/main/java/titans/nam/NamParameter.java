package titans.nam;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import rm.titansdata.Parameter;
import titans.nam.grib.ForecastTimeReference;

/**
 *
 * @author Ricardo Marquez
 */
public class NamParameter implements Parameter {

  private final String parentKey;

  public ZonedDateTime datetime;
  public int fcststep;
  
  /**
   * 
   * @param datetime
   * @param d 
   */
  public NamParameter(String parentKey, ZonedDateTime datetime, ForecastTimeReference d) {
    this.parentKey = parentKey;
    this.datetime = datetime.plusHours(d.refhour);
    this.fcststep = d.fcsthourAhead;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public String toString() {  
    return "{" + "datetime=" + datetime + ", fcststep=" + fcststep + '}';
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public JSONObject toJSONObject() {
    JSONObject result = new JSONObject();
    try {
      String format = this.datetime.format(getDateTimeFormatter());
      result.put("key",  format + "-" + this.fcststep); 
      result.put("parentKey", this.parentKey);
      result.put("fcststep", this.fcststep); 
      result.put("datetime", format);
      result.put("zoneid", this.datetime.getZone().getId());
      JSONArray view = new JSONArray(); 
      view.put(0, new JSONObject(){{
        put("type", "label"); 
        put("name", "key"); 
        put("value", format + "-" + fcststep); 
      }}); 
      view.put(1, new JSONObject(){{
        put("type", "label"); 
        put("name", "Reference Date"); 
        put("value", datetime.toString()); 
      }}); 
      view.put(2, new JSONObject(){{
        put("type", "label"); 
        put("name", "Forecast Step"); 
        put("value", fcststep); 
      }}); 
      result.put("view", view.toString()); 
    } catch(Exception ex) {
      throw new RuntimeException(ex); 
    }
    return result;
  }

  /**
   *
   * @param obj
   * @return
   */
  public static NamParameter create(JSONObject obj) {
    try {
      String parentKey = obj.getString("parentKey");
      int fcststep = obj.getInt("fcststep");
      String datetimetext = obj.getString("datetime");
      ZoneId zoneId = ZoneId.of(obj.getString("zoneid"));
      LocalDateTime localdatetime = getDateTimeFormatter()
        .parse(datetimetext, LocalDateTime::from);
      ZonedDateTime datetime = ZonedDateTime.of(localdatetime, zoneId);
      ForecastTimeReference d = new ForecastTimeReference(0, fcststep);
      NamParameter result = new NamParameter(parentKey, datetime, d);
      return result;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static DateTimeFormatter getDateTimeFormatter() {
    return new DateTimeFormatterBuilder()
      .appendPattern("yyyyMMddHH")
      .toFormatter();
  }

}
