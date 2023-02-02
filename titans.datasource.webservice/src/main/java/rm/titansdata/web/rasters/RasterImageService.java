package rm.titansdata.web.rasters;

import org.locationtech.jts.geom.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import rm.titansdata.SridUtils;
import rm.titansdata.colormap.ColorMap;
import rm.titansdata.images.RasterImage;
import rm.titansdata.raster.RasterObj;
import rm.titansdata.raster.RasterSearch;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class RasterImageService {

  @Autowired  
  private RastersValueService rastersValueService;

  /**
   *
   * @param rasterId
   * @return
   */
  public RasterImageResult getRasterImage(long rasterId, Parameter param) {
    RasterObj r = this.getRasterObj(rasterId, param);
    RasterSearch s = new RasterSearch(r.getBounds(), r.getDimensions()); 
    s.maxIndices(); 
    ColorMap cmap = this.getColorMap(s, r);
    RasterImage img = new RasterImage(r, cmap);  
    RasterImageResult result = this.toRasterImageResult(rasterId, img);
    return result;
  }
  
  /**
   * 
   * @param img
   * @param r
   * @param rasterId
   * @return 
   */
  private RasterImageResult toRasterImageResult(long rasterId, RasterImage img) {
    BufferedImage bufferedImg = img.asBufferedImage();
    int targetSrid = 3857;   
    Point upperRight = SridUtils.transform(img.getBounds().upperright(), targetSrid);
    Point lowerLeft = SridUtils.transform(img.getBounds().lowerleft(), targetSrid);
    String imageURL = this.getImageURL(rasterId, bufferedImg);
    RasterImageResult result = new RasterImageResult(imageURL, upperRight, lowerLeft, targetSrid);
    return result;
  }
  
  /**
   * 
   * @param s
   * @param r
   * @return 
   */
  private ColorMap getColorMap(RasterSearch s, RasterObj r) {
    double max = s.stream(b->r.getValue(b))
      .mapToDouble(i->i.getValue().getValue())
      .max()
      .orElseThrow(()->new RuntimeException());
    double min = s.stream(b->r.getValue(b))
      .mapToDouble(i->i.getValue().getValue())
      .min()
      .orElseThrow(()->new RuntimeException());
    ColorMap cmap = new ColorMap.Builder()
      .setXmin(min)
      .setXmax(max)
      .setColorMin("#000")
      .setColorMax("#fff")
      .build();
    return cmap;
  }

  /**
   *
   * @param rasterId
   * @return
   */
  private RasterObj getRasterObj(long rasterId, Parameter p) {
    RasterObj result = this.rastersValueService.getRasterObj(rasterId, p); 
    return result;
  }

  /**
   *
   * @param rasterId
   * @param bufferedImg
   * @return
   */
  private String getImageURL(long rasterId, BufferedImage bufferedImg) {
    String filename = rasterId + ".png";
    File f = new File("C:\\Servers\\apache-tomcat-8.5.45\\webapps\\data\\" + filename);
    if (!f.getParentFile().exists()) {
      f.getParentFile().mkdirs(); 
    }
    try (ImageOutputStream output = new FileImageOutputStream(f)) {
      ImageIO.write(bufferedImg, "png", output);
    } catch(Exception ex) {
      throw new RuntimeException(ex);
    }
    String result = "http://localhost:8081/data/" + filename;
    return result;
  }
}
