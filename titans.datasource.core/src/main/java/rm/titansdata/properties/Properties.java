package rm.titansdata.properties;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.GeometryFactory;
import javax.measure.Measure;
import javax.measure.quantity.Length;

/**
 *
 * @author Ricardo Marquez
 */
public class Properties {


  private final Bounds bounds;
  private final Dimensions dimensions;

  public Properties(Bounds bounds, Measure<Length> lenthx,  Measure<Length> lenthy) {
    this.bounds = bounds;
    this.dimensions = Dimensions.create(bounds, lenthx, lenthy);
  }

  public Bounds getBounds() {
    return bounds;
  }

  public Dimensions getDimensions() {
    return dimensions;
  }
  
  @Override
  public String toString() {
    return "{" + "bounds=" + bounds + ", dimensions=" + dimensions + '}';
  }
  
  /**
   * 
   * @param envelope
   * @param lenx
   * @param leny
   * @return 
   */
  public static Properties create(GeometryFactory gf, Envelope envelope, 
    Measure<Length> lenx, Measure<Length> leny) {
    Bounds b  = Bounds.fromEnvelope(gf, envelope);
    Properties result = new Properties(b, lenx, leny); 
    return result;
  }
  
}
