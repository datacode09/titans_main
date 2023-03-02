package titans.noaa.core;

import common.http.RmHttpReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import rm.titansdata.raster.RasterObj;
import titans.nam.NoaaParameter;
import titans.noaa.netcdf.NetCdfFile;
import titans.noaa.netcdf.NetCdfRaster;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class NoaaNetCdfImporter implements Closeable, NoaaImporter {
  private final NetCdfRaster rasterLoader = new NetCdfRaster();
  private final File netCdfRootFolder;
  private final int subfolderId;

  public NoaaNetCdfImporter(File netCdfRootFolder, int subfolderId) {
    this.netCdfRootFolder = netCdfRootFolder;
    this.subfolderId = subfolderId;
  }
    /**
   *
   * @param forecaststep
   * @param datetimeref
   * @return
   */
  @Override
  public final RasterObj getRaster(NoaaVariable var, ZonedDateTime datetimeref, int forecaststep) {
    NoaaParameter param = new NoaaParameter("GOES-18", datetimeref, forecaststep, var.getGribVarName(), var.getUnit());
    NetCdfFile netCdfFile = NetCdfFile.create(netCdfRootFolder, this.subfolderId, param);
    if (!netCdfFile.exists()) {
      this.downloadAndExtract(netCdfFile, datetimeref, forecaststep); 
    } 
    RasterObj result = this.rasterLoader.getRaster(netCdfFile);
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
   * @throws IOException
   */
  @Override
  public final void close() throws IOException {
    this.rasterLoader.close();
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
