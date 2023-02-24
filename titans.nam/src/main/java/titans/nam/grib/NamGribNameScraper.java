package titans.nam.grib;

import titans.noaa.grib.ForecastTimeReference;
import com.google.common.base.Objects;
import common.http.RmHttpReader;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.measure.unit.Unit;
import titans.nam.NoaaParameter;
import titans.nam.core.NamInventoryReader;

/**
 *
 * @author Ricardo Marquez
 */
public class NamGribNameScraper {

  /**
   *
   * @param minusDays
   * @param parentKey
   * @return
   */
  public List<NoaaParameter> getParameters(String url, String parentKey, long minusDays) {
    ZonedDateTime date = this.getDateForNamParameters(minusDays);
    String fullUrl = this.getNamParametersFullUrl(url, date);
    List<NoaaParameter> result = new RmHttpReader.Builder(fullUrl)
      .readTo((text) -> this.parseFullHtmlText(parentKey, date, text));
    result.removeIf(p -> p == null);
    result.stream()
      .sorted((o1, o2) -> -o1.datetime.compareTo(o2.datetime))
      .findFirst()
      //      .ifPresent(maxparameter -> result.removeIf(e -> !e.datetime.equals(maxparameter.datetime)));
      .ifPresent(maxparameter -> result.removeIf(e -> !Objects.equal(e.datetime.getHour(), 0)));
    return result;
  }

  /**
   *
   * @param minusDays
   * @return
   */
  private ZonedDateTime getDateForNamParameters(long minusDays) {
    ZonedDateTime result = ZonedDateTime.now(ZoneId.of("UTC"))
      .minusDays(minusDays)
      .minusHours(3)
      .truncatedTo(ChronoUnit.DAYS);
    return result;
  }

  /**
   *
   * @param date
   * @return
   */
  private String getNamParametersFullUrl(String url, ZonedDateTime date) {
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
      .appendPattern("yyyyMMdd")
      .toFormatter();
    String datetext = date
      .format(formatter);
    String fullUrl = url + "nam." + datetext;
    return fullUrl;
  }

  /**
   *
   * @param parentKey
   * @param date
   * @param text
   * @return
   */
  private NoaaParameter parseFullHtmlText(String parentKey, ZonedDateTime date, String text) {
    String var = "TMP_2-HTGL";
    NoaaParameter result;
    if (this.isConusNestLine(text)) {
      ForecastTimeReference d = this.toForecastTimeRef(text);
      Unit<?> unit = new NamInventoryReader().getUnit(var);
      result = new NoaaParameter(parentKey, date, d, var, unit);
    } else {
      result = null;
    }
    return result;
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
    String trimmed = r.replaceAll(".*?hiresf", "").replace(".tm00.grib2", "");
    int result = Integer.parseInt(trimmed);
    return result;
  }

  /**
   *
   * @param line
   * @return
   */
  private boolean isConusNestLine(String line) {
    boolean isConusNest = line.contains(".conusnest.");
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
  private int parseLineToHour(String text) {
    int result = Integer.parseInt(text.replaceFirst("nam.t", "")
      .substring(0, 2));
    return result;
  }

}
