package rm.test_source;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import rm.titansdata.plugin.ParameterFactory;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class TestParametersFactory implements ParameterFactory {

  @Override
  public String key() {
    return "test_source"; 
  }

  
  
  @Override
  public Parameter create(JSONObject obj) {
    return new Parameter() {
    };
  }
  
}
