package titans.hrrr.classes;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import rm.titansdata.plugin.Clazz;

/**
 *
 * @author Ricardo Marquez
 */
public class HrrrDateClazz implements Clazz {

  public static HrrrDateClazz TODAY = new HrrrDateClazz(ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS));
  public static HrrrDateClazz YESTERDAY = new HrrrDateClazz(ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS));
  private final ZonedDateTime datetime;
  
  public HrrrDateClazz(ZonedDateTime datetime) {
    this.datetime = datetime;
  }
  
  
  @Override
  public String getKey() {
    return "HrrrDateClazz"; 
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public String toJson() {
    DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("").toFormatter();
    String formatteddatetime = this.datetime.format(formatter);
    return "{'key': '" + this.getKey() + "', 'datetime':' " + formatteddatetime  + "'}";
  }
  
  
    
  
  
}
