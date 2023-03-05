package titans.goes;

import common.http.RmHttpReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.InputSource;

/**
 *
 * @author Ricardo Marquez
 */
public class BucketListReader {
  
  /**
   * 
   */
  public List<Content> read(String var, ZonedDateTime datetime) {
    List<String> lines = this.getLinesFromHttp(var, datetime);
    List<Content> result = new ArrayList<>();
    Iterator<String> iterator = lines.iterator();
    while (iterator.hasNext()) {
      String line = iterator.next();
      if (line.trim().startsWith("<Contents>")) {
        Content content = this.extractContent(iterator);
        result.add(content); 
      }
    }
    return result;
  }
  
  /**
   * 
   * @param iterator
   * @return 
   */
  private Content extractContent(Iterator<String> iterator) {
    List<String> sublines = new ArrayList<>();
    String line = "<Contents>";
    while(!line.trim().startsWith("</Contents>")) {
      line = iterator.next();
      sublines.add(line);
    }
    Content content = Content.create(sublines);
    return content;
  }
  
  /**
   * 
   * @param var
   * @param datetime
   * @return 
   */
  private List<String> getLinesFromHttp(String var, ZonedDateTime datetime) {
    String url = "https://noaa-goes18.s3.amazonaws.com/";
    int year = datetime.getYear();
    int doy = datetime.getDayOfYear();
    int hour = datetime.getHour();
    String prefix = String.format("%s/%04d/%03d/%02d/", var.split("\\$")[0], year, doy, hour);
    String prefixEncoded;
    try {
      prefixEncoded = URLEncoder.encode(prefix, "UTF-8");
    } catch (UnsupportedEncodingException ex) {
      throw new RuntimeException(ex); 
    }
    List<String> lines = new RmHttpReader.Builder(url)
      .setRequestParam("list-type", "2")
      .setRequestParam("delimiter", "/")
      .setRequestParam("prefix", prefixEncoded)
      .readTo(s->s);
    lines = Arrays.asList(prettyPrintByTransformer(lines.get(1), 1, true).split("\n")); 
    return lines;
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
