package titans.hrrr;

import common.RmThreadUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import titans.hrrr.core.grib.HrrrGribSource;
import titans.hrrr.core.grib.HrrrInventoryReader;
import titans.nam.NoaaParameter;
import titans.nam.classes.NoaaVarClazz;
import titans.nam.grib.GribFile;
import titans.nam.grib.NamGribSource;

/**
 *
 * @author Ricardo Marquez
 */
public class HrrrGribSourceIT {

  @Test
  public void test01() {
    HrrrGribSource source = new HrrrGribSource();
    int minusDays = 0;
    List<NoaaParameter> list = source.getCurrentParameters("HRRR", minusDays);
    list.forEach(System.out::println);
  }

  @Test
  public void test02() {
    HrrrInventoryReader reader = new HrrrInventoryReader();
    List<NoaaVarClazz> vars = reader.read();
    vars.forEach(System.out::println);
  }

  @Test
  @Ignore
  public void test03() throws Exception {
    File g = new File("G:\\tests\\data\\test.grib2");
    File gidx = new File("G:\\tests\\data\\grib2.idx");
    NamGribSource source = new NamGribSource();
    ZonedDateTime datetime = ZonedDateTime.now().minusDays(1).truncatedTo(ChronoUnit.DAYS);
    GribFile file = new GribFile(datetime, 0, g, gidx);
    URL url = new URL(source.createUrl(file)); 
    InputStream input = url.openStream();
    new RmThreadUtils.ThreadBuilder("afadfasf").setRunnable(() -> {
      try (OutputStream out = file.getOutputStream()) {
        IOUtils.copy(input, out);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }).start();
    Thread.sleep(3000);
    GribFile file2 = new GribFile(ZonedDateTime.now(), 0, g, gidx);
    boolean locked = file2.isLocked();
    Assert.assertTrue(locked);
    System.out.println("locked = " + locked);
  }

  @Test
  public void test04() throws Exception {
    File g = new File("G:\\tests\\data\\test.grib2");
    File gidx = new File("G:\\tests\\data\\grib2.idx");
    GribFile file = new GribFile(ZonedDateTime.now(), 0, g, gidx);
    boolean locked = file.isLocked();
    Assert.assertFalse(locked);
  }
}
