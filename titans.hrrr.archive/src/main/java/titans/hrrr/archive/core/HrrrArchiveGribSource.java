package titans.hrrr.archive.core;

import java.time.ZoneId;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import titans.nam.NoaaParameter;
import titans.noaa.core.NoaaGribSource;
import titans.noaa.grib.GribFile;

/**
 * https://noaa-hrrr-bdp-pds.s3.amazonaws.com/hrrr.20140807/conus/hrrr.t06z.wrfsfcf06.grib2
 * @author Ricardo Marquez
 */
public class HrrrArchiveGribSource extends NoaaGribSource {
  
  private final String url = "https://noaa-hrrr-bdp-pds.s3.amazonaws.com/";
  
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
   throw new UnsupportedOperationException();
  }  
}
