package titans.nam.netcdf;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import javax.measure.unit.Unit;
import titans.nam.NoaaParameter;
import titans.nam.core.NoaaVariable;
import titans.nam.grib.ForecastTimeReference;

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
    int year = this.datetime.getYear();
    int month = this.datetime.getMonthValue();
    int day = this.datetime.getDayOfMonth();
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
      .appendPattern("yyyyMMddHHmm")
      .toFormatter();
    String format = this.datetime.format(formatter);
    String filename = String.format("%s_%s_%03d.nc", var.getGribVarName(), format, this.fcststep);
    String child = String.format("%04d/%02d/%02d/%s", year, month, day, filename);
    String subfolder = String.format("%04d", this.subFolderId);
    File baseFolder1 = new File(baseFolder, subfolder);
    File result = new File(baseFolder1, child);
    return result;
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
