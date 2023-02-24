package titans.noaa.core;

import javax.measure.unit.Unit;

/**
 *
 * @author Ricardo Marquez
 */
public class NoaaVariable {

  
  private final String varName;
  private final Unit<?> unit;

  /**
   * 
   * @param varName 
   */
  public NoaaVariable(String varName, Unit<?> unit) {
    this.varName = varName;
    this.unit = unit;
  }
  
  /**
   * 
   * @return 
   */
  public Unit<?> getUnit() {
    return unit;
  }
  
  
  
  /**
   * 
   * @return 
   */
  public String getGribVarName() {
    return this.varName;
  }
  
}
