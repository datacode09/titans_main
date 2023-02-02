package rm.titansdata.web.rasters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import rm.titansdata.plugin.ParameterFactory;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class AbstractParameterFactory {
    
  private final Map<String, ParameterFactory> factories = new HashMap<>();
  
  /**
   * 
   * @param key
   * @param factory 
   */
  public void add(String key, ParameterFactory factory) {
    this.factories.put(key, factory); 
  }
  
  /**
   * 
   * @param string
   * @return 
   */
  public Parameter get(JSONObject parameter) {
    ParameterFactory factory = this.getFactory(parameter);
    Parameter result = factory.create(parameter);
    return result;
  }
  
  /**
   * 
   * @param obj
   * @return 
   */
  private ParameterFactory getFactory(JSONObject obj) {
    String key = this.getParentKey(obj);
    if (!this.factories.containsKey(key)) {
      throw new RuntimeException( //
        String.format("Factory for key '%s' doesn't exist", key)); 
    }
    ParameterFactory impl = this.factories.get(key);
    return impl;
  }
  
  /**
   * 
   * @param obj
   * @return
   * @throws RuntimeException 
   */
  private String getParentKey(JSONObject obj) {
    if (!obj.has("parentKey")) {
      throw new RuntimeException("JSON serialized parameter doesn't contain parentKey");
    }
    String key;   
    try {
      key = obj.getString("parentKey");
    } catch (JSONException ex) {
      throw new RuntimeException(ex);
    }
    return key;
  }
  
  /**
   * 
   * @param key
   * @return 
   */
  List<Parameter> getParameters(String key) {
    if (!this.factories.containsKey(key)) {
      throw new RuntimeException(String.format("Invalid key '%s'", key)); 
    }
    ParameterFactory factory = this.factories.get(key);
    List<Parameter> result = factory.getParameters();
    return result;
  }
  
}
