package titans.mrms;

import java.util.List;
import org.junit.Test;
import titans.noaa.core.NoaaVarClazz;

/**
 *
 * @author Ricardo Marquez
 */
public class InventoryReaderIT {
  
  /**
   * 
   */
  @Test
  public void test() {
    MrmsInventoryReader reader = new MrmsInventoryReader();
    List<NoaaVarClazz> a = reader.read();
    System.out.println("a = " + a.size());
  }
}
