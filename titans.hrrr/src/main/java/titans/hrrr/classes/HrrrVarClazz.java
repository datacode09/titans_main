package titans.hrrr.classes;

import rm.titansdata.plugin.Clazz;

/**
 *
 * @author Ricardo Marquez
 */
public class HrrrVarClazz implements Clazz {

  private final String key;
  private final String varName;
  
  public HrrrVarClazz(String key, String varName) {
    this.key = key;
    this.varName = varName;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public String getKey() {
    return key;
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
  public String toString() {
    return "HrrrVarClazz{" + "key=" + key + ", varName=" + varName + '}';
  }
}
