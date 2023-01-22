package titans.nam.core;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatterBuilder;
import rm.titansdata.raster.RasterObj;
import titans.nam.grib.GribFile;
import titans.nam.grib.NamGribSource;
import titans.nam.grib.NetCdfExtractor;
import titans.nam.netcdf.NetCdfFile;
import titans.nam.netcdf.NetCdfRaster;

/**
 *
 * @author Ricardo Marquez
 */
public class NamImporter implements Closeable {

  private final NetCdfRaster rasterLoader = new NetCdfRaster();
  private final NamGribSource source = new NamGribSource();
  private final NetCdfExtractor extractor;
  private final File root;

  /**
   *
   * @param gribRootFolder
   */
  public NamImporter(File gribRootFolder, File degribExe) {
    this.root = gribRootFolder;
    this.extractor = new NetCdfExtractor(degribExe);
  }

  /**
   *
   * @param forecaststep
   * @param datetimeref
   * @return
   */
  public RasterObj getRaster(int forecaststep, ZonedDateTime datetimeref) {
    GribFile gribFile = this.getGribFile(forecaststep, datetimeref);
    if (gribFile.notExists()) {
      this.source.download(gribFile);
    }
    NetCdfFile netCdfFile = this.extractor.extract(gribFile);
    RasterObj result = this.rasterLoader.getRaster(netCdfFile);
    return result;
  }

  /**
   *
   * @param forecaststep
   * @param datetimeref
   * @return
   */
  private GribFile getGribFile(int forecaststep, ZonedDateTime datetimeref) {
    String filename = this.getGribFileName(forecaststep, datetimeref);
    File grib = new File(this.root, filename);
    File gribIdx = new File(this.root, filename + ".idx");
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
    String refdatetimetext = datetimeref //
      .toOffsetDateTime() //
      .atZoneSameInstant(ZoneId.of("UTC")) //
      .format(new DateTimeFormatterBuilder()
        .appendPattern("HH")
        .toFormatter());
    DecimalFormat decimalFormat = new DecimalFormat("00");
    String fcstHourTxt = decimalFormat.format(fcstHour);
    String filename = String.format("nam.t%sz.conusnest.hiresf%s.tm00.grib2", new Object[]{
      refdatetimetext, fcstHourTxt,});
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
