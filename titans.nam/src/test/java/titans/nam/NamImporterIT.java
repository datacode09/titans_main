package titans.nam;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.Test;
import rm.titansdata.raster.RasterObj;

/**
 *
 * @author Ricardo Marquez
 */
public class NamImporterIT {

  /**
   *
   * @throws Exception
   */
  @Test
  public void testimport() throws Exception {
    File gribRootFolder = new File("G:\\tests\\data\\grib");
    File degribExe  = new File("C:\\ndfd\\degrib\\bin\\degrib.exe");
    NamImporter importer = new NamImporter(gribRootFolder, degribExe); 
    ZoneId timeZone = ZoneId.of("UTC"); 
    ZonedDateTime refdate = ZonedDateTime  //
      .now(timeZone)  //
      .truncatedTo(ChronoUnit.DAYS); 
    int forecastStep = 0; 
    RasterObj raster = importer.getRaster(forecastStep, refdate);
    int srid = 4326;
    PrecisionModel pm = new PrecisionModel(PrecisionModel.FLOATING); 
    GeometryFactory factory = new GeometryFactory(pm, srid);
    Point p = factory.createPoint(new Coordinate(-120.43, 37.36));
    double value = raster.getValue(p);
    System.out.println("raster = " + raster);
  }
}
