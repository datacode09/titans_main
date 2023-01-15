package rm.titansdata.plugin;

import java.util.List;
import org.json.JSONObject;
import rm.titansdata.Parameter;

/**
 *
 * @author Ricardo Marquez
 */
public interface ParameterFactory {
  
  
  public String key();
  
  
  /**
   * 
   * @param obj
   * @return 
   */
  public Parameter create(JSONObject obj); 

  public List<Parameter> getParameters();
  
}
