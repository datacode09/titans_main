package titans.noaa.core;

import java.util.List;
import javax.measure.unit.Unit;

/**
 *
 * @author Ricardo Marquez
 */
public interface InventoryReader {

  public List<NoaaVarClazz> read();
  
  public Unit<?> getUnit(String var);
}
