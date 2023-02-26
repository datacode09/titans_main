package titans.mrms;

import org.junit.Before;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestContextManager;
import rm.titansdata.SridUtils;

/**
 *
 * @author Ricardo Marquez
 */
@ContextHierarchy({
  @ContextConfiguration(
    locations = {
      "/mrms.spring.xml"
    }
  )
})
public class BaseSpringITest {
  
  /**
   * 
   * @throws Exception 
   */
  @Before
  public void setup() throws Exception {
    TestContextManager testContextManager = new TestContextManager(getClass());
    testContextManager.prepareTestInstance(this);
    SridUtils.init();
  }

  
}
