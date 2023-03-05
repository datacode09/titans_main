package titans.goes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

/**
 *
 * @author Ricardo Marquez
 */
public class Content {
  
  private final String key;
  private final ZonedDateTime endTime;
  
  /**
   * 
   * @param key
   * @param endTime 
   */
  public Content(String key, ZonedDateTime endTime) {
    this.key = key;
    this.endTime = endTime;
  }
    
  /**
   * 
   * @return 
   */
  public String getVar() {
    int endIndex = this.key.indexOf("_s");
    String result = this.key.substring(0, endIndex); 
    return result; 
  }
  
  /**
   * 
   * @return 
   */
  public ZonedDateTime endTime(){
    return this.endTime;
  };

  /**
   * 
   * @return 
   */
  String key() {
    return this.key;
  }
  
  /**
   * 
   * @param refdate
   * @return 
   */
  public static ReferenceDateTimeComparator getComparator(ZonedDateTime refdate) {
    ReferenceDateTimeComparator result = new ReferenceDateTimeComparator(refdate); 
    return result; 
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public String toString() {
    return "{" + "key=" + key + ", endTime=" + endTime + '}';
  }
  
  public static class ReferenceDateTimeComparator {
    
    private final ZonedDateTime datetimeref;

    public ReferenceDateTimeComparator(ZonedDateTime datetimeref) {
      this.datetimeref = datetimeref;
    }
    
    /**
     * 
     * @param o1
     * @param o2
     * @return 
     */
    int compareDates(Content o1, Content o2) {
      int result =Duration.between(o1.endTime(), datetimeref).compareTo(Duration.between(o2.endTime(), datetimeref));
      return result;
    }  
  }
  
    
  /**
   * 
   * @param sublines
   * @return 
   */
  static Content create(List<String> sublines) {
    String key = getKey(sublines); 
    int startIndex = key.indexOf("_e") + 2; 
    int endIndex = key.indexOf("_c"); 
    String datetext = key.substring(startIndex, endIndex-1);
    DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyyDDDHHmmss").toFormatter();
    formatter.parse(datetext);
    LocalDateTime local = LocalDateTime.parse(datetext, formatter); 
    ZonedDateTime datetime = ZonedDateTime.of(local, ZoneId.of("UTC"));
    Content result = new Content(key, datetime);
    return result;
  }
  
  /**
   * 
   * @param sublines
   * @return 
   */
  private static String getKey(List<String> sublines) {
    String longkey = sublines.stream()
      .filter(l->l.trim().startsWith("<Key>"))
      .findFirst()
      .orElseThrow(RuntimeException::new)
      .trim()
      .replace("<Key>", "")
      .replace("</Key>", "")
      ;
    int startIndex = -1; 
    int index; 
    while ((index = longkey.indexOf("/", startIndex + 1)) > 0){
      startIndex = index;
    }
    String result = longkey.substring(startIndex + 1, longkey.length()); 
    return result;
  }
}
