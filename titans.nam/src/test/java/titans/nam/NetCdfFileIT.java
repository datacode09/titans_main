
package titans.nam;

import java.io.File;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;
import org.junit.Test;
import titans.noaa.netcdf.NetCdfFile;

/**
 *
 * @author Ricardo Marquez
 */
public class NetCdfFileIT {
  
  @Test
  public void test() {
    File file = new File("G:\\tests\\data\\UGRD_01070000.nc");
    NetCdfFile netcdffile = NetCdfFile.test("UGRD_10_HTGL", file);
    Unit<? extends Quantity> units = netcdffile.getUnits();
    System.out.println("units = " + units);
  }
  
}
