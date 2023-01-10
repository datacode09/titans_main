package rm.titansdata.images;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import javafx.util.Pair;
import rm.titansdata.colormap.ColorMap;
import rm.titansdata.raster.Cell;
import rm.titansdata.raster.RasterObj;
import rm.titansdata.raster.RasterSearch;

/**
 *
 * @author Ricardo Marquez
 */
public class RasterImage {

  private final RasterObj r;

  private final ColorMap cmap;
  
  /**
   * 
   * @param r
   * @param cmap 
   */
  public RasterImage(RasterObj r, ColorMap cmap) {
    this.r = r;
    this.cmap = cmap;
  }

  /**
   *
   */
  public BufferedImage asBufferedImage() {
    BufferedImage result = this.getInitialBufferedImage();
    WritableRaster writableRaster = this.asWritableRaster(result);
    result.setData(writableRaster);
    return result;
  }
  
  /**
   * 
   * @param img
   * @return 
   */
  private WritableRaster asWritableRaster(BufferedImage img) {
    WritableRaster raster = img.getRaster();
    WritableRaster writableRaster = raster.createCompatibleWritableRaster();
    RasterSearch search = new RasterSearch(this.r.getBounds(), this.r.getDimensions());
    search.stream(p -> this.r.getValue(p)).forEach(p -> {
      this.setImageValue(p, writableRaster);
    });
    return writableRaster;
  }
  
  /**
   * 
   * @return 
   */
  private BufferedImage getInitialBufferedImage() {
    RasterSearch h = new RasterSearch(this.r.getBounds(), this.r.getDimensions());
    int[] ij = h.maxIndices();
    int imax = ij[0];
    int jmax = ij[1];
    BufferedImage img = new BufferedImage(imax, jmax, BufferedImage.TYPE_INT_RGB);
    return img;
  }
  
  /**
   * 
   * @param p
   * @param img 
   */
  private void setImageValue(Pair<Integer, Cell> p, WritableRaster img) {
    try {
      Cell c = p.getValue();
      int[] dArray = this.getColorValue(c);
      int i = c.ij[0];
      int j = c.ij[1];
      img.setPixel(i, j, dArray);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
  
  /**
   * 
   * @param c
   * @return 
   */
  private int[] getColorValue(Cell c) {
    double value = c.getValue();
    int redAmount = this.cmap.getRed255(value);
    int greenAmount = this.cmap.getGreen255(value);
    int blueAmount = this.cmap.getBlue255(value);
    int[] dArray = new int[]{redAmount, greenAmount, blueAmount};
    if (!this.isValidRgb(dArray)) {
      throw new RuntimeException();
    }
    return dArray;
  }
  
  /**
   * 
   * @param dArray
   * @return 
   */
  private boolean isValidRgb(int[] dArray) {
    boolean validRgb = 0 <= dArray[0] && dArray[0] <= 255
      && 0 <= dArray[1] && dArray[1] <= 255
      && 0 <= dArray[2] && dArray[2] <= 255;
    return validRgb;
  }
}
