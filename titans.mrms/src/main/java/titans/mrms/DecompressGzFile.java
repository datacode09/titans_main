package titans.mrms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;


/**
 *
 * @author Ricardo Marquez
 */
public class DecompressGzFile {
  
  /**
   * 
   * @param source
   * @param target 
   */
  public static void decompressGzip(File source, File target) {
    try (GZIPInputStream gis = new GZIPInputStream(
      new FileInputStream(source));
      FileOutputStream fos = new FileOutputStream(target)) {
      byte[] buffer = new byte[1024];
      int len;
      while ((len = gis.read(buffer)) > 0) {
        fos.write(buffer, 0, len);
      }
    } catch(Exception ex) {
      throw new RuntimeException(ex); 
    }
  }
}
