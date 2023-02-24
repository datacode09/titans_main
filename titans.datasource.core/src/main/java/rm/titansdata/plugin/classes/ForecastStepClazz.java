package rm.titansdata.plugin.classes;

import org.json.JSONException;
import org.json.JSONObject;
import rm.titansdata.plugin.Clazz;

/**
 *
 * @author Ricardo Marquez
 */
public final class ForecastStepClazz implements Clazz {


  public final int step;

  public ForecastStepClazz(int step) {
    this.step = step;
  }

  /**
   *
   * @return
   */
  @Override
  public String getKey() {
    return "FCST_STEP";
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {
    return "{key = " + this.getKey() +  ", step=" + step + '}';
  }
  
 
  
  /**
   * 
   * @return 
   */
  @Override
  public String toJson() {
    JSONObject obj = toJSONObj();
    return obj.toString();
  }
    /**
   * 
   * @return
   * @throws JSONException 
   */
  private JSONObject toJSONObj() {
    try {
      JSONObject obj = new JSONObject();
      obj.append("key", this.getKey());
      obj.append("step", this.step);
      return obj;
    } catch (JSONException ex) {
      throw new RuntimeException(ex); 
    }
  }
  
  
   /**
   * 
   * @param obj
   * @return 
   */
  public static ForecastStepClazz parse(JSONObject obj) {
    try {
      String key = obj.getString("key");
      if (!key.equals("FCST_STEP")) {
        throw new RuntimeException();
      }
      int step = obj.getInt("step");
      ForecastStepClazz result = new ForecastStepClazz(step); 
      return result;
    } catch (JSONException ex) {
      throw new RuntimeException(ex); 
    }
  }
}
