package titans.hrrr;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import org.springframework.context.annotation.Bean;
import titans.nam.NoaaParameter;

/**
 *
 * @author Ricardo Marquez
 */
public class HrrrConfiguration {

  /**
   *
   * @return
   */
  @Bean("hrrr.parameters")
  public ListProperty<NoaaParameter> getParameters() {
    ListProperty<NoaaParameter> result = new SimpleListProperty<>();
    return result;
  }
}
