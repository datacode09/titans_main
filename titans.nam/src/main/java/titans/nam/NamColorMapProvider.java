package titans.nam;

import java.io.File;
import org.apache.commons.lang.math.DoubleRange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import rm.titansdata.colormap.ColorMap;
import rm.titansdata.plugin.ColorMapProvider;
import titans.noaa.netcdf.NetCdfFile;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@DependsOn(value = "namRasterFactory")
public class NamColorMapProvider implements ColorMapProvider {

  private final File baseNetCdfdir;
  
  /**
   * 
   * @param baseNetCdfdir 
   */
  public NamColorMapProvider(@Qualifier(value = "nam.netCdfRootFolder") File baseNetCdfdir) {
    this.baseNetCdfdir = baseNetCdfdir;
  }
  
  

  @Override
  public ColorMap getColorMap(int projectId, Parameter param) {
    ColorMap result; 
    if (param instanceof NoaaParameter) {
      result = this.getColorMapFromFile(projectId, (NoaaParameter) param);
    } else {
      throw new RuntimeException();
    }
    return result;
  }
  
  /**
   * 
   * @param namParameter
   * @return 
   */
  private ColorMap getColorMapFromFile(int projectId, NoaaParameter namParameter) {
    NetCdfFile f = NetCdfFile.create(this.baseNetCdfdir, projectId,  namParameter);
    DoubleRange r= f.getValueRange();
    ColorMap result = new ColorMap.Builder()
      .setXmin(r.getMinimumDouble())
      .setXmax(r.getMaximumDouble())
      .setColorMin("#000")
      .setColorMax("#fff")
      .build();
    return result;
  }
  
}
