package titans.hrrr;

import java.io.File;
import org.apache.commons.lang.math.DoubleRange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import rm.titansdata.colormap.ColorMap;
import rm.titansdata.plugin.ColorMapProvider;
import titans.nam.NoaaParameter;
import titans.nam.netcdf.NetCdfFile;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@DependsOn(value = "hrrrRasterFactory")
public class HrrrColorMapProvider implements ColorMapProvider {

  private final File baseNetCdfdir;
  
  /**
   * 
   * @param baseNetCdfdir 
   */
  public HrrrColorMapProvider(@Qualifier(value = "hrrr.netCdfRootFolder") File baseNetCdfdir) {
    this.baseNetCdfdir = baseNetCdfdir;
  }
  
  /**
   * 
   * @param param
   * @return 
   */
  @Override
  public ColorMap getColorMap(Parameter param) {
    ColorMap result;
    if (param instanceof NoaaParameter) {
      result = this.getColorMapFromFile((NoaaParameter) param);
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
  private ColorMap getColorMapFromFile(NoaaParameter namParameter) {
    NetCdfFile f = NetCdfFile.create(this.baseNetCdfdir, namParameter);
    DoubleRange r = f.getValueRange();
    ColorMap result = new ColorMap.Builder()
      .setXmin(r.getMinimumDouble())
      .setXmax(r.getMaximumDouble())
      .setColorMin("#000")
      .setColorMax("#fff")
      .build();
    return result;
  }

}
