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

  public NoaaDateClazz(ZonedDateTime datetime) {
    this.datetime = datetime;
  }

  @Override
  public String getKey() {
    return "NoaaDateClazz";
  }

  /**
   *
   * @return
   */
  @Override
  public String toJson() {
    DateTimeFormatter formatter = getFormatter();
    String formatteddatetime = this.datetime.format(formatter);
    return "{\"key\": \"" + this.getKey() + "\", \"datetime\":\"" + formatteddatetime + "\"}";
  }

  /**
   * 
   * @return 
   */
  private static DateTimeFormatter getFormatter() {
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
   * @param obj
   * @return 
   */
  public static NoaaDateClazz parse(JSONObject obj) {
    try {
      String text = obj.getString("datetime");
      ZonedDateTime datetime = ZonedDateTime.of(LocalDateTime.parse(text, getFormatter()), ZoneId.of("UTC"));
      NoaaDateClazz result = new NoaaDateClazz(datetime);
      return result;
    } catch (JSONException ex) {
      throw new RuntimeException(ex); 
    }
  }
}
