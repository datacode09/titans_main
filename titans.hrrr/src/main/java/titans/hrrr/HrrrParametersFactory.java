package titans.hrrr;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ListProperty;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import rm.titansdata.plugin.ParameterFactory;
import titans.nam.NoaaParameter;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class HrrrParametersFactory implements ParameterFactory {

  @Autowired
  @Qualifier("hrrr.parameters")
  private ListProperty<NoaaParameter> parameters;
  
  /**
   * 
   * @return 
   */
  @Override
  public String key() {
    return "High Resolution Rapid Refresh";
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
