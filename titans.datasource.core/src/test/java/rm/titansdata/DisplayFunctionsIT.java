package rm.titansdata;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import javax.imageio.ImageIO;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import rm.titansdata.colormap.ColorMap;
import rm.titansdata.images.RasterImage;
import rm.titansdata.raster.RasterObj;
import rm.titansdata.testdata.RasterITTestData;

/**
 *
 * @author Ricardo Marquez
 */
@RunWith(JUnitParamsRunner.class)
public class DisplayFunctionsIT {

  @Test
  @Parameters({
    "0, 100, #fff, #000, 100, #000000",
    "0, 100, #fff, #000, 0, #ffffff",
    "0, 100, #fff, #000, 50, #808080"
  })
  public void linearcolormap_known_inputs_outputs( //
    double xmin, double xmax, String colormin, String colormax, double x, String expresult) {
    ColorMap instance = new ColorMap.Builder()
      .setXmin(xmin)
      .setXmax(xmax)
      .setColorMin(colormin)
      .setColorMax(colormax)
      .build();
    String result = instance.getColorHex(x);
    Assert.assertEquals("colormap_known_inputs_outputs", expresult, result);
  }

  @Test
  @Parameters({
    "G:\\tests\\data\\image.jpg"
  })
  public void full_image(String arg) throws Exception {
    RasterObj rasterobj = RasterITTestData.getBasicRasterObj();
    double max = Arrays.stream(rasterobj.interleave().values())
      .max()
      .orElseThrow(() -> new RuntimeException());
    ColorMap cmap = new ColorMap.Builder()
      .setColorMin("#000")
      .setColorMax("#ffff00")
      .setXmin(0)
      .setXmax(max)
      .build();
    RasterImage image = new RasterImage(rasterobj, cmap);
    BufferedImage bufferedImage = image.asBufferedImage();
    File outputfile = new File(arg);
    ImageIO.write(bufferedImage, "jpg", outputfile);
  }

  @Test
  @Parameters({
    "-120.00, 37.00, -121.50, 40.00, 4326, G:\\tests\\data\\image_subset.jpg"
  })
  public void partial_image( //
    double x1, double y1, double x2, double y2, int srid, String arg) throws Exception {

    RasterObj rasterobj = RasterITTestData.getBasicRasterObj();
    double max = Arrays.stream(rasterobj.interleave().values())
      .max()
      .orElseThrow(() -> new RuntimeException());
    ColorMap cmap = new ColorMap.Builder()
      .setColorMin("#000")
      .setColorMax("#ffff00")
      .setXmin(0)
      .setXmax(max)
      .build();
    PrecisionModel precisionModel = new PrecisionModel(PrecisionModel.FLOATING);
    GeometryFactory factory = new GeometryFactory(precisionModel, srid);
    Polygon p = factory.createPolygon(new Coordinate[]{
      new Coordinate(x1, y1),
      new Coordinate(x2, y1),
      new Coordinate(x2, y2),
      new Coordinate(x1, y2),
      new Coordinate(x1, y1),});
    RasterObj subset = rasterobj.getSubsetRaster("subset", p);
    RasterImage image = new RasterImage(subset, cmap);
    BufferedImage bufferedImage = image.asBufferedImage();
    File outputfile = new File(arg);
    ImageIO.write(bufferedImage, "jpg", outputfile);
  }
}
