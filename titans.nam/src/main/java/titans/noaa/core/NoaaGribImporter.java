package titans.noaa.core;

import java.io.File;
import java.time.ZonedDateTime;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.raster.RasterObj;
import titans.noaa.grib.GribFile;
import titans.noaa.grib.NetCdfExtractor;
import titans.noaa.netcdf.NetCdfFile;
import titans.noaa.netcdf.NetCdfRasterProvider;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class NoaaGribImporter implements NoaaImporter {
  
  private final NetCdfRasterProvider rasterLoader = new NetCdfRasterProvider();
  private final File gribRootFolder;
  private final File netCdfRootFolder;
  private final int subfolderId;
  private final File degribExe;

  /**
   *
   * @param gribRootFolder
   */
  public NoaaGribImporter(File gribRootFolder, File netCdfRootFolder, int subfolderId, File degribExe) {
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
  @Override
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
   * @param var
   * @param datetime
   * @param fcststep
   * @param bounds
   * @param dims
   * @return 
   */
  @Override
  public final RasterObj getRaster(NoaaVariable var, ZonedDateTime datetime, int fcststep, Bounds bounds, Dimensions dims) {
    NetCdfFile netCdfFile;
    NetCdfExtractor extractor = new NetCdfExtractor(this.degribExe, this.netCdfRootFolder, this.subfolderId, var); 
    if (!extractor.netCdfFileExists(datetime, fcststep)) {
      netCdfFile = this.downloadAndExtract(extractor, datetime, fcststep); 
    } else {
      netCdfFile = extractor.getNetCdfFile(datetime, fcststep);
    }
    RasterObj result = this.rasterLoader.getRaster(netCdfFile, bounds, dims);
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
    NoaaVariable var = extractor.getVar();
    GribFile gribFile = this.downloadGribFile(var, datetimeref, forecaststep);
    netCdfFile = extractor.extract(gribFile);
    return netCdfFile;
  }
  
  
  /**
   * 
   * @param forecaststep
   * @param datetimeref
   * @return 
   */
  private GribFile downloadGribFile(NoaaVariable var, ZonedDateTime datetimeref, int forecaststep) {
    GribFile gribFile = this.getGribFile(var, datetimeref, forecaststep);
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
  public final GribFile getGribFile(NoaaVariable var, ZonedDateTime datetimeref, int forecaststep) {
    String filename = this.getGribFileName(var, datetimeref, forecaststep);
    File grib = new File(this.gribRootFolder, filename);
    File gribIdx = new File(this.gribRootFolder, filename + ".idx");
    GribFile result = new GribFile(datetimeref, forecaststep, var, grib, gribIdx);
    return result;
  }

  /**
   *
   * @param forecaststep
   * @param datetimeref
   * @return
   */
  private String getGribFileName(NoaaVariable var, ZonedDateTime datetimeref, int fcstHour) {
    String filename = this.onGetGribFileName(var, datetimeref, fcstHour);
    return filename;
  }
  
  /**
   * 
   * @param datetimeref
   * @param fcstHour
   * @return 
   */
  protected abstract String onGetGribFileName(NoaaVariable var, ZonedDateTime datetimeref, int fcstHour); 

  
  /**
   * 
   * @return 
   */
  protected abstract NoaaGribSource getGribSource();  
}
