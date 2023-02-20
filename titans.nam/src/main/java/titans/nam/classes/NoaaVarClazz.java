package titans.nam.classes;

import java.util.Objects;
import javax.measure.unit.Unit;
import org.json.JSONException;
import org.json.JSONObject;
import rm.titansdata.plugin.Clazz;

/**
 *
 * @author Ricardo Marquez
 */
public class NoaaVarClazz implements Clazz{
  private final String key;
  private final String varName;
  private final Unit<?> unit;

  public NoaaVarClazz(String varName, Unit<?> unit) {
    this.key = "NOAA_VAR";
    this.varName = varName;
    this.unit = unit;
  }
  
  /**
   * 
   * @return 
   */
  public String getVarName() {
    return varName;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public String getKey() {
    return this.key;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    int hash = 5;
    hash = 53 * hash + Objects.hashCode(this.key);
    hash = 53 * hash + Objects.hashCode(this.varName);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final NoaaVarClazz other = (NoaaVarClazz) obj;
    if (!Objects.equals(this.key, other.key)) {
      return false;
    }
    if (!Objects.equals(this.varName, other.varName)) {
      return false;
    }
    return true;
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
      obj.append("varName", this.getVarName());
      obj.append("unit", this.unit);
      return obj;
    } catch (JSONException ex) {
      throw new RuntimeException(ex); 
    }
  }

  /**
   * 
   * @return 
   */
  public Unit<?> getUnit() {
    return this.unit;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public String toString() {
    return "{" + "key=" + key + ", varName=" + varName + ", unit=" + unit + '}';
  }
  
}
