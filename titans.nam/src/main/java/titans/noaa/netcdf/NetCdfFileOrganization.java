package titans.noaa.netcdf;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import javax.measure.unit.Unit;
import titans.nam.NoaaParameter;
import titans.noaa.core.NoaaVariable;
import titans.noaa.grib.ForecastTimeReference;

/**
 *
 * @author Ricardo Marquez
 */
public class NetCdfFileOrganization {

  private final File baseFolder;
  private final int subFolderId;
  private final int fcststep;
  private final ZonedDateTime datetime;
  private final NoaaVariable var;

  /**
   *
   * @param baseFolder
   * @param fcststep
   * @param datetime
   * @param var
   */
  public NetCdfFileOrganization( //
    File baseFolder, int subFolderId, int fcststep, ZonedDateTime datetime, NoaaVariable var) {
    this.baseFolder = baseFolder;
    this.subFolderId = subFolderId;
    this.datetime = datetime;
    this.fcststep = fcststep;
    this.var = var;
  }

  /**
   *
   */
  public File getFile() {
    String subfilepath = this.getSubFilePath();
    File realBaseFolder = this.getBaseFolder();
    File result = new File(realBaseFolder, subfilepath);
    return result;
  }
  
  /**
   * 
   * @return 
   */
  private String getSubFilePath() {
    String filename = this.getFileName();
    int year = this.datetime.getYear();
    int month = this.datetime.getMonthValue();
    int day = this.datetime.getDayOfMonth();
    String child = String.format("%04d/%02d/%02d/%s", year, month, day, filename);
    return child;
  }
  
  /**
   * 
   * @return 
   */
  private String getFileName() {
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
      .appendPattern("yyyyMMddHHmm")
      .toFormatter();
    String format = this.datetime.format(formatter);
    String varName = var.getGribVarName();
    String filename = String.format("%s_%s_%03d.nc", varName, format, this.fcststep);
    return filename;
  }
  
  /**
   * 
   * @return 
   */
  private File getBaseFolder() {
    String subfolder = this.subFolderId < 0 ? "test" : String.format("%04d", this.subFolderId);
    File realBaseFolder = new File(baseFolder, subfolder);
    return realBaseFolder;
  }
    
  
  /**
   *
   * @return
   */
  public NetCdfFile getNetCdfFile() {
    String gribVarName = this.var.getGribVarName();
    ForecastTimeReference d = new ForecastTimeReference(0, fcststep);
    NoaaParameter namParameter = new NoaaParameter(gribVarName, datetime, d, gribVarName, Unit.ONE);
    NetCdfFile result = NetCdfFile.create(baseFolder, subFolderId, namParameter);
    return result;
  }
}
