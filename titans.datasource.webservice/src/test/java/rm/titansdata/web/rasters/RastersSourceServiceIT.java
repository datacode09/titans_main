package rm.titansdata.web.rasters;

import java.util.List;
import java.util.Map;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.web.WebAppConfiguration;
import rm.titansdata.Parameter;
import rm.titansdata.plugin.ClassType;
import rm.titansdata.plugin.Clazz;

/**
 *
 * @author Ricardo Marquez
 */
@RunWith(JUnitParamsRunner.class)
@WebAppConfiguration()
@ContextHierarchy({
  @ContextConfiguration(
    locations = {
      "/dispatcher-servlet.xml", "/applicationContext.xml"
    }
  ),})
public class RastersSourceServiceIT {

  @Autowired
  private RastersSourceService service;
  @Autowired
  private AbstractParameterFactory parameterFactory;

  @Before
  public void setup() throws Exception {
    TestContextManager testContextManager = new TestContextManager(getClass());
    testContextManager.prepareTestInstance(this);
  }

  @Test
  @Parameters({
    "0, 2",
    "-1, 0",
    "1, 0",})
  public void getrasters_by_userid(Long userId, int expGroupSize) {
    Map<RasterGroupEntity, List<Long>> rasters = service.getRastersByUserId(userId);
    Assert.assertEquals(expGroupSize, rasters.keySet().size());
  }

  @Test
  @Parameters({
    "0, true",
    "-1, false"
  })
  public void getrasters_by_rasterid(Long rasterId, boolean exists) {
    RasterEntity e = this.service.getRaster(rasterId);
    Assert.assertEquals(exists, e != null);
  }

  @Test
  @Parameters({
    //    "0, 1",
    //    "-1, 0",
    "1, 1"
  })
  public void getrasters_by_rastergroup_id(Long rasterGroupId, int expectedSize) {
    List<Long> e = this.service.getRastersByGroupId(rasterGroupId);
    Assert.assertEquals(expectedSize, e.size());
  }

  @Test
  @Parameters({
    "1", 
    "2"
  })
  public void getparameters_by_rasterId(long rasterId) throws Exception {
    String key = this.service.getRaster(rasterId).sourceTitle;
    JSONArray arr = new JSONArray();
    arr.put(new JSONObject("{'key': 'NOAA_VAR', 'varName': 'TMP_2-HTGL'}")); 
    List<Clazz> clazz = this.parameterFactory.getClasse(key, arr);
    List<Parameter> params = this.parameterFactory.getParameters(key, clazz);
    params.forEach(System.out::println);
  }
  
  @Test
  @Parameters({
    "1", 
    "2"
  })
  public void getclasses_by_rasterId(long rasterId) throws Exception {
    String key = this.service.getRaster(rasterId).sourceTitle;
    Map<ClassType, List<Clazz>> clazzes = this.parameterFactory.getClasses(key); 
    clazzes.entrySet().forEach(e->System.out.println(e.getKey() + ", " + e.getValue().size()));
  }
}
