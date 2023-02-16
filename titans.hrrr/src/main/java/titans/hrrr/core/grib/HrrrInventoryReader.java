package titans.hrrr.core.grib;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import titans.hrrr.classes.HrrrVarClazz;

/**
 *
 * @author Ricardo Marquez
 */
public class HrrrInventoryReader {

  public HrrrInventoryReader() {
  }

  public List<HrrrVarClazz> read() {
    InputStream a = this.getInputStream();
    try {
      List<String> lines = IOUtils.readLines(a, Charset.forName("UTF-8"));
      lines.remove(0);
      List<HrrrVarClazz> result = lines.stream().filter(this::isDataLine).map(this::toHrrrVarClazz)
        .collect(Collectors.toList());
      return result;
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  /**
   * 
   * @return 
   */
  private InputStream getInputStream() {
    String inventoryfile = "hrrr.inventory.txt";
    InputStream a = this.getClass().getClassLoader().getResourceAsStream(inventoryfile);
    return a;
  }

  /**
   *
   * @param line
   * @return
   */
  private boolean isDataLine(String line) {
    return true;
  }

  /**
   *
   * @param line
   * @return
   */
  private HrrrVarClazz toHrrrVarClazz(String line) {
    String[] parts0 = line.split(",");
    String[] parts = parts0[3].split("=");
    String varname = parts[0] + "_" + parts0[4].trim();
    HrrrVarClazz result = new HrrrVarClazz(varname, varname);
    return result;
  }
}
