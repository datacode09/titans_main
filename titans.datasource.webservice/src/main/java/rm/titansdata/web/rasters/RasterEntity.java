package rm.titansdata.web.rasters;

import com.vividsolutions.jts.geom.Point;
import javax.measure.Measure;
import javax.measure.unit.SI;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimension;
import rm.titansdata.properties.Dimensions;

/**
 *
 * @author Ricardo Marquez
 */
public class RasterEntity {

  public final long rasterId;
  public final long rasterTypeId;
  public final String sourceTitle;
  public final String sourceDescription;
  public final double dx;
  public final double dy;
  public final Point lowerleft;
  public final Point upperright;

  private RasterEntity(long rasterId, //
    long rasterTypeId, String sourceTitle, String sourceDescription, //
    double dx, double dy, Point lowerleft, Point upperright) {
    this.rasterId = rasterId;
    this.rasterTypeId = rasterTypeId;
    this.sourceTitle = sourceTitle;
    this.sourceDescription = sourceDescription;
    this.dx = dx;
    this.dy = dy;
    this.lowerleft = lowerleft;
    this.upperright = upperright;
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {
    return "{" + "rasterId=" + rasterId + '}';
  }
  
  /**
   * 
   * @return 
   */
  public Bounds getBounds() {
    Bounds result = new Bounds(lowerleft, upperright); 
    return result; 
  }
  
  /**
   * 
   * @return 
   */
  public Dimensions getDimensions() {
    int numpixelsx = Double.valueOf((upperright.getX() - lowerleft.getX())/this.dx).intValue();
    int numpixelsy = Double.valueOf( (upperright.getY() - lowerleft.getY())/this.dy).intValue();
    Dimension dimensionx = new Dimension(Measure.valueOf(dx, SI.METRE), numpixelsx);
    Dimension dimensiony = new Dimension(Measure.valueOf(dy, SI.METRE), numpixelsy);
    Dimensions result = new Dimensions(dimensionx, dimensiony); 
    return result; 
  }

  public static class Builder {

    private long rasterId;
    private long rasterTypeId;
    private String sourceTitle;
    private String sourceDescription;
    private double dx;
    private double dy;
    private Point lowerleft;
    private Point upperright;

    public Builder setRasterId(long rasterId) {
      this.rasterId = rasterId;
      return this;
    }

    public Builder setRasterTypeId(long rasterTypeId) {
      this.rasterTypeId = rasterTypeId;
      return this;
    }

    public Builder setSourceTitle(String sourceTitle) {
      this.sourceTitle = sourceTitle;
      return this;
    }

    public Builder setSourceDescription(String sourceDescription) {
      this.sourceDescription = sourceDescription;
      return this;
    }

    public Builder setDx(double dx) {
      this.dx = dx;
      return this;
    }

    public Builder setDy(double dy) {
      this.dy = dy;
      return this;
    }

    public Builder setLowerleft(Point lowerleft) {
      this.lowerleft = lowerleft;
      return this;
    }

    public Builder setUpperright(Point upperright) {
      this.upperright = upperright;
      return this;
    }

    public RasterEntity build() {
      return new RasterEntity(rasterId, rasterTypeId, sourceTitle, sourceDescription, dx, dy, lowerleft, upperright);
    }
  }
}
