package titans.nam.core;

import java.util.List;
import javax.measure.unit.Unit;
import titans.nam.classes.NoaaVarClazz;

/**
 *
 * @author Ricardo Marquez
 */
public interface InventoryReader {

  public List<NoaaVarClazz> read();
  
  public Unit<?> getUnit(String var);
}
