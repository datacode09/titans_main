package rm.titansdata.web.rasters;

import java.net.URLDecoder;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import rm.titansdata.test.help.MockHelper;

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
  )
})
public class RastersServletIT {

  @Autowired
  private WebApplicationContext wac;
  private MockMvc mockMvc;

  @Before
  public void setup() throws Exception {
    TestContextManager testContextManager = new TestContextManager(getClass());
    testContextManager.prepareTestInstance(this);
    MockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
    this.mockMvc = builder.build();
  }

  /**
   *
   * @throws Exception
   */
  @Test
  @Parameters({
    "0", "-1"
  })
  public void getRastersByUserId(String userId) throws Exception {
    JSONObject jsonObj = new MockHelper(this.mockMvc, "/getRasters")
      .setParam("userId", userId)
      .perform();
    System.out.println("jsonObj = " + jsonObj);
  }

  /**
   *
   * @throws Exception
   */
  @Test
  @Parameters({
    "0", "-1"
  })
  public void getRasterByRasterId(String rasterId) throws Exception {
    JSONObject jsonObj = new MockHelper(this.mockMvc, "/getRaster")
      .setParam("rasterId", rasterId)
      .perform();
    System.out.println("jsonObj = " + jsonObj);
  }

  @Test
  @Parameters({
    "0", "-1"
  })
  public void getRastersByGroupId(String rasterGroupId) throws Exception {
    JSONObject jsonObj = new MockHelper(this.mockMvc, "/getRastersByGroupId")
      .setParam("rasterGroupId", rasterGroupId)
      .perform();
    System.out.println("jsonObj = " + jsonObj);
  }

  @Test
  @Parameters({
    "0, POLYGON ((766962 4099080%2C 633468 4095923%2C 628042 4428834%2C 756099 4432069%2C 766962 4099080)), 32610, 1",})
  public void getRasterValues(Long rasterId, String geometry, int srid, double value) throws Exception {
    JSONObject parameter = new JSONObject();
    parameter.put("key", "test_source");
    parameter.put("parentKey", "test_source");
    JSONObject jsonObj = new MockHelper(this.mockMvc, "/getRasterValues")
      .setParam("rasterId", String.valueOf(rasterId))
      .setParam("geometry", URLDecoder.decode(geometry, "UTF-8"))
      .setParam("srid", String.valueOf(srid))
      .setParam("parameter", parameter.toString())
      .perform();
    System.out.println("jsonObj = " + jsonObj);
  }

  @Test
  @Parameters({
    "0, POINT(766962 4099080), 32610, 1",})
  public void getRasterValue(Long rasterId, String geometry, int srid, double value) throws Exception {
    JSONObject parameter = new JSONObject();
    parameter.put("key", "test_source");
    parameter.put("parentKey", "test_source"); 
    JSONObject jsonObj = new MockHelper(this.mockMvc, "/getRasterValue")
      .setParam("rasterId", String.valueOf(rasterId))
      .setParam("point", URLDecoder.decode(geometry, "UTF-8"))
      .setParam("srid", String.valueOf(srid))
      .setParam("parameter", parameter.toString())
      .perform();
    System.out.println("jsonObj = " + jsonObj);
  }
  
  /**
   * 
   * @param rasterId 
   */
  @Test
  @Parameters({"1"})
  public void getPatterns(long rasterId) {
    JSONObject jsonObj = new MockHelper(this.mockMvc, "/getRasterParameters")
      .setParam("rasterId", String.valueOf(rasterId))
      .perform();
    System.out.println("jsonObj = " + jsonObj);
  }
}
