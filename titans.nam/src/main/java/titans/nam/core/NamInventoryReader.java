package titans.nam.core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import titans.nam.classes.NoaaVarClazz;

/**
 *
 * @author Ricardo Marquez
 */
public class NamInventoryReader {

  public NamInventoryReader() {
  }

  public List<NoaaVarClazz> read() {
    InputStream a = this.getInputStream();
    try {
      List<String> lines = IOUtils.readLines(a, Charset.forName("UTF-8"));
      lines.remove(0);
      List<NoaaVarClazz> result = lines.stream().filter(this::isDataLine).map(this::toNoaaVarClazz)
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
    String inventoryfile = "nam.inventory.txt";
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
  private NoaaVarClazz toNoaaVarClazz(String line) {
    String[] parts0 = line.split(",");
    String[] parts = parts0[3].split("=");
    String varname = parts[0] + "_" + parts0[4].trim();
    NoaaVarClazz result = new NoaaVarClazz(varname);
    return result;
  }
}
