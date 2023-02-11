package titans.hrrr.core;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatterBuilder;
import rm.titansdata.raster.RasterObj;
import titans.hrrr.core.grib.HrrrGribSource;
import titans.nam.core.NoaaVariable;
import titans.nam.grib.GribFile;
import titans.nam.grib.NetCdfExtractor;
import titans.nam.netcdf.NetCdfFile;
import titans.nam.netcdf.NetCdfRaster;

/**
 *
 * @author Ricardo Marquez
 */
public class HrrrImporter implements Closeable {
  
  private final NetCdfRaster rasterLoader = new NetCdfRaster();
  private final HrrrGribSource source = new HrrrGribSource();
  private final File gribRootFolder;
  private final File netCdfRootFolder;
  private final File degribExe;
  

  /**
   *
   * @param gribRootFolder
   */
  public HrrrImporter(File gribRootFolder, File netCdfRootFolder, File degribExe) {
    this.gribRootFolder = gribRootFolder;
    this.netCdfRootFolder = netCdfRootFolder;
    this.degribExe = degribExe;
  }

  /**
   *
   * @param forecaststep
   * @param datetimeref
   * @return
   */
  public RasterObj getRaster(NoaaVariable var, ZonedDateTime datetimeref, int forecaststep) {
    NetCdfFile netCdfFile;
    NetCdfExtractor extractor = new NetCdfExtractor(this.degribExe, this.netCdfRootFolder, var);
    if (!extractor.netCdfFileExists(datetimeref, forecaststep)) {
      netCdfFile = this.downloadAndExtract(extractor, datetimeref, forecaststep);
    } else {
      netCdfFile = extractor.getNetCdfFile(datetimeref, forecaststep);
    }
    RasterObj result = this.rasterLoader.getRaster(netCdfFile);
    return result;
  }

  /**
   *
   * @param extractor
   * @param datetimeref
   * @param forecaststep
   * @return
   */
  private NetCdfFile downloadAndExtract(NetCdfExtractor extractor, ZonedDateTime datetimeref, int forecaststep) {
    NetCdfFile netCdfFile;
    GribFile gribFile = this.downloadGribFile(datetimeref, forecaststep);
    netCdfFile = extractor.extract(gribFile);
    return netCdfFile;
  }

  /**
   *
   * @param forecaststep
   * @param datetimeref
   * @return
   */
  private GribFile downloadGribFile(ZonedDateTime datetimeref, int forecaststep) {
    GribFile gribFile = this.getGribFile(datetimeref, forecaststep);
    if (gribFile.notExists()) {
      this.source.download(gribFile);
    }
    return gribFile;
  }

  /**
   *
   * @param forecaststep
   * @param datetimeref
   * @return
   */
  public GribFile getGribFile(ZonedDateTime datetimeref, int forecaststep) {
    String filename = this.getGribFileName(forecaststep, datetimeref);
    File grib = new File(this.gribRootFolder, filename);
    File gribIdx = new File(this.gribRootFolder, filename + ".idx");
    GribFile result = new GribFile(datetimeref, forecaststep, grib, gribIdx);
    return result;
  }

  /**
   *
   * @param forecaststep
   * @param datetimeref
   * @return
   */
  private String getGribFileName(int fcstHour, ZonedDateTime datetimeref) {
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
   * @throws IOException
   */
  @Override
  public void close() throws IOException {
    this.rasterLoader.close();
  }
}
