package titans.nam.netcdf;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import titans.nam.core.NoaaVariable;

/**
 *
 * @author Ricardo Marquez
 */
public class NetCdfFileOrganization {

  private final File baseFolder;
  private int fcststep;
  private ZonedDateTime datetime;
  private NoaaVariable var;

  /**
   *
   * @param baseFolder
   * @param fcststep
   * @param datetime
   * @param var
   */
  public NetCdfFileOrganization(File baseFolder, int fcststep, ZonedDateTime datetime, NoaaVariable var) {
    this.baseFolder = baseFolder;
    this.fcststep = fcststep;
    this.datetime = datetime;
    this.var = var;
  }

  /**
   *
   */
  public File getFile() {
    int year = this.datetime.getYear();
    int month = this.datetime.getMonthValue();
    int day = this.datetime.getDayOfMonth();
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
      .appendPattern("yyyyMMddHHmm")
      .toFormatter();
    String format = this.datetime.format(formatter);
    String filename = String.format("%s_%s_%03d.nc", var.getGribVarName(), format, this.fcststep);
    String child = String.format("%04d/%02d/%02d/%s", year, month, day, filename);
    File result = new File(baseFolder, child);
    return result;
  }
  
  /**
   * 
   * @return 
   */
  public NetCdfFile getNetCdfFile() {
    File file = this.getFile();
    String gribVarName = var.getGribVarName();
    NetCdfFile result = new NetCdfFile(gribVarName, file);
    return result;
  }
}
