package titans.nam;

import java.util.List;
import org.junit.Test;
import titans.nam.grib.NamGribSource;

/**
 *
 * @author Ricardo Marquez
 */
public class NamGribSourceIT {

  @Test
  public void test01() {
    NamGribSource source = new NamGribSource();
    List<NamParameter> list = source.getCurrentNamParameters();
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
}
