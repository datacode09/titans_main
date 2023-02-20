package rm.titansdata;

import javax.measure.unit.Unit;
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
  
  
  /**
   * 
   */
  public Unit<?> getUnit();
}
