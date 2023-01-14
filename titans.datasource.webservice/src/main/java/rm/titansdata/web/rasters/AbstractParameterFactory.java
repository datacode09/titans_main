package rm.titansdata.web.rasters;

import java.util.HashMap;
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
  public Parameter get(JSONObject obj) {
    ParameterFactory factory = this.getFactory(obj);
    Parameter result = factory.create(obj);
    return result;
  }

  private ParameterFactory getFactory(JSONObject obj) {
    String key = this.getKey(obj);
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
  private String getKey(JSONObject obj) {
    if (!obj.has("key")) {
      throw new RuntimeException("JSON serialized parameter doesn't contain key");
    }
    String key;
    try {
      key = obj.getString("key");
    } catch (JSONException ex) {
      throw new RuntimeException(ex);
    }
    return key;
  }
  
}
