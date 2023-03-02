package titans.goes;

import java.io.File;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import titans.noaa.core.NoaaNetCdfImporter;

/**
 *
 * @author Ricardo Marquez
 */
class Goes18Importer extends NoaaNetCdfImporter {

  /**
   *
   * @param netCdfRootFolder
   * @param subfolderId
   */
  public Goes18Importer(File netCdfRootFolder, int subfolderId) {
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
//    String s = "OR_ABI-L1b-RadC-M6C01_G18_s20230020101172_e20230020103548_c20230020103594.nc";
    List<Content> bucket = this.getBucketList(varName, datetimeref, forecaststep);
    bucket.sort((o1, o2) -> Duration.between(o1.endTime(), datetimeref).compareTo(Duration.between(o2.endTime(), datetimeref)));
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
    String year = String.format("%4d", datetimeref.getYear());
    String doy = String.format("%3d", datetimeref.getDayOfYear());
    String hour = String.format("%2d", datetimeref.getHour());
    String result = String.format("%s/%s/%s/%s", var, year, doy, hour); //ABI-L1b-RadC/2023/002/01/";
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
    String channel = parts[1];
    String subfilename = String.format("OR_%s-%s_G18", var, channel);
    
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
