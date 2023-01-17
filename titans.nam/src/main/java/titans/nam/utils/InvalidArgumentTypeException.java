package titans.nam.utils;

import titans.nam.NamParameter;

/**
 *
 * @author Ricardo Marquez
 */
public class InvalidArgumentTypeException extends RuntimeException {
  
  /**
   * 
   * @param p
   * @param aClass 
   */
  public InvalidArgumentTypeException(Object p, Class<NamParameter> aClass) {
    super(String.format("The object '%s' is not a type of %s", p, aClass)); 
  }
  
}
