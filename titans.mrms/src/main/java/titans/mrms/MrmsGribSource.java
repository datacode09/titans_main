package titans.mrms;

import java.io.File;
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
public class MrmsGribSource extends NoaaGribSource {

  String baseUrl = "https://noaa-mrms-pds.s3.amazonaws.com/CONUS/";

  public MrmsGribSource() {
  }
  
  /**
   * https://noaa-mrms-pds.s3.amazonaws.com/CONUS/MultiSensor_QPE_01H_Pass2_00.00/20230103/MRMS_MultiSensor_QPE_01H_Pass2_00.00_20230103-000000.grib2.gz
   * https://noaa-mrms-pds.s3.amazonaws.com/CONUS/MultiSensor_QPE_01H_Pass2_00.00/20230103/MultiSensor_QPE_01H_Pass2_00.00_20230103-100000.grib2.gz
   * @param gribFile
   * @return 
   */
  @Override
  public String createUrl(GribFile gribFile) {
    String baseFileName = gribFile.getBaseFileName();
    String dateTxt = gribFile.datetimeref
      .toOffsetDateTime().atZoneSameInstant(ZoneId.of("UTC"))
      .format(new DateTimeFormatterBuilder().appendPattern("yyyyMMdd").toFormatter());
    String result = String.format("%s%s/%s/%s",
      this.baseUrl, gribFile.var.getGribVarName(), dateTxt, baseFileName);
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
   throw new UnsupportedOperationException();
  }
  
  /**
   * 
   * @param gribFile 
   */
  @Override
  protected GribFile onPostDownLoad(GribFile gribFile) {
    gribFile.grib.getAbsoluteFile();
    File source = gribFile.grib.getAbsoluteFile();
    File target = new File(source.getParentFile(), source.getName().replace(".gz", ""));
    DecompressGzFile.decompressGzip(source, target);
    source.delete();
    GribFile result = new GribFile(gribFile.datetimeref, gribFile.fcststep, gribFile.var, target, gribFile.gribIdx);
    return result;
  }
  
  

}
