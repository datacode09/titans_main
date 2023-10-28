package titans.noaa.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.locationtech.jts.geom.Point;
import rm.titansdata.properties.Bounds;
import titans.noaa.grib.GribFile;

/**
 *
 * @author Ricardo Marquez
 */
public class GribCropper {

  private final File degribExe;

  public GribCropper(File degribExe) {
    this.degribExe = degribExe;
  }

  /**
   *
   * @param gribFile
   * @param subfolderId
   * @param bounds
   * @return
   */
  public boolean crop(GribFile gribFile, int subfolderId, Bounds bounds) {
    File tempgrib = this.getTempGribFile(gribFile);
    this.doCrop(bounds, gribFile, tempgrib);
    if (!tempgrib.exists()) {
      return false;
    }
    GribFile target = this.copyTempFileToSubFolder(gribFile, subfolderId, tempgrib);
    tempgrib.delete();
    boolean result = target.exists();
    return result;
  }
  
  /**
   * 
   * @param gribFile
   * @return 
   */
  private File getTempGribFile(GribFile gribFile) {
    String basefilename = gribFile.getBaseFileName().replace(".", "-");
    String tempgribfilename = String.format("cropped_%s.grb", basefilename);
    File tempgrib = new File(gribFile.grib.getParent(), tempgribfilename);
    return tempgrib;
  }

  /**
   * 
   * @param gribFile
   * @param subfolderId
   * @param tempgrib
   * @return 
   */
  private GribFile copyTempFileToSubFolder(GribFile gribFile, int subfolderId, File tempgrib) {
    GribFile target = gribFile.setSubPath(subfolderId);
    if (!target.exists()) {
      target.createFile();
    }
    try (OutputStream outstream = new FileOutputStream(target.grib); //
      FileInputStream instream = new FileInputStream(tempgrib)) {
      IOUtils.copy(instream, outstream);
    } catch(Exception ex) {
      throw new RuntimeException(ex);
    }
    return target;
  }
  
  /**
   * 
   * @param bounds
   * @param gribFile
   * @param tempgribfilename
   * @throws RuntimeException 
   */
  private void doCrop(Bounds bounds, GribFile gribFile, File tempgrib) {
    Bounds transformed = bounds.transform(4326);
    Point lwlf = transformed.lowerleft();
    Point uprt = transformed.upperright();
    ProcessBuilder result = new ProcessBuilder(
      degribExe.getAbsolutePath().replace(".exe", ""),
      gribFile.grib.getAbsolutePath().replace(".gz", ""),
      "-C", "-msg", "0", "-Grib2",
      "-lwlf", String.format("%f,%f", lwlf.getY(), lwlf.getX()),
      "-uprt", String.format("%f,%f", uprt.getY(), uprt.getX()),
      "-out", tempgrib.getAbsolutePath()
    );
    File workingDirectory = gribFile.grib.getParentFile();
    result.directory(workingDirectory);
    result.redirectErrorStream(true);
    try {
      Process p = result.start();
      InputStream in = p.getInputStream();
      List<String> lines = toListOfLines(in);
      printLines(lines);
      InputStream errorstream = p.getErrorStream();
      String errorMsg;
      if (errorstream != null && !(errorMsg = toMessage(errorstream)).isEmpty()) {
        throw new RuntimeException(errorMsg);
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   *
   * @param lines
   */
  private void printLines(List<String> lines) {
    if (lines != null) {
      for (String string : lines) {
        System.out.println("process output:" + string);
      }
    }
  }

  /**
   *
   * @param in
   * @return
   * @throws IOException
   */
  private static List<String> toListOfLines(InputStream in) throws IOException {
    return IOUtils.readLines(in, Charset.forName("utf8"));
  }

  private static String toMessage(InputStream errorstream) throws IOException {
    return String.join("\n", toListOfLines(errorstream));
  }

  
}
