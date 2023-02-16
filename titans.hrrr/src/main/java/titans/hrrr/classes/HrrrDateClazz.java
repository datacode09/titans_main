package titans.hrrr.classes;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import rm.titansdata.plugin.Clazz;

/**
 *
 * @author Ricardo Marquez
 */
public class HrrrDateClazz implements Clazz {

  public static HrrrDateClazz TODAY = new HrrrDateClazz(ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS));
  public static HrrrDateClazz YESTERDAY = new HrrrDateClazz(ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS));
  
  public HrrrDateClazz(ZonedDateTime datetime) {
  }
  
  
  @Override
  public String getKey() {
    return "HrrrDateClazz"; 
  }
  
}
