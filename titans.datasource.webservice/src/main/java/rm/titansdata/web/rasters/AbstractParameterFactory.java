package rm.titansdata.web.rasters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
   * @param key
   * @param arr
   * @return
   */
  public List<Clazz> getClasse(String key, JSONArray arr) {
    ParameterFactory paramFactory = this.factories.get(key);
    List<Clazz> result = paramFactory.getClasses(arr);
    return result;
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
   * @param string
   * @return
   */
  public List<Parameter> get(JSONArray arr) {
    List<Parameter> result = new ArrayList<>();
    for (int i = 0; i < arr.length(); i++) {
      try {
        String s = arr.getString(i);
        JSONObject jsonParam = new JSONObject(s);
        ParameterFactory factory = this.getFactory(jsonParam);
        Parameter param = factory.create(jsonParam);
        result.add(param);
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
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
  List<Parameter> getParameters(String key, List<Clazz> classes) {
    if (!this.factories.containsKey(key)) {
      throw new RuntimeException(String.format("Invalid key '%s'", key));
    }
    ParameterFactory factory = this.factories.get(key);
    List<Parameter> result = factory.getParameters(classes.toArray(new Clazz[0]));
    return result;
  }
  
  /**
   * 
   * @param sourceTitle
   * @return 
   */
  Map<ClassType, List<Clazz>> getClasses(String sourceTitle) {
    ParameterFactory f = this.factories.get(sourceTitle);
    Map<ClassType, List<Clazz>> result = new HashMap<>();
    for (ClassType classtype : f.getClassTypes()) {
      result.put(classtype, f.getClasses(classtype));    
    }
    return result;
  }

}
