package titan.goes;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import org.junit.Test;

/**
 *
 * @author Ricardo Marquez
 */
public class QuickIT {

  @Test
  public void test() {
    String datetext = "2022364030354";
    DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyyDDDHHmmss").toFormatter();
    formatter.parse(datetext);
  }
}
