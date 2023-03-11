package titans.goes;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.measure.unit.Unit;
import org.apache.commons.io.IOUtils;
import titans.noaa.core.InventoryReader;
import titans.noaa.core.NoaaVarClazz;

/**
 *
 * @author Ricardo Marquez
 */
public class GoesInventoryReader implements InventoryReader {
  
  public GoesInventoryReader() {
  }

  /**
   *
   * @return
   */
  @Override
  public List<NoaaVarClazz> read() {
    InputStream a = this.getInputStream();
    try {
      List<String> lines = IOUtils.readLines(a, Charset.forName("UTF-8"));
      List<NoaaVarClazz> list = lines.stream()
        .map(this::toName)
        .filter(s -> s != null)
        .map(s -> new NoaaVarClazz(s, Unit.ONE))
        .collect(Collectors.toList());
      List<NoaaVarClazz> result = this.addChannels(list);
      return result;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   *
   * @return
   */
  private InputStream getInputStream() {
    String inventoryfile = "goes18inventory.xml";
    InputStream a = this.getClass().getClassLoader().getResourceAsStream(inventoryfile);
    return a;
  }

  /**
   *
   * @param line
   * @return
   */
  private String toName(String line) {
    String result;
    if (line.trim().startsWith("<Prefix>")) {
      result = line.trim()
        .replace("<Prefix>", "")
        .replace("</Prefix>", "")
        .replace("CONUS/", "")
        .replace("/", "");
    } else {
      result = null;
    }
    return result;
  }

  /**
   *
   * @param var
   * @return
   */
  @Override
  public Unit<?> getUnit(String var) {
    return Unit.ONE;
  }
  
  /**
   * 
   * @param list
   * @return 
   */
  private List<NoaaVarClazz> addChannels(List<NoaaVarClazz> list) {
    List<NoaaVarClazz> result = new ArrayList<>(list);
    List<NoaaVarClazz> copy = new ArrayList<>();
    for (NoaaVarClazz noaaVarClazz : list) {
      if (noaaVarClazz.getVarName().startsWith("ABI-L1b")) {
        List<NoaaVarClazz> channels = this.getChannels(noaaVarClazz.getVarName());
        result.addAll(channels); 
        copy.add(noaaVarClazz); 
      } 
    }
    result.removeAll(copy); 
    return result;
  }
  
  /**
   * 
   * @param varName
   * @return 
   */
  private List<NoaaVarClazz> getChannels(String varName) {
    List<NoaaVarClazz> result = IntStream.range(1, 17) //
      .mapToObj(i->String.format("%s$M6C%02d", varName, i)) //
      .map(s->new NoaaVarClazz(s, Unit.ONE)) //
      .collect(Collectors.toList());
    return result; 
  }

}
