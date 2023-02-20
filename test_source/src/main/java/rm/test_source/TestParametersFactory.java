package rm.test_source;

import java.util.ArrayList;
import java.util.List;
import javax.measure.unit.Unit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import rm.titansdata.plugin.ClassType;
import rm.titansdata.plugin.Clazz;
import rm.titansdata.plugin.ParameterFactory;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class TestParametersFactory implements ParameterFactory {

  @Override
  public String key() {
    return "test_source";
  }

  @Override
  public List<ClassType> getClassTypes() {
    return new ArrayList<>();
  }

  @Override
  public List<Clazz> getClasses(ClassType classtype) {
    return new ArrayList<>();
  }
  
  
  /**
   * 
   * @return 
   */
  @Override
  public List<Parameter> getParameters(Clazz... clazzes) {
    List<Parameter> result = new ArrayList<>();
    result.add(new Parameter() {
      @Override
      public JSONObject toJSONObject() {
        try {
          JSONObject jsonObject = new JSONObject();
          jsonObject.put("parentKey", "test_source");
          jsonObject.put("key", "test_source_01");
          return jsonObject;
        } catch (JSONException ex) {
          throw new RuntimeException(ex);
        }
      }

      @Override
      public String getKey() {
        return "test_source_01";
      }

      @Override
      public Unit<?> getUnit() {
        return Unit.ONE;
      }
      
      
    });
    return result;
  }
  
  /**
   * 
   * @param obj
   * @return 
   */
  @Override
  public Parameter create(JSONObject obj) {
    return new Parameter() {
      @Override
      public JSONObject toJSONObject() {
        //To change body of generated methods, choose Tools | Templates.
        throw new UnsupportedOperationException("Not supported yet.");
      }

      @Override
      public String getKey() {
        //To change body of generated methods, choose Tools | Templates.
        throw new UnsupportedOperationException("Not supported yet.");
      }

      @Override
      public Unit<?> getUnit() {
        //To change body of generated methods, choose Tools | Templates.
        throw new UnsupportedOperationException("Not supported yet.");
      }
      
    };
  }

  @Override
  public List<Clazz> getClasses(JSONArray arr) {
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
