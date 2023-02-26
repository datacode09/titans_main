package titans.nam.core;

import java.io.Closeable;
import java.io.File;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatterBuilder;
import titans.nam.grib.NamGribSource;
import titans.noaa.core.NoaaGribSource;
import titans.noaa.core.NoaaImporter;
import titans.noaa.core.NoaaVariable;

/**
 *
 * @author Ricardo Marquez
 */
public class NamImporter extends NoaaImporter implements Closeable {
    
  /**
   * 
   * @param gribRootFolder
   * @param netCdfRootFolder
   * @param subfolderId
   * @param degribExe 
   */
  public NamImporter(File gribRootFolder, File netCdfRootFolder, int subfolderId, File degribExe) {
    super(gribRootFolder, netCdfRootFolder, subfolderId, degribExe);
  }
  
  /**
   * MRMS_BrightBandTopHeight_00.00_20220724-000400.grib2.gz
   * @param datetimeref
   * @param fcstHour
   * @return 
   */
  @Override
  protected String onGetGribFileName(NoaaVariable var, ZonedDateTime datetimeref, int fcstHour) {
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
    String filename = String.format("%s\\nam.t%sz.conusnest.hiresf%s.tm00.grib2", new Object[]{
      datetext, hourtext, fcstHourTxt});
    return filename;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  protected NoaaGribSource getGribSource() {
    return new NamGribSource();
  }
  
  
  

  
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
    public NamImporter build() {
      return new NamImporter(gribRootFolder, netCdfRootFolder, subfolderId, degribExe); 
    }
  }

}
