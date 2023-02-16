package titans.goes;

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
public class GoesParameterFactory implements ParameterFactory {

  @Autowired
  @Qualifier("goes.parameters")
  private ListProperty<GoesParameter> parameters;

  @Override
  public String key() {
    return "GOES";
  }

  @Override
  public Parameter create(JSONObject obj) {
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<ClassType> getClassTypes() {
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<Clazz> getClasses(ClassType classtype) {
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<Parameter> getParameters(Clazz... clazzes) {
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
