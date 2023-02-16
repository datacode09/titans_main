package titans.nam;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ListProperty;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import rm.titansdata.plugin.ClassType;
import rm.titansdata.plugin.Clazz;
import rm.titansdata.plugin.ParameterFactory;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class NamParametersFactory implements ParameterFactory {

  @Autowired
  @Qualifier("nam.parameters")
  private ListProperty<NoaaParameter> parameters;

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
   * @return
   */
  @Override
  public List<ClassType> getClassTypes() {
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<Parameter> getParameters(Clazz... clazzes) {
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   *
   * @return
   */
//  @Override
  public List<Parameter> getParameters() {
    List<Parameter> arrayList = new ArrayList<>(this.parameters.getValue());
    return arrayList;
  }

  @Override
  public List<Clazz> getClasses(ClassType classtype) {
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   *
   * @param obj
   * @return
   */
  @Override
  public Parameter create(JSONObject obj) {
    NoaaParameter param;
    if (obj.has("key")) {
      try {
        String key = obj.getString("key");
        param = this.parameters.stream()
          .filter(e -> e.getKey().equals(key))
          .findFirst()
          .orElseGet(() -> NoaaParameter.create(obj));
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    } else {
      param = NoaaParameter.create(obj);
    }

    return param;
  }

}
