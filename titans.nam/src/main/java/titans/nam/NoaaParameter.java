package titans.nam;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.measure.unit.Unit;
import org.json.JSONArray;
import org.json.JSONObject;
import rm.titansdata.Parameter;
import rm.titansdata.units.UnitsUtils;
import titans.noaa.core.NoaaDateClazz;
import titans.noaa.grib.ForecastTimeReference;

/**
 *
 * @author Ricardo Marquez
 */
public class NoaaParameter implements Parameter {

  private final String parentKey;
  public final ZonedDateTime datetime;
  public final int fcststep;
  public final String noaaVar;
  private Unit<?> unit;

  /**
   *
   * @param datetime
   * @param d
   */
  public NoaaParameter(String parentKey, ZonedDateTime datetime, //
    ForecastTimeReference d, String namVar, Unit<?> unit) {
    this(parentKey, datetime.plusHours(d.refhour), d.fcsthourAhead, namVar, unit);
  }
  
  /**
   * 
   * @param parentKey
   * @param datetime
   * @param fcststep
   * @param namVar 
   */
  public NoaaParameter(String parentKey, 
    ZonedDateTime datetime, 
    int fcststep, 
    String namVar, Unit<?> unit) {
    this.parentKey = parentKey;
    this.datetime = datetime;
    this.fcststep = fcststep;
    this.noaaVar = namVar;
    this.unit = unit;
  }
  
  
  /**
   * 
   * @param var
   * @return 
   */
  public NoaaParameter setVar(String var, Unit<?> unit) {
    NoaaParameter result = new NoaaParameter(parentKey, datetime, fcststep, var, unit);
    return result;
  }

  @Override
  public Unit<?> getUnit() {
    return this.unit;
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
      String key = this.getKey();
      result.put("key", key);
      result.put("parentKey", this.parentKey);
      result.put("fcststep", this.fcststep);
      result.put("noaaVar", this.noaaVar);
      result.put("unit", this.unit.toString());
      result.put("datetime", this.datetime.format(getDateTimeFormatter()));
      result.put("zoneid", this.datetime.getZone().getId());
      result.put("validtime", this.getValidTime());
      JSONArray view = new JSONArray();
      view.put(0, new JSONObject() {
        {
          put("type", "label");
          put("name", "key");
          put("value", key);
        }
      });
      view.put(1, new JSONObject() {
        {
          put("type", "label");
          put("name", "Reference Date");
          put("value", datetime.toString());
        }
      });
      view.put(2, new JSONObject() {
        {
          put("type", "label");
          put("name", "Forecast Step");
          put("value", fcststep);
        }
      });
      result.put("view", view.toString());
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    return result;
  }

  /**
   *
   * @return
   */
  @Override
  public String getKey() {
    String format = this.datetime.format(getDateTimeFormatter());
    String key = format + "-" + this.fcststep;
    return key;
  }

  /**
   *
   * @param obj
   * @return
   */
  public static NoaaParameter create(JSONObject obj) {
    try {
      String parentKey = obj.getString("parentKey");
      int fcststep = obj.getInt("fcststep");
      String datetimetext = obj.getString("datetime");
      ZoneId zoneId = ZoneId.of(obj.getString("zoneid"));
      String var = obj.getString("noaaVar");
      Unit<?> unit = UnitsUtils.valueOf(obj.getString("unit"));
      LocalDateTime localdatetime = getDateTimeFormatter()
        .parse(datetimetext, LocalDateTime::from);
      ZonedDateTime datetime = ZonedDateTime.of(localdatetime, zoneId);
      ForecastTimeReference d = new ForecastTimeReference(0, fcststep);
      NoaaParameter result = new NoaaParameter(parentKey, datetime, d, var, unit);
      return result;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   *
   * @return
   */
  private static DateTimeFormatter getDateTimeFormatter() {
    DateTimeFormatter result = NoaaDateClazz.getFormatter();
    return result;
  }

  /**
   *
   * @return
   */
  private String getValidTime() {
    ZonedDateTime plusHours = this.datetime.plusHours(fcststep) //
      .toOffsetDateTime()//
      .atZoneSameInstant(ZoneId.of("UTC"));
    String result = plusHours.format(getDateTimeFormatter());
    return result;
  }

}
