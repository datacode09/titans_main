package rm.titansdata;

import org.json.JSONObject;

/**
 *
 * @author Ricardo Marquez
 */
public interface Parameter {
    
  /**
   * 
   * @return 
   */
  public JSONObject toJSONObject(); 
  
  /**
   * 
   * @return 
   */
  public String getKey();
  
  
}
