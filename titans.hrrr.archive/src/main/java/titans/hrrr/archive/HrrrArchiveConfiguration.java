package titans.hrrr.archive;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import titans.nam.NoaaParameter;

/**
 *
 * @author Ricardo Marquez
 */
@Configuration
public class HrrrArchiveConfiguration {

  /**
   *
   * @return
   */
  @Bean("hrrr.arch.parameters")
  public ListProperty<NoaaParameter> getParameters() {
    ListProperty<NoaaParameter> result = new SimpleListProperty<>();
    result.setValue(FXCollections.observableArrayList());
    return result;
  }
}
