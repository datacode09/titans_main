package titans.hrrr.core.grib;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import javax.measure.unit.Unit;
import org.apache.commons.io.IOUtils;
import rm.titansdata.units.UnitsUtils;
import titans.noaa.core.NoaaVarClazz;
import titans.noaa.core.InventoryReader;


/**
 *
 * @author Ricardo Marquez
 */
public class HrrrInventoryReader implements InventoryReader{

  public HrrrInventoryReader() {
  }

  @Override
  public List<NoaaVarClazz> read() {
    InputStream a = this.getInputStream();
    try {
      List<String> lines = IOUtils.readLines(a, Charset.forName("UTF-8"));
      lines.remove(0);
      List<NoaaVarClazz> result = lines.stream().filter(this::isDataLine).map(this::toHrrrVarClazz)
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
  private NoaaVarClazz toHrrrVarClazz(String line) {
    String[] parts0 = line.split(",");
    String[] parts = parts0[3].split("=");
    String varname = parts[0].trim() + "_" + parts0[4].trim();
    int startIndex = parts0[3].indexOf("[");
    int endIndex = parts0[3].indexOf("]");
    Unit<?> unit;    
    if (startIndex > -1) {
      String unitstext = parts0[3].substring(startIndex+1, endIndex);
      unit = UnitsUtils.valueOf(unitstext);
    } else {
      unit = null;
    }
    NoaaVarClazz result = new NoaaVarClazz(varname, unit);
    return result;
  }

  /**
   * 
   * @param noaaVar
   * @return 
   */
  @Override
  public Unit<?> getUnit(String var) {
    InputStream a = this.getInputStream();
    try {
      List<String> lines = IOUtils.readLines(a, Charset.forName("UTF-8"));
      lines.remove(0);
      Unit<?> result = lines.stream().filter(this::isDataLine)
        .map(this::toHrrrVarClazz) //
        .filter(c->c.getVarName().equals(var))
        .map(c->c.getUnit())
        .findFirst()
        .orElse(null);
      return result;
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
