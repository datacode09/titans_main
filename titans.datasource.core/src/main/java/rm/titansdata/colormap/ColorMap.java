package rm.titansdata.colormap;

import javafx.scene.paint.Color;
import org.apache.commons.lang3.Range;

/**
 *
 * @author Ricardo Marquez
 */
public class ColorMap {

  public static ColorMap parse(String string) {
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }

  private final double xmin;
  private final double xmax;
  private final Color colormin;
  private final Color colormax;
  
  private ColorMap(double xmin, double xmax, String colormin, String colormax) {
    this.xmin = xmin;
    this.xmax = xmax;
    this.colormin = Color.web(colormin);
    this.colormax = Color.web(colormax);
  }

  /**
   *
   * @param x
   * @return
   */
  public String getColorHex(double x) {
    Range<Double> range = Range.between(this.xmin, this.xmax);
    double r = interpolate(range, colormin.getRed(), colormax.getRed(), x);
    double g = interpolate(range, colormin.getGreen(), colormax.getGreen(), x);
    double b = interpolate(range, colormin.getBlue(), colormax.getBlue(), x);
    Color colorResult = Color.color(r, g, b);
    String result = this.toHexString(colorResult);
    return result;
  }
  
  /**
   * 
   * @param xrange
   * @param y1
   * @param y2
   * @param x
   * @return 
   */
  private double interpolate(Range<Double> xrange, double y1, double y2, double x) {
    double f = (x - xrange.getMinimum()) / (xrange.getMaximum()- xrange.getMinimum());
    double result = (1 - f) * y1 + f * y2;
    return result;
  }
  
  /**
   * 
   * @param val
   * @return 
   */
  private String format(double val) {
    String in = Integer.toHexString((int) Math.round(val * 255));
    return in.length() == 1 ? "0" + in : in;
  }

  /**
   *
   * @param value
   * @return
   */
  private String toHexString(Color value) {
    String result = "#" + (format(value.getRed())
      + format(value.getGreen())
      + format(value.getBlue()))
      .toLowerCase();
    return result;
  }
  
  private int interpolateColor255(double value, double colorMin, double colorMax) {
    double fraction = (value - xmin)/(xmax - xmin); 
    double colorvalue = colorMin + fraction*(colorMax - colorMin);
    int result = new Double(255*colorvalue).intValue();
    return result;
  }
  
  public int getRed255(double value) {
    double colorMin = this.colormin.getRed();
    double colorMax = this.colormax.getRed();
    int result = this.interpolateColor255(value, colorMin, colorMax); 
    return result;
  }

  public int getGreen255(double value) {
    double colorMin = colormin.getGreen();
    double colorMax = colormax.getGreen();
    int result = this.interpolateColor255(value, colorMin, colorMax); 
    return result;
  }

  public int getBlue255(double value) {  
    double colorMin = colormin.getBlue();
    double colorMax = colormax.getBlue();
    int result = this.interpolateColor255(value, colorMin, colorMax); 
    return result;
  }

  public static class Builder {

    private double xmin;
    private double xmax;

    private String colormin;
    private String colormax;

    public Builder setXmax(double xmax) {
      this.xmax = xmax;
      return this;
    }

    public Builder setXmin(double xmin) {
      this.xmin = xmin;
      return this;
    }

    public Builder setColorMin(String colormin) {
      this.colormin = colormin;
      return this;
    }

    public Builder setColorMax(String colormax) {
      this.colormax = colormax;
      return this;
    }

    public ColorMap build() {
      return new ColorMap(xmin, xmax, colormin, colormax);
    }

  }
}
