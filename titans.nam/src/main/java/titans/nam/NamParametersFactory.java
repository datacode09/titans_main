package titans.nam;

import titans.noaa.core.InventoryReader;
import javafx.beans.property.ListProperty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import titans.nam.core.NamInventoryReader;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class NamParametersFactory extends NoaaFcstParameterFactory {

  
  public NamParametersFactory(
    @Qualifier("nam.parameters") ListProperty<NoaaParameter> parameters){
    super(parameters);
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

  
}
