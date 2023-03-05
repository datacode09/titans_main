package titans.nam;

import common.RmTimer;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import javax.measure.unit.Unit;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import titans.noaa.netcdf.NetCdfFile;
import titans.noaa.netcdf.NetCdfRaster;

/**
 *
 * @author Ricardo Marquez
 */
@RunWith(JUnitParamsRunner.class)
public class NetCdfRasterIT {
  
  @Test
  @Parameters({
    "-121.43, 37.36"
  })
  public void test(double x, double y) {
    File baseFolder = new File("G:\\tests\\data\\goes\\netcdf");
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
      .appendPattern("yyyyMMddHHmm")
      .toFormatter();    
    LocalDateTime local = LocalDateTime.parse("202212301800", formatter);
    ZonedDateTime datetime = ZonedDateTime.of(local, ZoneId.of("UTC"));
    String parentKey = "GOES-18";
    int fcststep = -1;
    String noaaVar = "ABI-L1b-RadC$M6C01";
    int project = 790;
    PrecisionModel precisionModel = new PrecisionModel(PrecisionModel.FLOATING); 
    int srid = 4326; 
    GeometryFactory factory = new GeometryFactory(precisionModel, srid); 
    Point point = factory.createPoint(new Coordinate(x, y));
    NoaaParameter namParameter = new NoaaParameter(parentKey, datetime, fcststep, noaaVar, Unit.ONE);
    NetCdfFile netCdfFile = NetCdfFile.create(baseFolder, project, namParameter);
    Bounds bounds = netCdfFile.getBounds();
    Dimensions dims = netCdfFile.getDimensions();
    NetCdfRaster raster = new NetCdfRaster(netCdfFile, bounds, dims);
    RmTimer timer = RmTimer.start();
    for (int i = 0; i < 500000; i++) {
      raster.getValue(point);
    }
    timer.endAndPrint();
    
  }
}
