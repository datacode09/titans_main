package rm.titansdata.plugin.classes;

import rm.titansdata.plugin.ClassType;

/**
 *
 * @author Ricardo Marquez
 */
public class BaseClassType implements ClassType{

  private final String name;

  public BaseClassType(String name) {
    this.name = name;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public final String name() {
    return this.name;
  }
  
}
