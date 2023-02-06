package titans.nam.core;

/**
 *
 * @author Ricardo Marquez
 */
public class NoaaVariable {

  private final String varName;

  /**
   * 
   * @param varName 
   */
  public NoaaVariable(String varName) {
    this.varName = varName;
  }
  
  
  /**
   * 
   * @return 
   */
  public String getGribVarName() {
    return this.varName;
  }
  
}
