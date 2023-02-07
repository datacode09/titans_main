package titans.hrrr.core;

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
import titans.hrrr.HrrrRasterFactory;
import titans.hrrr.core.grib.HrrrGribSource;
import titans.nam.NoaaParameter;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
@DependsOn("hrrr.parameters")
public class HrrrPropertyUpdates implements InitializingBean {
  
  /**
   * 
   */
  @Autowired
  @Qualifier("hrrr.parameters")
  private ListProperty<NoaaParameter> parameters;
  
  @Autowired
  private HrrrRasterFactory factory;
  
  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    HrrrGribSource source = new HrrrGribSource();
    String parentKey = this.factory.key();
    List<NoaaParameter> params = source.getCurrentParameters(parentKey);
    ObservableList<NoaaParameter> obsList = FXCollections.observableArrayList(params);
    this.parameters.setValue(obsList);
  }
}
