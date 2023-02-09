package titans.auth;

import org.junit.Before;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestContextManager;

/**
 *
 * @author Ricardo Marquez
 */
@ContextHierarchy({
  @ContextConfiguration(
    locations = {
      "/auth.spring.xml"
    }
  )
})
public class BaseSpringTest {
  
  /**
   * 
   * @throws Exception 
   */
  @Before
  public void setup() throws Exception {
    TestContextManager testContextManager = new TestContextManager(getClass());
    testContextManager.prepareTestInstance(this);
  }

  
}
