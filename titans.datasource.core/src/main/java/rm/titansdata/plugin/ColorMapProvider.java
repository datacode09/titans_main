package rm.titansdata.plugin;

import rm.titansdata.Parameter;
import rm.titansdata.colormap.ColorMap;

/**
 *
 * @author Ricardo Marquez
 */
public interface ColorMapProvider {
  
  public ColorMap getColorMap(Parameter param); 
}
