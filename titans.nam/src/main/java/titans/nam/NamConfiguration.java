package titans.nam;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
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
  public ListProperty<NoaaParameter> getParameters() {
    ListProperty<NoaaParameter> result = new SimpleListProperty<>();
    result.setValue(FXCollections.observableArrayList());
    return result;
  } 
}
