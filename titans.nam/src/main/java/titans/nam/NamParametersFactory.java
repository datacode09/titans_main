package titans.nam;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.beans.property.ListProperty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import titans.nam.core.NamInventoryReader;
import titans.noaa.core.FcstDateRange;
import titans.noaa.core.InventoryReader;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class NamParametersFactory extends NoaaFcstParameterFactory {

  private final ListProperty<NoaaParameter> parameters;

  /**
   * 
   * @param parameters 
   */
  public NamParametersFactory(
    @Qualifier("nam.parameters") ListProperty<NoaaParameter> parameters){
    this.parameters = parameters;
    
  }
  
  /**
   *
   * @return
   */
  @Override
  public String key() {
    return "North American Model Forecasts";
  }

  @Override
  protected InventoryReader getInventoryReader() {
    return new NamInventoryReader();
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
    int fcststep = range.fcststep;
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
