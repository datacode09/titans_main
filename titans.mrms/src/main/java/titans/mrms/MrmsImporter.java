package titans.mrms;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatterBuilder;
import titans.noaa.core.NoaaGribSource;
import titans.noaa.core.NoaaGribImporter;
import titans.noaa.core.NoaaVariable;

/**
 *
 * @author Ricardo Marquez
 */
public class MrmsImporter extends NoaaGribImporter {
  
  public MrmsImporter(File gribRootFolder, File netCdfRootFolder, int subfolderId, File degribExe) {
    super(gribRootFolder, netCdfRootFolder, subfolderId, degribExe);
  }

  /**
   * 
   * @param datetimeref
   * @param fcstHour
   * @return 
   */
  @Override
  protected String onGetGribFileName(NoaaVariable var, ZonedDateTime datetimeref, int fcstHour) {
    String datetext = datetimeref //
      .toOffsetDateTime() //
      .atZoneSameInstant(ZoneId.of("UTC")) //
      .format(new DateTimeFormatterBuilder()
        .appendPattern("yyyyMMdd-HHmmss")
        .toFormatter());
    String filename = String.format("MRMS_%s_%s.grib2.gz", new Object[]{
      var.getGribVarName(), datetext});    
    return filename;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  protected NoaaGribSource getGribSource() {
    return new MrmsGribSource();
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
    public MrmsImporter build() {
      return new MrmsImporter(gribRootFolder, netCdfRootFolder, subfolderId, degribExe); 
    }
  }
  
}
