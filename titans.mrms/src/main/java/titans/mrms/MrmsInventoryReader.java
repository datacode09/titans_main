package titans.mrms;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import javax.measure.unit.Unit;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.IOUtils;
import org.xml.sax.InputSource;
import titans.noaa.core.InventoryReader;
import titans.noaa.core.NoaaVarClazz;

/**
 *
 * @author Ricardo Marquez
 */
public class MrmsInventoryReader implements InventoryReader {

  private final String url = "https://noaa-mrms-pds.s3.amazonaws.com/?list-type=2&delimiter=%2F&prefix=CONUS%2F";

  public MrmsInventoryReader() {
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
      List<NoaaVarClazz> result = lines.stream()
        .map(this::toName)
        .filter(s -> s != null)
        .map(s -> new NoaaVarClazz(s, Unit.ONE))
        .collect(Collectors.toList());
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
    String inventoryfile = "mrms.inventory.xml";
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

  public static String prettyPrintByTransformer(String xmlString, int indent, boolean ignoreDeclaration) {

    try {
      InputSource src = new InputSource(new StringReader(xmlString));
      org.w3c.dom.Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src);
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      transformerFactory.setAttribute("indent-number", indent);
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, ignoreDeclaration ? "yes" : "no");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      Writer out = new StringWriter();
      transformer.transform(new DOMSource(document.cloneNode(true)), new StreamResult(out));
      return out.toString();
    } catch (Exception e) {
      throw new RuntimeException("Error occurs when pretty-printing xml:\n" + xmlString, e);
    }
  }

}
