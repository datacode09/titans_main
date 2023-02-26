package titans.noaa.core;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.apache.commons.io.IOUtils;
import titans.nam.NoaaParameter;
import titans.noaa.grib.GribFile;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class NoaaGribSource {
  
  /**
   *
   * @param gribFile
   */
  public final void download(GribFile gribFile) {
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
      InputStream inputStream = gribUrl.openStream();
      IOUtils.copy(inputStream, output);
    } catch (Exception ex) {
      gribFile.delete();
      throw new RuntimeException("Error on copying stream.  Check args:{"
        + "output file : " + gribFile
        + ", connection : " + gribUrl
        + "}", ex);
    } 
    this.onPostDownLoad(gribFile);
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
   * 
   * @param gribFile
   * @return 
   */
  public abstract String createUrl(GribFile gribFile);
  
  
  /**
   * 
   * @param parentKey
   * @param minusDays
   * @return 
   */
  public abstract List<NoaaParameter> getParameters(String parentKey, int minusDays); 

  /**
   * 
   * @param gribFile 
   */
  protected void onPostDownLoad(GribFile gribFile) {
  }
}
