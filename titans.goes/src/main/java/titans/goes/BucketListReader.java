package titans.goes;

import common.http.RmHttpReader;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
      if (line.trim().startsWith("<Content>")) {
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
    String line = "<Content>";
    while(line.trim().startsWith("")) {
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
    String prefix = String.format("%s/%s/%s/%s/", var, year, doy, hour);
    Charset charset = Charset.forName("UTF-8");
    List<String> lines = new RmHttpReader.Builder(url)
      .setRequestParam("list-type", "2")
      .setRequestParam("delimiter", "/")
      .setRequestParam("prefix", URLEncoder.encode(prefix, charset))
      .readTo(s->s);
    return lines;
  }
}
