package titans.noaa.core;

import common.RmTimer;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import rm.titansdata.properties.Bounds;
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
  public final GribFile download(GribFile gribFile) {
    URL gribUrl = this.getGribUrl(gribFile);
    this.createGribFileParent(gribFile);
    GribFile result = gribFile;
    if (!gribFile.exists()) {
      result = this.doDownload(gribUrl, gribFile);
    }
    return result;
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
  private GribFile doDownload(URL gribUrl, GribFile gribFile) {
    RmTimer timer = RmTimer.start();
    try (OutputStream output = gribFile.getOutputStream()) {
      InputStream inputStream = gribUrl.openStream();
      System.out.println("downloading from " + gribUrl);

      int bytesRead;
      byte[] buffer = new byte[256000];
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        output.write(buffer, 0, bytesRead);
      }
    } catch (Exception ex) {
      gribFile.delete();
      throw new RuntimeException("Error on copying stream.  Check args:{"
        + "output file : " + gribFile
        + ", connection : " + gribUrl
        + "}", ex);
    }
    timer.endAndPrint();
    GribFile result = this.onPostDownLoad(gribFile);
    return result;
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
   * @param subfolderId
   * @param bounds
   * @return
   */
  boolean crop(File degribExe, GribFile gribFile, int subfolderId, Bounds bounds) {
    GribCropper cropper = new GribCropper(degribExe);
    boolean result = cropper.crop(gribFile, subfolderId, bounds);
    return result;
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
  protected GribFile onPostDownLoad(GribFile gribFile) {
    return gribFile;
  }

}
