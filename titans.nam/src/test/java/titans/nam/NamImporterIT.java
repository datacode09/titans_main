package titans.nam;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import javax.measure.unit.Unit;
import org.junit.BeforeClass;
import org.junit.Test;
import rm.titansdata.Parameter;
import rm.titansdata.SridUtils;
import rm.titansdata.colormap.ColorMap;
import rm.titansdata.images.RasterImage;
import rm.titansdata.plugin.ColorMapProvider;
import rm.titansdata.raster.RasterObj;
import titans.nam.core.NamImporter;
import titans.nam.core.NamInventoryReader;
import titans.noaa.core.NoaaVariable;
import titans.noaa.grib.ForecastTimeReference;

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
    File netCdfRootFolder = new File("G:\\tests\\data\\netcdf");
    File degribExe = new File("C:\\ndfd\\degrib\\bin\\degrib.exe");
    String varName = "TMP_2-HTGL";
    String parentKey = "";
    RasterObj raster;
    ZoneId timeZone = ZoneId.of("UTC");
    ZonedDateTime refdate = ZonedDateTime //
      .now(timeZone) //
      .minusHours(3)
      .truncatedTo(ChronoUnit.DAYS);
    int forecastStep = 0;

    NamImporter importer = new NamImporter(gribRootFolder, netCdfRootFolder, 0, degribExe);
    Unit<?> unit = new NamInventoryReader().getUnit(varName);
    NoaaVariable var = new NoaaVariable(varName, unit);
    raster = importer.getRaster(var, refdate, forecastStep, null, null);
    ColorMapProvider cmprovider = new NamColorMapProvider(netCdfRootFolder);
    ForecastTimeReference ref = new ForecastTimeReference(refdate.getHour(), forecastStep);
    Parameter param = new NoaaParameter(parentKey, refdate, ref, varName, unit);
    ColorMap cmap = cmprovider.getColorMap(0, param);
    RasterImage image = new RasterImage(raster, cmap);
    File output = new File(gribRootFolder, "image.png");
    image.writeToFile("png", output);
    System.out.println("raster = " + raster);
  }
}
