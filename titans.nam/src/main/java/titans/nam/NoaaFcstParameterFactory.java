package titans.nam;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.measure.unit.Unit;
import org.json.JSONArray;
import org.json.JSONObject;
import rm.titansdata.Parameter;
import rm.titansdata.plugin.ClassType;
import rm.titansdata.plugin.Clazz;
import rm.titansdata.plugin.ParameterFactory;
import rm.titansdata.plugin.classes.BaseDateClassType;
import rm.titansdata.plugin.classes.ForecastStepClassType;
import rm.titansdata.plugin.classes.ForecastStepClazz;
import rm.titansdata.plugin.classes.ValueClassType;
import rm.titansdata.units.UnitsUtils;
import titans.noaa.core.FcstDateRange;
import titans.noaa.core.InventoryReader;
import titans.noaa.core.NoaaDateClazz;
import titans.noaa.core.NoaaVarClazz;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class NoaaFcstParameterFactory implements ParameterFactory {

  public static ClassType BASEDATE_CLASSTYPE = new BaseDateClassType();
  public static ClassType FORECAST_CLASSTYPE = new ForecastStepClassType();
  public static ClassType VALUE_CLASSTYPE = new ValueClassType("NOAA_VAR");

  public NoaaFcstParameterFactory() {
  }

  /**
   *
   * @return
   */
  @Override
  public final List<ClassType> getClassTypes() {
    List<ClassType> result = Arrays.asList( //
      BASEDATE_CLASSTYPE, FORECAST_CLASSTYPE, VALUE_CLASSTYPE);
    return result;
  }

  /**
   *
   * @param clazzes
   * @return
   */
  @Override
  public final List<Parameter> getParameters(Clazz... clazzes) {
    String noaaVar = this.getNoaaVar(clazzes);
    Unit<?> unit = this.getUnit(noaaVar);

    NoaaDateClazz dateclazz = this.getDateClazz(clazzes);
    ZonedDateTime zonedDateTime = dateclazz.getZoneDateTime();
    ZonedDateTime zonedDateTime2 = dateclazz.getZoneDateTime2();
    ForecastStepClazz fcstclazz = this.getForecastStepClazz(clazzes);
    int fcststep = fcstclazz == null ? -1 : fcstclazz.step;
    FcstDateRange range = new FcstDateRange(zonedDateTime, zonedDateTime2, fcststep);
    List<Parameter> arrayList = this.getParameters(range);
    List<Parameter> result = arrayList.stream()
      .map(p -> (NoaaParameter) p)
      .map(p -> p.setVar(noaaVar, unit))
      .collect(Collectors.toList());
    return result;
  }

  /**
   *
   * @param noaaVar
   * @return
   */
  private Unit<?> getUnit(String noaaVar) {
    InventoryReader reader = this.getInventoryReader();
    Unit<?> unit = reader.getUnit(noaaVar);
    return unit;
  }

  /**
   *
   * @return
   */
  protected abstract InventoryReader getInventoryReader();

  /**
   *
   * @param clazzes
   * @return
   */
  private String getNoaaVar(Clazz[] clazzes) {
    List<NoaaVarClazz> varClazzes = Arrays.stream(clazzes)
      .filter(c -> c instanceof NoaaVarClazz)
      .map(c -> (NoaaVarClazz) c)
      .collect(Collectors.toList());
    String result = this.onGetVarName(varClazzes);
    return result;
  }

  /**
   *
   * @param varClazzes
   * @return
   */
  private String onGetVarName(List<NoaaVarClazz> varClazzes) {
    NoaaVarClazz varClazz = varClazzes.stream()
      .findFirst()
      .orElseThrow(RuntimeException::new);
    String result = varClazz.getVarName();
    return result;
  }

  /**
   *
   * @param classtype
   * @return
   */
  @Override
  public final List<Clazz> getClasses(ClassType classtype) {
    List<? extends Clazz> result;
    if (classtype == VALUE_CLASSTYPE) {
      InventoryReader reader = this.getInventoryReader();
      result = reader.read();
    } else if (classtype == FORECAST_CLASSTYPE) {
      result = IntStream.range(0, 50) //
        .mapToObj(i -> new ForecastStepClazz(i)) //
        .collect(Collectors.toList());
    } else {
      result = new ArrayList<>();
    }
    return (List<Clazz>) result;
  }

  /**
   *
   * @param obj
   * @return
   */
  @Override
  public final Parameter create(JSONObject obj) {
    NoaaParameter param;
    if (obj.has("key")) {
      try {
        String key = obj.getString("key");
        String var = obj.getString("noaaVar");
        Unit<?> unit = UnitsUtils.valueOf(obj.getString("unit"));
        ZonedDateTime datetime = ZonedDateTime.of( //
          LocalDateTime.parse(obj.getString("datetime"), NoaaDateClazz.getFormatter()), // 
          ZoneId.of("UTC"));
        int step = obj.getInt("fcststep");
        param = new NoaaParameter(key, datetime, step, var, unit);
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    } else {
      param = NoaaParameter.create(obj);
    }
    return param;
  }

  /**
   *
   * @param arr
   * @return
   */
  @Override
  public final List<Clazz> getClasses(JSONArray arr) {
    try {
      List<Clazz> result = new ArrayList<>();
      for (int i = 0; i < arr.length(); i++) {
        JSONObject o;
        Object value = arr.get(i);
        if (value instanceof JSONObject) {
          o = arr.getJSONObject(i);
        } else if (value instanceof String) {
          o = ((String) value).isEmpty() ? null : new JSONObject((String) value);
        } else {
          throw new RuntimeException(String.format("Invalid type '%s'", value));
        }
        if (o != null) {
          String key = o.getString("key");
          Clazz e = this.getParser(key).parse(o);
          result.add(e);
        }
      }
      return result;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   *
   * @param key
   * @return
   */
  private Parser getParser(String key) {
    Parser result;
    switch (key) {
      case "NOAA_VAR":
        result = (JSONObject o) -> {
          try {
            String varName = o.getString("varName");
            return new NoaaVarClazz(varName, this.getUnit(varName));
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        };
        break;
      case "FCST_STEP":
        result = (JSONObject o) -> {
          try {
            return ForecastStepClazz.parse(o);
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        };
        break;
      case "NoaaDateClazz":
        result = (JSONObject o) -> {
          try {
            return NoaaDateClazz.parse(o);
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        };
        break;
      default:
        throw new RuntimeException( //
          String.format("Invalid key : '%s'", key));
    }
    return result;
  }

  /**
   *
   * @param clazzes
   * @return
   */
  private ForecastStepClazz getForecastStepClazz(Clazz[] clazzes) {
    ForecastStepClazz result = Stream.of(clazzes)
      .filter(c -> c instanceof ForecastStepClazz)
      .map(c -> (ForecastStepClazz) c)
      .findFirst()
      .orElse(null);
    return result;
  }

  /**
   *
   * @param clazzes
   * @return
   */
  private NoaaDateClazz getDateClazz(Clazz[] clazzes) {
    NoaaDateClazz result = Stream.of(clazzes)
      .filter(c -> c instanceof NoaaDateClazz)
      .map(c -> (NoaaDateClazz) c)
      .findFirst()
      .orElse(null);
    return result;
  }

  protected abstract List<Parameter> getParameters(FcstDateRange range);

  /**
   *
   */
  private static interface Parser {

    /**
     *
     * @param o
     * @return
     */
    Clazz parse(JSONObject o);
  }
}
