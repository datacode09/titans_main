package rm.titansdata.web.projects;

import org.junit.Test;
import org.locationtech.jts.geom.Point;
import rm.titansdata.web.RequestParser;

/**
 *
 * @author Ricardo Marquez
 */
public class FastTestIT {
    
  /**
   * 
   */
  @Test
  public void test() {
    Point p = RequestParser.getPointFromJsonText("{x: 0, y: 0}", 3857);
    System.out.println("p = " + p);
  }
}
