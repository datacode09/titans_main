package titans.noaa.core;

import common.http.RmHttpReader;
import java.io.File;
import java.io.InputStream;
import java.time.ZonedDateTime;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.raster.RasterObj;
import titans.nam.NoaaParameter;
import titans.noaa.netcdf.NetCdfFile;
import titans.noaa.netcdf.NetCdfRasterProvider;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class NoaaNetCdfImporter implements NoaaImporter {
  private final NetCdfRasterProvider rasterLoader = new NetCdfRasterProvider();
  private final File netCdfRootFolder;
  private final int subfolderId;

  public NoaaNetCdfImporter(File netCdfRootFolder, int subfolderId) {
    this.netCdfRootFolder = netCdfRootFolder;
    this.subfolderId = subfolderId;
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
  public final RasterObj getRaster(NoaaVariable var, ZonedDateTime datetime, // 
    int fcststep, Bounds bounds, Dimensions dims) {
    NoaaParameter param = new NoaaParameter("GOES-18", datetime, fcststep, var.getGribVarName(), var.getUnit());
    NetCdfFile netCdfFile = NetCdfFile.create(netCdfRootFolder, this.subfolderId, param);
    if (!netCdfFile.exists()) {
      this.downloadAndExtract(netCdfFile, datetime, fcststep); 
    } 
    RasterObj result = this.rasterLoader.getRaster(netCdfFile, bounds, dims);
    return result;
  }
  
  
  
  /**
   * 
   * @param netcdffile
   * @param datetimeref
   * @param forecaststep
   * @return 
   */
  private void downloadAndExtract(NetCdfFile netcdffile, ZonedDateTime datetimeref, int forecaststep) {
    String urlstring = this.onGetUrlString(netcdffile.getVarName(), datetimeref, forecaststep);
    InputStream inputStream = new RmHttpReader.Builder(urlstring).readStream();
    netcdffile.save(inputStream);
  }
  

  /**
   * 
   * @param varName
   * @param datetimeref
   * @param forecaststep
   * @return 
   */
  protected abstract String onGetUrlString(String varName, ZonedDateTime datetimeref, int forecaststep);
}
