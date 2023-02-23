package titans.hrrr.core.grib;

import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import org.apache.commons.io.IOUtils;
import titans.nam.NoaaParameter;
import titans.nam.core.NoaaGribSource;
import titans.nam.grib.GribFile;

/**
 *
 * @author Ricardo Marquez
 */
public class HrrrGribSource extends NoaaGribSource {

  private final String url = "https://nomads.ncep.noaa.gov/pub/data/nccf/com/hrrr/prod/";

  /**
   *
   * @param gribFile
   */
  public void download(GribFile gribFile) {
    String urlText = this.createUrl(gribFile);
    URL gribUrl = this.toUrlObject(urlText);
    if (!gribFile.grib.getParentFile().exists()) {
      gribFile.grib.getParentFile().mkdirs();
    }
    if (!gribFile.exists()) {
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
   *
   * @param filename
   * @return
   */
  private String createUrl(GribFile gribFile) {
    String dateTxt = gribFile.datetimeref
      .toOffsetDateTime().atZoneSameInstant(ZoneId.of("UTC"))
      .format(new DateTimeFormatterBuilder().appendPattern("yyyyMMdd").toFormatter());
    String result = String.format("%shrrr.%s/conus/%s",
      this.url, dateTxt, gribFile.getBaseFileName());
    return result;
  }

  /**
   *
   * @param parentKey
   * @return
   */
  public List<NoaaParameter> getCurrentParameters(String parentKey, int minusDays) {
    HrrrGribNameScraper scraper = new HrrrGribNameScraper();
    List<NoaaParameter> noaaParams = scraper.getParameters(this.url, parentKey, minusDays);
    return noaaParams;
  }
}
