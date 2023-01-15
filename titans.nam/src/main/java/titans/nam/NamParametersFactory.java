package titans.nam;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ListProperty;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import rm.titansdata.plugin.ParameterFactory;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class NamParametersFactory implements ParameterFactory {

  @Autowired
  @Qualifier("nam.parameters")
  private ListProperty<NamParameter> parameters;

  /**
   *
   * @return
   */
  @Override
  public String key() {
    return "North American Model Forecasts";
  }

  /**
   *
   * @param obj
   * @return
   */
  @Override
  public Parameter create(JSONObject obj) {
    NamParameter param = NamParameter.create(obj);
    return param;
  }

  /**
   *
   * @return
   */
  @Override
  public List<Parameter> getParameters() {
    List<Parameter> arrayList = new ArrayList<>(this.parameters.getValue());
    return arrayList;
  }

}
