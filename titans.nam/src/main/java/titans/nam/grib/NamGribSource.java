package titans.nam.grib;

import common.http.RmHttpReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.IOUtils;
import titans.nam.NamParameter;

/**
 *
 * @author Ricardo Marquez
 */
public class NamGribSource {

  private final String url = "https://nomads.ncep.noaa.gov/pub/data/nccf/com/nam/prod/";

  public List<NamParameter> getCurrentNamParameters(String parentKey) {
    DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyyMMdd").toFormatter();
    ZonedDateTime date = ZonedDateTime.now(ZoneId.of("UTC"))
      .minusDays(1l)
      .truncatedTo(ChronoUnit.DAYS);
    String datetext = date
      .format(formatter);
    String fullUrl = this.url + "nam." + datetext;
    List<NamParameter> result = new ArrayList<>();
    new RmHttpReader.Builder(fullUrl).read((text) -> {
      Arrays.stream(text.split("\n")) //
        .filter(this::isConusNestLine)
        .map(this::toForecastTimeRef)
        .map(d -> new NamParameter(parentKey, date, d))
        .forEach(result::add);
    });
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
    String trimmed = r.replaceAll(".*?tm", "").replace(".grib2", "");
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
    String urlText = this.createUrl(gribFile);
    URL gribUrl = this.toUrlObject(urlText);
    if (!gribFile.exists()) {
      System.out.println("Importing file : " + gribFile);
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
