package titans.hrrr;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import rm.titansdata.Parameter;
import rm.titansdata.plugin.ClassType;
import rm.titansdata.plugin.Clazz;
import titans.hrrr.classes.HrrrDateClazz;
import titans.hrrr.classes.HrrrVarClazz;

/**
 *
 * @author Ricardo Marquez
 */
public class GetParametersIT extends BaseSpringTest{
  @Autowired
  private HrrrParametersFactory factory;
    
  /**
   * 
   */
  @Test
  public void get() {
    HrrrVarClazz defaultClazz = new HrrrVarClazz("TMP_2-HTGL", "TMP_2-HTGL"); 
    List<Parameter> parameters = this.factory.getParameters(defaultClazz, HrrrDateClazz.TODAY); 
    int expected = 49;
    Assert.assertEquals(expected, parameters.size());
  }
  
  @Test
  public void test2() {
    List<ClassType> classtypes = this.factory.getClassTypes();
    for (ClassType classtype : classtypes) {
      System.out.println(classtype);
      List<Clazz> c = this.factory.getClasses(classtype);
      c.stream().forEach(System.out::println);
    }
  }
}
