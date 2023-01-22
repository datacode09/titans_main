package rm.titansdata.properties;

import javax.measure.Measure;
import javax.measure.quantity.Length;

/**
 *
 * @author Ricardo Marquez
 */
public final class Dimension {
  public final int pixels;
  public final Measure<Length> length;

  public Dimension(Measure<Length> length, int pixels) {
    if (pixels <= 0) {
      throw new RuntimeException("Invalid pixels size"); 
    }
    this.pixels = pixels;
    this.length = length;
  }
  
  @Override
  public String toString() {
    return "Dimension{" + "pixels=" + pixels + ", length=" + length + '}';
  }
  
}
