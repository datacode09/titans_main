package titans.nam.components;

import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import titans.nam.NamParameter;
import titans.nam.NamRasterFactory;
import titans.nam.grib.NamGribSource;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
@DependsOn("nam.parameters")
public class NamPropertyUpdates implements InitializingBean {
  
  /**
   * 
   */
  @Autowired
  @Qualifier("nam.parameters")
  private ListProperty<NamParameter> parameters;
  
  @Autowired
  private NamRasterFactory factory;
  
  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    NamGribSource source = new NamGribSource();
    String parentKey = this.factory.key();
    List<NamParameter> params = source.getCurrentNamParameters(parentKey);
    ObservableList<NamParameter> obsList = FXCollections.observableArrayList(params);
    this.parameters.setValue(obsList);
  }
}
