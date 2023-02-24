package titans.hrrr.core.grib;

import java.time.ZoneId;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
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
   * @param filename
   * @return
   */
  @Override
  public String createUrl(GribFile gribFile) {
    String baseFileName = gribFile.getBaseFileName();
    String dateTxt = gribFile.datetimeref
      .toOffsetDateTime().atZoneSameInstant(ZoneId.of("UTC"))
      .format(new DateTimeFormatterBuilder().appendPattern("yyyyMMdd").toFormatter());
    String result = String.format("%shrrr.%s/conus/%s",
      this.url, dateTxt, baseFileName);
    return result;
  }

  /**
   *
   * @param parentKey
   * @return
   */
  @Override
  public List<NoaaParameter> getParameters(String parentKey, int minusDays) {
    HrrrGribNameScraper scraper = new HrrrGribNameScraper();
    List<NoaaParameter> noaaParams = scraper.getParameters(this.url, parentKey, minusDays);
    return noaaParams;
  }
}
