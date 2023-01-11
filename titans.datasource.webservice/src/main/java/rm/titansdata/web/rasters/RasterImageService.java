package rm.titansdata.web.rasters;

import com.vividsolutions.jts.geom.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.titansdata.SridUtils;
import rm.titansdata.colormap.ColorMap;
import rm.titansdata.images.RasterImage;
import rm.titansdata.raster.RasterObj;

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
  public RasterImageResult getRasterImage(long rasterId) {
    ColorMap cmap = new ColorMap.Builder()
      .setXmin(0)
      .setXmax(1.0)
      .setColorMin("#000")
      .setColorMax("#fff")
      .build();
    RasterObj r = this.getRasterObj(rasterId);
    RasterImage img = new RasterImage(r, cmap);
    BufferedImage bufferedImg = img.asBufferedImage();
    int targetSrid = 3857;   
    Point upperRight = SridUtils.transform(r.getBounds().upperright(), targetSrid);
    Point lowerLeft = SridUtils.transform(r.getBounds().lowerleft(), targetSrid);
    String imageURL = this.getImageURL(rasterId, bufferedImg);
    RasterImageResult result = new RasterImageResult(imageURL, upperRight, lowerLeft, targetSrid);
    return result;
  }

  /**
   *
   * @param rasterId
   * @return
   */
  private RasterObj getRasterObj(long rasterId) {
    RasterObj result = this.rastersValueService.getRasterObj(rasterId); 
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
