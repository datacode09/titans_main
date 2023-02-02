package rm.titansdata.web.rasters;

import org.locationtech.jts.geom.Point;

/**
 *
 * @author Ricardo Marquez
 */
public class RasterImageResult {

  public final String imageURL;
  public final Point upperRight;
  public final Point lowerLeft;  
  public final int srid;

  public RasterImageResult(String imageURL, Point upperRight, Point lowerLeft, int srid) {
    this.imageURL = imageURL;
    this.upperRight = upperRight;
    this.lowerLeft = lowerLeft;
    this.srid = srid;
  }
  
}
