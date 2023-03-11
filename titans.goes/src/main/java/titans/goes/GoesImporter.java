package titans.goes;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.List;
import titans.noaa.core.NoaaNetCdfImporter;

/**
 *
 * @author Ricardo Marquez
 */
class GoesImporter extends NoaaNetCdfImporter {

  /**
   *
   * @param netCdfRootFolder
   * @param subfolderId
   */
  public GoesImporter(File netCdfRootFolder, int subfolderId) {
    super(netCdfRootFolder, subfolderId);
  }

  /**
   *
   * @param varName
   * @param datetimeref
   * @param forecaststep
   * @return
   */
  @Override
  protected String onGetUrlString(String varName, ZonedDateTime datetimeref, int forecaststep) {
    String baseUrl = "https://noaa-goes18.s3.amazonaws.com/";
    String subPath = this.getSubPath(varName, datetimeref, forecaststep);
    String fileName = this.getFileName(varName, datetimeref, forecaststep);
    String url = baseUrl + subPath + fileName;
    return url;
  }

  /**
   *
   * @return
   */
  private String getFileName(String varName, ZonedDateTime datetimeref, int forecaststep) {
    List<Content> bucket = this.getBucketList(varName, datetimeref, forecaststep);
    bucket.sort(Content.getComparator(datetimeref)::compareDates);
    Content content = bucket.stream().findFirst().orElseThrow(RuntimeException::new);
    String result = content.key();
    return result;
  }

  /**
   *
   * @param varName
   * @param datetimeref
   * @param forecaststep
   * @return
   */
  private String getSubPath(String varName, ZonedDateTime datetimeref, int forecaststep) {
    String[] parts = varName.split("\\$");
    String var = parts[0];
    String year = String.format("%04d", datetimeref.getYear());
    String doy = String.format("%03d", datetimeref.getDayOfYear());
    String hour = String.format("%02d", datetimeref.getHour());
    String result = String.format("%s/%s/%s/%s/", var, year, doy, hour);
    return result;
  }
  
  /**
   * 
   * @param varName
   * @param datetimeref
   * @param forecaststep
   * @return 
   */
  private List<Content> getBucketList(String varName, ZonedDateTime datetimeref, int forecaststep) {
    String[] parts = varName.split("\\$");
    String var = parts[0];
    String channel = parts.length == 1 ? "M6" : parts[1];
    BucketListReader reader = new BucketListReader();
    List<Content> result = reader.read(varName, datetimeref);
    String vartest = var + "-" + channel; 
    result.removeIf(c->!c.key().contains(vartest)); 
    return result;   
  }

}
