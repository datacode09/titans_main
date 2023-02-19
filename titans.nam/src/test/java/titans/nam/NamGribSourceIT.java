package titans.nam;

import java.io.File;
import java.util.List;
import java.util.Set;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;
import org.junit.Test;
import titans.nam.grib.GribFileVarsReader;
import titans.nam.grib.NamGribSource;

/**
 *
 * @author Ricardo Marquez
 */
public class NamGribSourceIT {

  @Test
  public void test01() {
    NamGribSource source = new NamGribSource();
    List<NoaaParameter> list = source.getCurrentNamParameters("NAM");
    list.forEach(System.out::println);
  }
  
  @Test
  public void test02() {
    String line = "<a href=\"nam.t00z.class1.bufr_priconest.tm00\">nam.t00z.class1.bufr_priconest.tm00</a>         13-Jan-2023 02:31  2.0M  ";
    String a = line.replaceAll("<.*?>", "")
      .replaceAll("\\s.*$", "")
      .replaceFirst("nam.t", "")
      .substring(0, 2)
      ; 
    System.out.println(a);
  }
  
  @Test
  public void test03() {
    File degribExe = new File("C:\\ndfd\\degrib\\bin\\degrib.exe") ;
    File gribFile = new File("G:\\tests\\data\\nam\\grib\\nam.t00z.conusnest.hiresf01.tm00.grib2");
    GribFileVarsReader reader = new GribFileVarsReader(degribExe, gribFile);
    Set<String> varNames = reader.parseVarNames();
    for (String varName : varNames) {
      Unit<? extends Quantity> unit = reader.getUnit(varName);
    }    
  }
}
