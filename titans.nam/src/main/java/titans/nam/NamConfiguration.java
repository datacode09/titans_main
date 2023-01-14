package titans.nam;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Ricardo Marquez
 */
@Configuration
public class NamConfiguration {
  
  /**
   * 
   * @return 
   */
  @Bean("nam.parameters")
  public ListProperty<NamParameter> getParameters() {
    ListProperty<NamParameter> result = new SimpleListProperty<>();
    return result;
  } 
}
