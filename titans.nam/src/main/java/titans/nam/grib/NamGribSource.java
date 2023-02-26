package titans.nam.grib;

import java.time.ZoneId;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import titans.nam.NoaaParameter;
import titans.noaa.core.NoaaGribSource;
import titans.noaa.grib.GribFile;

/**
 *
 * @author Ricardo Marquez
 */
public class NamGribSource extends NoaaGribSource{
  
  private final String url = "https://nomads.ncep.noaa.gov/pub/data/nccf/com/nam/prod/";
  
  /**
   * https://noaa-mrms-pds.s3.amazonaws.com/CONUS/BrightBandBottomHeight_00.00/20220722/MRMS_BrightBandBottomHeight_00.00_20220722-000000.grib2.gz
   * @param filename
   * @return
   */
  @Override
  public String createUrl(GribFile gribFile) {
    String baseFileName = gribFile.getBaseFileName();
    String dateTxt = gribFile.datetimeref
      .toOffsetDateTime().atZoneSameInstant(ZoneId.of("UTC"))
      .format(new DateTimeFormatterBuilder().appendPattern("yyyyMMdd").toFormatter());
    
    String result = String.format("%snam.%s/%s",
      this.url, dateTxt, baseFileName);
    return result;
  }
    
  /**
   * 
   * @param parentKey
   * @param minusDays
   * @return 
   */
  @Override
  public List<NoaaParameter> getParameters(String parentKey, int minusDays) {
    NamGribNameScraper scraper = new NamGribNameScraper();
    List<NoaaParameter> noaaParams = scraper.getParameters(this.url, parentKey, minusDays);
    return noaaParams;
  }

}
