package titans.nam.grib;

import com.google.common.base.Objects;
import common.http.RmHttpReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import titans.nam.NoaaParameter;

/**
 *
 * @author Ricardo Marquez
 */
public class NamGribSource {

  private final String url = "https://nomads.ncep.noaa.gov/pub/data/nccf/com/nam/prod/";

  /**
   *
   * @param parentKey
   * @return
   */
  public List<NoaaParameter> getCurrentNamParameters(String parentKey) {
    long minusDays = 2l;
    List<NoaaParameter> result = this.getCurrentNamParameters(minusDays, parentKey);
    return result;
  }

  /**
   *
   * @param minusDays
   * @param parentKey
   * @return
   */
  public List<NoaaParameter> getCurrentNamParameters(long minusDays, String parentKey) {
    ZonedDateTime date = this.getDateForNamParameters(minusDays);
    String fullUrl = this.getNamParametersFullUrl(date);
    List<NoaaParameter> result = new RmHttpReader.Builder(fullUrl)
      .readTo((text) -> this.parseFullHtmlText(parentKey, date, text));
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
    return ZonedDateTime.now(ZoneId.of("UTC"))
      .minusDays(minusDays)
      .truncatedTo(ChronoUnit.DAYS);
  }
  
  /**
   * 
   * @param date
   * @return 
   */
  private String getNamParametersFullUrl(ZonedDateTime date) {
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
      .appendPattern("yyyyMMdd")
      .toFormatter();
    String datetext = date
      .format(formatter);
    String fullUrl = this.url + "nam." + datetext;
    return fullUrl;
  }
  
  private List<NoaaParameter> parseFullHtmlText(String parentKey, ZonedDateTime date, String text )  {
        String var = "TMP_2-HTGL";
    List<NoaaParameter> a = Arrays.stream(text.split("\n")) //
        .filter(this::isConusNestLine)
        .map(this::toForecastTimeRef)
        .map(d -> new NoaaParameter(parentKey, date, d, var))
        .collect(Collectors.toList());
      return a;
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

  /**
   *
   * @param gribFile
   */
  public void download(GribFile gribFile) {
    URL gribUrl = this.getGribUrl(gribFile);
    this.createGribFileParent(gribFile);
    if (!gribFile.exists()) {
      this.doDownload(gribUrl, gribFile);
    }
  }

  /**
   *
   * @param gribFile
   * @return
   */
  private URL getGribUrl(GribFile gribFile) {
    String urlText = this.createUrl(gribFile);
    URL gribUrl = this.toUrlObject(urlText);
    return gribUrl;
  }

  /**
   *
   * @param gribFile
   * @param gribUrl
   * @throws RuntimeException
   */
  private void doDownload(URL gribUrl, GribFile gribFile) {
    System.out.println("Importing file : " + gribFile.grib);
    try (OutputStream output = gribFile.getOutputStream()) {
      IOUtils.copy(gribUrl.openStream(), output);
    } catch (Exception ex) {
      gribFile.delete();
      throw new RuntimeException("Error on copying stream.  Check args:{"
        + "output file : " + gribFile
        + ", connection : " + gribUrl
        + "}", ex);
    }
  }

  private void createGribFileParent(GribFile gribFile) {
    if (!gribFile.grib.getParentFile().exists()) {
      gribFile.grib.getParentFile().mkdirs();
    }
  }

  /**
   *
   * @param urlText
   * @return
   * @throws RuntimeException
   */
  private URL toUrlObject(String urlText) {
    URL gribUrl;
    try {
      gribUrl = new URL(urlText);
    } catch (MalformedURLException ex) {
      throw new RuntimeException(ex);
    }
    return gribUrl;
  }

  /**
   * https://www.ncei.noaa.gov/data/north-american-mesoscale-model/access/forecast/200710/20071020/
   * https://nomads.ncep.noaa.gov/pub/data/nccf/com/nam/prod/nam.20230106/
   *
   * @param filename
   * @return
   */
  private String createUrl(GribFile gribFile) {
    String dateTxt = gribFile.datetimeref
      .toOffsetDateTime().atZoneSameInstant(ZoneId.of("UTC"))
      .format(new DateTimeFormatterBuilder().appendPattern("yyyyMMdd").toFormatter());
    String result = String.format("%snam.%s/%s",
      this.url, dateTxt, gribFile.getBaseFileName());
    return result;
  }

}
