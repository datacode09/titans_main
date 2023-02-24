package titans.hrrr.archive.core;

import java.io.File;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatterBuilder;
import titans.noaa.core.NoaaGribSource;
import titans.noaa.core.NoaaImporter;


/**
 *
 * @author Ricardo Marquez
 */
public class HrrrArchiveImporter extends NoaaImporter {

  public HrrrArchiveImporter(File gribRootFolder, File netCdfRootFolder, int subfolderId, File degribExe) {
    super(gribRootFolder, netCdfRootFolder, subfolderId, degribExe);
  }

  /**
   *
   * @param datetimeref
   * @param fcstHour
   * @return
   */
  @Override
  protected String onGetGribFileName(ZonedDateTime datetimeref, int fcstHour) {
    String hourtext = datetimeref //
      .toOffsetDateTime() //
      .atZoneSameInstant(ZoneId.of("UTC")) //
      .format(new DateTimeFormatterBuilder()
        .appendPattern("HH")
        .toFormatter());
    String datetext = datetimeref //
      .toOffsetDateTime() //
      .atZoneSameInstant(ZoneId.of("UTC")) //
      .format(new DateTimeFormatterBuilder()
        .appendPattern("yyyy\\MM\\dd")
        .toFormatter());
    DecimalFormat decimalFormat = new DecimalFormat("00");
    String fcstHourTxt = decimalFormat.format(fcstHour);
    String filename = String.format("%s\\hrrr.t%sz.wrfsfcf%s.grib2", new Object[]{
      datetext, hourtext, fcstHourTxt});
    return filename;
  }

  /**
   *
   * @return
   */
  @Override
  protected NoaaGribSource getGribSource() {
    HrrrArchiveGribSource result = new HrrrArchiveGribSource();
    return result;
  }
  
  /**
   * 
   */
  public static class Builder {

    private File gribRootFolder;
    private File netCdfRootFolder;
    private int subfolderId;
    private File degribExe;

    public Builder() {
    }

    public Builder setGribRootFolder(File gribRootFolder) {
      this.gribRootFolder = gribRootFolder;
      return this;
    }

    public Builder setNetCdfRootFolder(File netCdfRootFolder) {
      this.netCdfRootFolder = netCdfRootFolder;
      return this;
    }

    public Builder setSubfolderId(int subfolderId) {
      this.subfolderId = subfolderId;
      return this;
    }

    public Builder setDegribExe(File degribExe) {
      this.degribExe = degribExe;
      return this;
    }

    /**
     *
     * @return
     */
    public HrrrArchiveImporter build() {
      return new HrrrArchiveImporter(gribRootFolder, netCdfRootFolder, subfolderId, degribExe);
    }
  }
}
