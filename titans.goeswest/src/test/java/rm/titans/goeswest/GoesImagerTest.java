package rm.titans.goeswest;

import com.rm.panzoomcanvas.Degrees;
import java.io.File;
import org.junit.Test;

/**
 *
 * @author Ricardo Marquez
 */
public class GoesImagerTest {
  
  private final File file = new File("C:\\Users\\Ricardo Marquez\\Desktop\\"
      + "OR_ABI-L1b-RadC-M6C01_G17_s20223440031177_e20223440033550_c20223440033577.nc"); 
  
  /**
   * 
   */
  @Test
  public void test() {
    GoesImager imager = new GoesImager(file);
    for (int i = 0; i < 100; i++) {
      double value = imager.getValue(new Degrees(37.36, -120.43));
    }
  }
    
  /**
   * 
   */
  @Test
  public void test2() {
    GoesImager imager = new GoesImager(file);
    int num = 10000; 
    Degrees[] points = new Degrees[num]; 
    Degrees point = new Degrees(37.36, -120.43); 
    for (int i = 0; i < num; i++) {
      points[i] = point;
    }
    double[] values = imager.getValue(points);
  }
}
