package titan.goes;

import java.util.List;
import org.junit.Test;
import titans.goes.Goes18InventoryReader;
import titans.noaa.core.NoaaVarClazz;

/**
 *
 * @author Ricardo Marquez
 */
public class GeosInventoryReaderIT {
  
  @Test
  public void test() {
    Goes18InventoryReader reader = new Goes18InventoryReader();
    List<NoaaVarClazz> list = reader.read();
    System.out.println("list = " + list.size());
  }
}
