package titans.noaa.core;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import rm.titansdata.raster.RasterObj;
import titans.noaa.grib.GribFile;
import titans.noaa.grib.NetCdfExtractor;
import titans.noaa.netcdf.NetCdfFile;
import titans.noaa.netcdf.NetCdfRaster;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class NoaaImporter implements Closeable {
  private final NetCdfRaster rasterLoader = new NetCdfRaster();
  private final File gribRootFolder;
  private final File netCdfRootFolder;
  private final int subfolderId;
  private final File degribExe;

  /**
   *
   * @param gribRootFolder
   */
  public NoaaImporter(File gribRootFolder, File netCdfRootFolder, int subfolderId, File degribExe) {
    this.gribRootFolder = gribRootFolder;
    this.netCdfRootFolder = netCdfRootFolder;
    this.subfolderId = subfolderId;
    this.degribExe = degribExe;
  }

  /**
   *
   * @param forecaststep
   * @param datetimeref
   * @return
   */
  public final RasterObj getRaster(NoaaVariable var, ZonedDateTime datetimeref, int forecaststep) {
    NetCdfFile netCdfFile;
    NetCdfExtractor extractor = new NetCdfExtractor(this.degribExe, this.netCdfRootFolder, this.subfolderId, var); 
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
      NoaaGribSource source = this.getGribSource();
      source.download(gribFile);
    }
    return gribFile;
  }

  /**
   *
   * @param forecaststep
   * @param datetimeref
   * @return
   */
  public final GribFile getGribFile(ZonedDateTime datetimeref, int forecaststep) {
    String filename = this.getGribFileName(datetimeref, forecaststep);
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
  private String getGribFileName(ZonedDateTime datetimeref, int fcstHour) {
    String filename = onGetGribFileName(datetimeref, fcstHour);
    return filename;
  }
  
  /**
   * 
   * @param datetimeref
   * @param fcstHour
   * @return 
   */
  protected abstract String onGetGribFileName(ZonedDateTime datetimeref, int fcstHour); 

  /**
   *
   * @throws IOException
   */
  @Override
  public final void close() throws IOException {
    this.rasterLoader.close();
  }

  /**
   * 
   * @return 
   */
  protected abstract NoaaGribSource getGribSource();  
}
