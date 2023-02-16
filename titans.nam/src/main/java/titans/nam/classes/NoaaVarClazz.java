package titans.nam.classes;

import rm.titansdata.plugin.Clazz;

/**
 *
 * @author Ricardo Marquez
 */
public class NoaaVarClazz implements Clazz{
  private final String key;
  private final String varName;

  public NoaaVarClazz(String varName) {
    this.key = "NOAA_VAR";
    this.varName = varName;
  }

  public String getVarName() {
    return varName;
  }
  
  
  @Override
  public String getKey() {
    return this.key;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public String toJson() {
    return String.format("{'key':'%s', 'varName', '%s'}", this.getKey(), this.getVarName());
  }
  
}
