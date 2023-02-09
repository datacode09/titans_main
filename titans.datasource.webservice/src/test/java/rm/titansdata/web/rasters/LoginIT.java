package rm.titansdata.web.rasters;

import java.util.Optional;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import rm.titansdata.test.help.BaseSpringTest;
import rm.titansdata.web.user.login.Credentials;
import rm.titansdata.web.user.login.LoginService;

/**
 *
 * @author Ricardo Marquez
 */
@RunWith(JUnitParamsRunner.class)
public class LoginIT extends BaseSpringTest {
  
  @Autowired
  private LoginService service;
    
  @Test
  @Parameters({
    "ricardo.marquez@epstechnologies.com, password, true"
    , "ricardo.marquez@epstechnologies.com, passwords, false"
  })
  public void test(String email, String password, boolean expresult) {
    Credentials credentials = new Credentials(email, password);
    Optional<String> authToken = this.service.loginUser(credentials);
    Assert.assertEquals(expresult, authToken.isPresent());
  }
  
  @Test
  @Parameters({"ricardo.marquez@epstechnologies.com, password"})
  public void test02(String email, String password) {
    Credentials credentials = new Credentials(email, password);
    Optional<String> authToken = this.service.loginUser(credentials);
    Assert.assertTrue(this.service.isLoggedIn(authToken.get()));
    this.service.logout(email);
    Assert.assertFalse(this.service.isLoggedIn(authToken.get()));
  }
  
}
