package titans.nam;

import java.time.ZonedDateTime;
import rm.titansdata.Parameter;
import titans.nam.grib.ForecastTimeReference;

/**
 *
 * @author Ricardo Marquez
 */
public class NamParameter implements Parameter {

  public ZonedDateTime datetime;
  public int fcststep;
  
  public NamParameter(ZonedDateTime datetime, ForecastTimeReference d) {
    this.datetime = datetime.plusHours(d.refhour); 
    this.fcststep = d.fcsthourAhead; 
  }

  @Override
  public String toString() {
    return "{" + "datetime=" + datetime + ", fcststep=" + fcststep + '}';
  }
  
}
