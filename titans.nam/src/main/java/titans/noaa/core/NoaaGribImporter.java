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
   * @param var
   * @param datetime
   * @param fcststep
   * @param bounds
   * @param dims
   * @return 
   */
  @Override
  public final RasterObj getRaster(NoaaVariable var, // 
    ZonedDateTime datetime, int fcststep, Bounds bounds, Dimensions dims) {
    NetCdfFile netCdfFile;
    NetCdfExtractor extractor = new NetCdfExtractor(this.degribExe, this.netCdfRootFolder, this.subfolderId, var); 
    if (!extractor.netCdfFileExists(datetime, fcststep)) {
      netCdfFile = this.downloadAndExtract(extractor, bounds, datetime, fcststep);
    } else {
      netCdfFile = extractor.getNetCdfFile(datetime, fcststep);
    }
    RasterObj result;
    if (bounds == null) {
      result = this.rasterLoader.getRaster(netCdfFile);
    } else {
      result = this.rasterLoader.getRaster(netCdfFile, bounds, dims); 
    }
    
    return result;
  }
  
  
  
  /**
   * 
   * @param extractor
   * @param datetimeref
   * @param forecaststep
   * @return 
   */
  private NetCdfFile downloadAndExtract(NetCdfExtractor extractor, //
     Bounds bounds, ZonedDateTime datetimeref, int forecaststep) {
    NetCdfFile netCdfFile;
    NoaaVariable var = extractor.getVar();
    GribFile gribFile = this.downloadGribFile(var, bounds, datetimeref, forecaststep);
    netCdfFile = extractor.extract(gribFile);
    return netCdfFile;
  }
  
  
  /**
   * 
   * @param forecaststep
   * @param datetimeref
   * @return 
   */
  private GribFile downloadGribFile(NoaaVariable var, Bounds bounds, // 
    ZonedDateTime datetimeref, int forecaststep) {
    GribFile gribFile = this.getGribFile(var, datetimeref, forecaststep);
    GribFile subPathGribFile = gribFile.setSubPath(subfolderId);
    GribFile result;
    if (gribFile.exists()) {
      result = this.crop(gribFile, bounds, subPathGribFile);
    } else if (subPathGribFile.exists()){
      result = subPathGribFile;
    } else {
      NoaaGribSource source = this.getGribSource();
      result = source.download(gribFile);
      if (this.onIsGribCroppable()) {
        result = this.crop(result, bounds, subPathGribFile);
      }
      
    }
    return result;
  }
  
  
  /**
   * 
   * @param gribFile
   * @param bounds
   * @param subPathGribFile
   * @return 
   */
  private GribFile crop(GribFile gribFile, Bounds bounds, GribFile subPathGribFile) {
    NoaaGribSource source = this.getGribSource();
    GribFile result;
    boolean successful = source.crop(this.degribExe, gribFile, this.subfolderId, bounds);
    if (successful) {
      gribFile.delete();
      result = subPathGribFile;
    } else {
      result = gribFile;
    }
    return result;
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
  
  /**
   * 
   * @return 
   */
  protected boolean onIsGribCroppable(){
    return false;
  } 
}
