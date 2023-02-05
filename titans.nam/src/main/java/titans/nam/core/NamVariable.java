package titans.nam.core;

/**
 *
 * @author Ricardo Marquez
 */
public class NamVariable {

  private final String varName;

  /**
   * 
   * @param varName 
   */
  public NamVariable(String varName) {
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
