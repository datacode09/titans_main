package titans.noaa.core;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import org.json.JSONException;
import org.json.JSONObject;
import rm.titansdata.plugin.Clazz;

/**
 *
 * @author Ricardo Marquez
 */
public class NoaaDateClazz implements Clazz {

  public static NoaaDateClazz TODAY = new NoaaDateClazz(ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS));
  public static NoaaDateClazz YESTERDAY = new NoaaDateClazz(ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS));

  private final ZonedDateTime datetime;
  private ZonedDateTime datetime2;

  /**
   *
   * @param datetime
   */
  public NoaaDateClazz(ZonedDateTime datetime) {
    this(datetime, null);
  }

  /**
   *
   * @param datetime
   * @param datetime2
   */
  public NoaaDateClazz(ZonedDateTime datetime, ZonedDateTime datetime2) {
    this.datetime = datetime;
    this.datetime2 = datetime2;
  }

  @Override
  public String getKey() {
    return "NoaaDateClazz";
  }

  @Override
  public String toString() {
    return "{" + "datetime=" + datetime + ", datetime2=" + datetime2 + '}';
  }

  /**
   *
   * @return
   */
  @Override
  public String toJson() {
    DateTimeFormatter formatter = getFormatter();
    String formatteddatetime = this.datetime.format(formatter);
    if (this.datetime2 == null) {
      return "{\"key\": \"" + this.getKey() + "\", \"datetime\":\"" + formatteddatetime + "\"}";
    } else {
      String formatteddatetime2 = this.datetime2.format(formatter);
      return "{\"key\": \"" + this.getKey() + "\","
        + " \"datetime\":\"" + formatteddatetime + "\","
        + " \"datetime2\":\"" + formatteddatetime2 + "\""
        + "}";
    }
  }

  /**
   *
   * @return
   */
  public static DateTimeFormatter getFormatter() {
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
      .appendPattern("yyyyMMddHHmm")
      .toFormatter();
    return formatter;
  }

  /**
   *
   * @return
   */
  public ZonedDateTime getZoneDateTime() {
    return this.datetime;
  }

  /**
   *
   * @return
   */
  public ZonedDateTime getZoneDateTime2() {
    return this.datetime2;
  }

  /**
   *
   * @param obj
   * @return
   */
  public static NoaaDateClazz parse(JSONObject obj) {
    try {
      ZonedDateTime datetime = parseDateTime(obj);
      ZonedDateTime datetime2 = parseDateTime2(obj);
      NoaaDateClazz result = new NoaaDateClazz(datetime, datetime2);
      return result;
    } catch (JSONException ex) {
      throw new RuntimeException(ex);
    }
  }

  private static ZonedDateTime parseDateTime(JSONObject obj) throws JSONException {
    String text = obj.getString("datetime");
    ZonedDateTime datetime;
    if (text.equals("today")) {
      datetime = ZonedDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.DAYS);
    } else {
      datetime = ZonedDateTime.of(LocalDateTime.parse(text, getFormatter()), ZoneId.of("UTC"));
    }
    return datetime;
  }

  private static ZonedDateTime parseDateTime2(JSONObject obj) throws JSONException {
    String text;
    if (obj.has("datetime2") 
      && !(text = obj.getString("datetime2")).isEmpty() 
      && !text.equals("[]")) {
      ZonedDateTime datetime;
      if (text.equals("today")) {
        datetime = ZonedDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.DAYS);
      } else {
        datetime = ZonedDateTime.of(LocalDateTime.parse(text, getFormatter()), ZoneId.of("UTC"));
      }
      return datetime;
    } else {
      return null;
    }
    
  }
}
