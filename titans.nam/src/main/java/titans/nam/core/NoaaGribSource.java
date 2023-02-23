package titans.nam.core;

import titans.nam.grib.GribFile;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class NoaaGribSource {
  
  /**
   * 
   * @param gribFile 
   */
  protected abstract void download(GribFile gribFile); 
  
}
