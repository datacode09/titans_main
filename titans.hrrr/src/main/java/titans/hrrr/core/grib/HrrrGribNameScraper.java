package titans.hrrr.core.grib;

import com.google.common.base.Objects;
import common.http.RmHttpReader;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.measure.unit.Unit;
import titans.nam.NoaaParameter;
import titans.nam.grib.ForecastTimeReference;

/**
 *
 * @author Ricardo Marquez
 */
public class HrrrGribNameScraper {

  public List<NoaaParameter> getParameters(String url, String parentKey, int minusDays) {
    DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyyMMdd").toFormatter();
    ZonedDateTime date = ZonedDateTime.now(ZoneId.of("UTC"))
      .minusDays(minusDays)
      .minusHours(3)
      .truncatedTo(ChronoUnit.DAYS);
    String datetext = date
      .format(formatter);
    String fullUrl = url + "hrrr." + datetext + "/conus";
    List<NoaaParameter> result = new ArrayList<>();
    String var = "TMP_2-HTGL";
    Unit<?> unit = new HrrrInventoryReader().getUnit(var);
    new RmHttpReader.Builder(fullUrl).read((text) -> {
      
      Arrays.stream(text.split("\n")) //
        .filter(this::isConusNestLine)
        .map(this::toForecastTimeRef)
        .map(d -> new NoaaParameter(parentKey, date, d, var, unit))
        .forEach(result::add);
    });
    result.stream()
      .sorted((o1, o2) -> -o1.datetime.compareTo(o2.datetime))
      .findFirst()
      //      .ifPresent(maxparameter -> result.removeIf(e -> !e.datetime.equals(maxparameter.datetime)));
      .ifPresent(maxparameter -> result.removeIf(e -> !Objects.equal(e.datetime.getHour(), 0)));
    return result;
  }
  
  
    /**
   *
   * @param line
   * @return
   */
  private boolean isConusNestLine(String line) {
    boolean isConusNest = line.contains(".wrfsfcf");
    if (isConusNest) {
      boolean isGribFile = line.contains(".grib2</a>");
      return isGribFile;
    }
    return false;
  }
  
  
  
  /**
   *
   * @param text
   * @return
   */
  private ForecastTimeReference toForecastTimeRef(String text) {
    String r = text.replaceAll("<.*?>", "")
      .replaceAll("\\s.*$", "");
    int hour = this.parseLineToHour(r);
    int fcststep = this.parseLineToForecastStep(r);
    ForecastTimeReference result = new ForecastTimeReference(hour, fcststep);
    return result;
  }
  

  /**
   *
   * @param r
   * @return
   */
  private int parseLineToForecastStep(String r) {
    String trimmed = r.replaceAll(".*?wrfsfcf", "").replace(".grib2", "");
    int result = Integer.parseInt(trimmed);
    return result;
  }

  

  /**
   *
   * @param text
   * @return
   */
  private int parseLineToHour(String text) {
    int result = Integer.parseInt(text.replaceFirst("hrrr.t", "")
      .substring(0, 2));
    return result;
  }
}
