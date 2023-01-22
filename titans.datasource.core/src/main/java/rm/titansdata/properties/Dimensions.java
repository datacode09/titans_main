package rm.titansdata.properties;

import java.io.Serializable;
import javax.measure.Measure;
import javax.measure.quantity.Length;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

/**
 *
 * @author Ricardo Marquez
 */
public class Dimensions implements Serializable{ 

  /**
   * 
   * @param bounds
   * @param dx
   * @param dy
   * @return 
   */
  public static Dimensions create(Bounds bounds, Measure<Length> dx, Measure<Length> dy) {
    double pixelsx = ((bounds.getMaxX() - bounds.getMinX())/dx.doubleValue(SI.METRE));
    double pixelsy = ((bounds.getMaxY() - bounds.getMinY())/dy.doubleValue(SI.METRE));
    Dimension dimx = new Dimension(dx, (int) pixelsx);
    Dimension dimy = new Dimension(dy, (int) pixelsy);
    Dimensions result = new Dimensions(dimx, dimy); 
    return result; 
    
  }
  
  public final Dimension x;
  public final Dimension y;

  public Dimensions(Dimension dimensionx, Dimension dimensiony) {
    this.x = dimensionx;
    this.y = dimensiony;
  }
    
  /**
   * 
   * @param unit
   * @return 
   */
  public double getDeltaX(Unit<Length> unit) {
    return this.x.length.doubleValue(unit); 
  }
  
  /**
   * 
   * @param unit
   * @return 
   */
  public double getDeltaY(Unit<Length> unit) {
    return this.x.length.doubleValue(unit); 
  }
  
  
  /**
   * 
   * @return 
   */
  public int numPixels() {
    int result = this.x.pixels*this.y.pixels;
    return result;
  }
  
  
}
