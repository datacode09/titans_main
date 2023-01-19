package rm.titansdata.plugin;

import java.util.List;
import org.json.JSONObject;
import rm.titansdata.Parameter;

/**
 *
 * @author Ricardo Marquez
 */
public interface ParameterFactory {
  
  
  /**
   * 
   * @return 
   */
  public String key();
  
  
  /**
   * 
   * @param obj
   * @return 
   */
  public Parameter create(JSONObject obj); 
  
  /**
   * 
   * @return 
   */
  public List<Parameter> getParameters();
  
}
