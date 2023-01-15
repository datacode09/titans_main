package titans.nam;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import org.json.JSONObject;
import rm.titansdata.Parameter;
import titans.nam.grib.ForecastTimeReference;

/**
 *
 * @author Ricardo Marquez
 */
public class NamParameter implements Parameter {

  public ZonedDateTime datetime;
  public int fcststep;
  
  /**
   * 
   * @param datetime
   * @param d 
   */
  public NamParameter(ZonedDateTime datetime, ForecastTimeReference d) {
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
      result.put("fcststep", this.fcststep); 
      result.put("datetime", this.datetime.format(getDateTimeFormatter()));
      result.put("zoneid", this.datetime.getZone().getId());
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
      int fcststep = obj.getInt("fcststep");
      String datetimetext = obj.getString("datetime");
      ZoneId zoneId = ZoneId.of(obj.getString("zoneid"));
      LocalDateTime localdatetime = getDateTimeFormatter()
        .parse(datetimetext, LocalDateTime::from);
      ZonedDateTime datetime = ZonedDateTime.of(localdatetime, zoneId);
      ForecastTimeReference d = new ForecastTimeReference(0, fcststep);
      NamParameter result = new NamParameter(datetime, d);
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
