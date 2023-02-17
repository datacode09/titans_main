package titans.nam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.beans.property.ListProperty;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import rm.titansdata.plugin.ClassType;
import rm.titansdata.plugin.Clazz;
import rm.titansdata.plugin.ParameterFactory;
import rm.titansdata.plugin.classes.BaseDateClassType;
import rm.titansdata.plugin.classes.ForecastStepClassType;
import rm.titansdata.plugin.classes.ForecastStepClazz;
import rm.titansdata.plugin.classes.ValueClassType;
import titans.nam.classes.NoaaVarClazz;
import titans.nam.core.NamInventoryReader;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class NamParametersFactory implements ParameterFactory {

  public static ClassType BASEDATE_CLASSTYPE = new BaseDateClassType();
  public static ClassType FORECAST_CLASSTYPE = new ForecastStepClassType();
  public static ClassType VALUE_CLASSTYPE = new ValueClassType("NOAA_VAR");
  @Autowired
  @Qualifier("nam.parameters")
  private ListProperty<NoaaParameter> parameters;

  /**
   *
   * @return
   */
  @Override
  public String key() {
    return "North American Model Forecasts";
  }

  /**
   *
   * @return
   */
  @Override
  public List<ClassType> getClassTypes() {
    List<ClassType> result = Arrays.asList( //
      BASEDATE_CLASSTYPE, FORECAST_CLASSTYPE, VALUE_CLASSTYPE);
    return result;
  }

  @Override
  public List<Parameter> getParameters(Clazz... clazzes) {
    String noaaVar = this.getNoaaVar(clazzes);
    List<Parameter> arrayList = new ArrayList<>(this.parameters.getValue());
    arrayList.stream()
      .map(p -> (NoaaParameter) p)
      .map(p -> p.setVar(noaaVar));
    return arrayList;
  }

  
    /**
   *
   * @param clazzes
   * @return
   */
  private String getNoaaVar(Clazz[] clazzes) {
    NoaaVarClazz varClazz = Arrays.stream(clazzes)
      .filter(c -> c instanceof NoaaVarClazz)
      .map(c -> (NoaaVarClazz) c)
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
  public List<Clazz> getClasses(ClassType classtype) {
    List<? extends Clazz> result;
    if (classtype == VALUE_CLASSTYPE) {
      result = new NamInventoryReader().read();
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
  public Parameter create(JSONObject obj) {
    NoaaParameter param;
    if (obj.has("key")) {
      try {
        String key = obj.getString("key");
        param = this.parameters.stream()
          .filter(e -> e.getKey().equals(key))
          .findFirst()
          .orElseGet(() -> NoaaParameter.create(obj));
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
  public List<Clazz> getClasses(JSONArray arr) {
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
            return new NoaaVarClazz(varName);
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        };
        break;
      case "FCST_STEP":
        result = (JSONObject o) -> {  
          try {
            int step = o.getInt("step");
            return new ForecastStepClazz(step);
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
