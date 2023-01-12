package titans.nam.grib;

import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatterBuilder;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Ricardo Marquez
 */
public class NamGribSource {
  
  private final String url = "https://nomads.ncep.noaa.gov/pub/data/nccf/com/nam/prod/";
  
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
