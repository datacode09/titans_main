package titans.hrrr;

import javafx.beans.property.ListProperty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import titans.hrrr.core.grib.HrrrInventoryReader;
import titans.nam.NoaaParameter;
import titans.nam.NoaaFcstParameterFactory;
import titans.nam.core.InventoryReader;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class HrrrParametersFactory extends NoaaFcstParameterFactory {

  public HrrrParametersFactory(
    @Qualifier("hrrr.parameters") ListProperty<NoaaParameter> parameters) {
    super(parameters);
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
    HrrrInventoryReader result = new HrrrInventoryReader();
    return result;
  }
  
  
}
