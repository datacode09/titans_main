package titans.nam;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.BeforeClass;
import org.junit.Test;
import rm.titansdata.SridUtils;
import rm.titansdata.colormap.ColorMap;
import rm.titansdata.images.RasterImage;
import rm.titansdata.raster.RasterObj;
import titans.nam.core.NamImporter;

/**
 *
 * @author Ricardo Marquez
 */
public class NamImporterIT {
  
  /**
   * 
   */
  @BeforeClass
  public static void before() {
    SridUtils.init();
  }
  
  /**
   *
   * @throws Exception
   */
  @Test
  public void testimport() throws Exception {
    File gribRootFolder = new File("G:\\tests\\data\\grib");
    File degribExe  = new File("C:\\ndfd\\degrib\\bin\\degrib.exe");
    RasterObj raster;
    try (NamImporter importer = new NamImporter(gribRootFolder, degribExe)) {
      ZoneId timeZone = ZoneId.of("UTC");
      ZonedDateTime refdate = ZonedDateTime  //
        .now(timeZone)  //
        .truncatedTo(ChronoUnit.DAYS);
      int forecastStep = 0;
      raster = importer.getRaster(refdate, forecastStep);
      ColorMap cmap = new ColorMap.Builder()
        .setXmin(098000.0)
        .setXmax(110000.0)
        .setColorMin("#000")
        .setColorMax("#fff")
        .build();
      RasterImage image = new RasterImage(raster, cmap);
      File output = new File(gribRootFolder, "image.png");
      image.writeToFile("png", output);
    }
    System.out.println("raster = " + raster);
  }
}
