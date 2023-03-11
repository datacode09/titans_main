package titan.goes;

import java.util.List;
import org.junit.Test;
import titans.goes.GoesInventoryReader;
import titans.noaa.core.NoaaVarClazz;

/**
 *
 * @author Ricardo Marquez
 */
public class GeosInventoryReaderIT {
  
  @Test
  public void test() {
    GoesInventoryReader reader = new GoesInventoryReader();
    List<NoaaVarClazz> list = reader.read();
    System.out.println("list = " + list.size());
    list.forEach(i->System.out.println(i.getVarName()));
    
  }
}
