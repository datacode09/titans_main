package titans.hrrr;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.beans.property.ListProperty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import titans.hrrr.core.grib.HrrrSfcInventoryReader;
import titans.nam.NoaaFcstParameterFactory;
import titans.nam.NoaaParameter;
import titans.noaa.core.FcstDateRange;
import titans.noaa.core.InventoryReader;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class HrrrParametersFactory extends NoaaFcstParameterFactory {

  private final ListProperty<NoaaParameter> parameters;
  
  /**
   * 
   * @param parameters 
   */
  public HrrrParametersFactory( 
    @Qualifier("hrrr.parameters") ListProperty<NoaaParameter> parameters) {
    this.parameters = parameters;
  }
  
  
  
  /**
   *
   * @return
   */
  @Override
  public String key() {
    return "High Resolution Rapid Refresh";
  }

  @Override
  protected InventoryReader getInventoryReader() {
    HrrrSfcInventoryReader result = new HrrrSfcInventoryReader();
    return result;
  }
  
  /**
   * 
   * @param zonedDateTime
   * @param fcststep
   * @return 
   */
  @Override
  protected List<Parameter> getParameters(FcstDateRange range) {
    List<Parameter> result;
    int fcststep= range.fcststep;
    ZonedDateTime zonedDateTime = range.datetime1;
    if (fcststep == -1) {
      result = this.parameters.get().stream()
        .filter(p->p.datetime.equals(zonedDateTime))
        .collect(Collectors.toList()); 
    } else {
      result = this.parameters.get().stream()
        .filter(p->p.datetime.equals(zonedDateTime) && Objects.equals(p.fcststep, fcststep))
        .collect(Collectors.toList());
    }
    return result;
  }
}
