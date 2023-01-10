package rm.titansdata.profiling;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;
import common.db.DbConnection;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import rm.titansdata.raster.RasterCells;
import rm.titansdata.web.rasters.RastersSourceService;
import rm.titansdata.web.rasters.RastersValueService;

/**
 *
 * @author Ricardo Marquez
 */
public class Main {

  public static void main(String[] args) throws Exception {

    RastersSourceService sourceservice = new RastersSourceService();
    Field dbconnfield = sourceservice.getClass().getDeclaredField("dbconn");
    DbConnection dbconn = new DbConnection.Builder()
      .setUrl("localhost")
      .setPort(5434)
      .setDatabaseName("titans.application")
      .setUser("postgres")
      .setPassword("postgres")
      .createDbConnection();
    dbconnfield.setAccessible(true);
    dbconnfield.set(sourceservice, dbconn);

    RastersValueService valueservice = new RastersValueService();
    Field sourceserviceField = valueservice.getClass().getDeclaredField("sourceService");
    sourceserviceField.setAccessible(true);
    sourceserviceField.set(valueservice, sourceservice);
    String wkt = URLDecoder.decode("POLYGON ((766962 4099080%2C 633468 4095923%2C 628042 4428834%2C 756099 4432069%2C 766962 4099080))", StandardCharsets.UTF_8.toString());
    PrecisionModel pm = new PrecisionModel(PrecisionModel.FLOATING);
    GeometryFactory factory = new GeometryFactory(pm, 32610);
    Geometry geometry = new WKTReader(factory).read(wkt);
    long rasterId = 0L; 
    RasterCells values = valueservice.getRasterValues(rasterId, geometry);
    System.out.println("values = " + values);
  }
}
