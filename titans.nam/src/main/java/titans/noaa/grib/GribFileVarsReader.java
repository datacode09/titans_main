package titans.noaa.grib;

import common.RmExceptions;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;
import org.apache.commons.io.IOUtils;
import rm.titansdata.units.UnitsUtils;

/**
 *
 * @author Ricardo Marquez
 */
public class GribFileVarsReader {

  private final File degribExe;
  private final File gribFile;

  /**
   *
   * @param gribFile
   */
  public GribFileVarsReader(File degribExe, File gribFile) {
    this.degribExe = degribExe;
    this.gribFile = gribFile;
  }

  /**
   *
   */
  public Set<String> parseVarNames() {
    ProcessBuilder process = this.createGribInventoryProcess();
    List<String> processOutput = this.getProcessOutput(process);
    Set<String> result = this.mapProcessOutputToVarNames(processOutput);
    return result;
  }

  /**
   *
   * @param varName
   * @return
   */
  public String getVarMsgNumber(String varName) {
    String line = this.getLineForVarName(varName);
    String result = this.lineToVarMsgNumber(line);
    return result;
  }

  /**
   *
   * @param varName
   * @return
   */
  private String getLineForVarName(String varName) {
    ProcessBuilder process = this.createGribInventoryProcess();
    List<String> processOutput = this.getProcessOutput(process);
    String line;
    if (processOutput.size() > 1) {
      line = processOutput.stream().filter(l -> this.toVarName(l).equals(varName))
      .findFirst().orElse(null);
    } else {
      line = processOutput.get(0); 
    }
    return line;
  }

  /**
   *
   * @param varName
   * @return
   */
  public Unit<? extends Quantity> getUnit(String varName) {
    String line = this.getLineForVarName(varName);
    String description = line.split(",")[3];
    int istart = description.indexOf("[");
    int iend = description.indexOf("]");
    Unit<?> result;
    if (istart >= 0 && iend >= 0) {
      String unitstext = description.substring(istart + 1, iend);
      result = UnitsUtils.valueOf(unitstext);
      if (result == null) {
        System.out.println("varName, unitstext = " + varName + ", " + unitstext);
        Unit<?> a = UnitsUtils.valueOf(unitstext);
      }
    } else {
      result = null;
    }
    return result;
  }

  /**
   *
   * @param processOutput
   * @return
   */
  private Set<String> mapProcessOutputToVarNames(List<String> processOutput) {
    Set<String> resutl = processOutput.stream()
      .map(this::toVarName)
      .collect(Collectors.toSet());
    return resutl;
  }

  /**
   *
   * @param line
   * @return
   */
  private String toVarName(String line) {
    String result = null;
    try {
      String[] parts = line.split(",");
      String elementPrefix = parts[3].split("=")[0].trim();
      String level = parts[4].trim();
      result = elementPrefix + "_" + level;
    } catch (Exception ex) {
      throw RmExceptions.create(ex, "An error occured on parsing line: '%s'", line);
    }
    return result;
  }

  /**
   *
   * @return
   */
  private ProcessBuilder createGribInventoryProcess() {
    ProcessBuilder process = new ProcessBuilder(
      this.degribExe.getAbsolutePath().replace(".exe", ""),
      this.gribFile.getAbsolutePath().replace(".gz", ""),
      "-I"
    );
    File workingDirectory = gribFile.getParentFile();
    process.directory(workingDirectory);
    process.redirectErrorStream(true);
    return process;
  }

  /**
   *
   * @param process
   * @throws RuntimeException
   */
  private List<String> getProcessOutput(ProcessBuilder process) {
    List<String> lines;
    try {
      Process p = process.start();
      InputStream errorstream = p.getErrorStream();
      String message;
      if (errorstream != null && !(message = this.streamToString(errorstream)).isEmpty()) {
        throw new RuntimeException(message);
      }
      InputStream in = p.getInputStream();
      lines = IOUtils.readLines(in, Charset.forName("utf8"));
      lines.stream().filter(l->l.contains("Error")).findFirst().ifPresent((errorline)->{
        throw new RuntimeException(errorline); 
      });   
      lines.remove(0);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    return lines;
  }

  /**
   *
   * @param in
   * @return
   * @throws IOException
   */
  private String streamToString(InputStream in) throws IOException {
    List<String> lines = IOUtils.readLines(in, Charset.forName("utf8"));
    String result = String.join(",", lines);
    return result;
  }

  /**
   *
   * @param line
   * @return
   */
  private String lineToVarMsgNumber(String line) {
    try {
      String result = line.split(",")[0];
      return result;
    } catch (Exception ex) {
      throw new RuntimeException(String.format("Parsing line ''%s", line), ex);
    }

  }

}
