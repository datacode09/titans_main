package titans.nam.grib;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.io.IOUtils;
import titans.nam.netcdf.NetCdfFile;

/**
 *
 * @author Ricardo Marquez
 */
public class NetCdfExtractor {

  private final File degribExe;

  public NetCdfExtractor(File degribExe) {
    this.degribExe = degribExe;
  }
  
  /**
   * 
   * @param gribFile
   * @return 
   */
  public NetCdfFile extract(GribFile gribFile) {
//    String vId = this.getVariableId(var, fileIdx);
    ProcessBuilder process = new ProcessBuilder(
      this.degribExe.getAbsolutePath().replace(".exe", ""),
      gribFile.grib.getAbsolutePath(),
//      "-msg", vId,
      "-C", "-NetCDF", "3"
//      "-lwlf", this.getLowerLeftTxt(), "-uprt", this.getUpperRightTxt()
    );
    File workingDirectory = gribFile.grib.getParentFile();
    process.directory(workingDirectory);
    process.redirectErrorStream(true);
    try {
      Process p = process.start();
      InputStream in = p.getInputStream();
      List<String> lines = IOUtils.readLines(in, Charset.forName("utf8"));
      printLines(lines);
      InputStream errorstream = p.getErrorStream();
      if (errorstream != null) {
        printLines(IOUtils.readLines(in, Charset.forName("utf8")));
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    File file = new File(workingDirectory, "PRMSL_01120000.nc");
    String varName = "PRMSL_0_MSL";
    NetCdfFile netCdfFile = new NetCdfFile(file, varName);
    return netCdfFile;
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
}
