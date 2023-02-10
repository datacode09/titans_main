package rm.titansdata.web.user.login;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.titansdata.web.user.session.SessionManager;
import titans.auth.Authenticator;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class LoginService {

  @Autowired
  private Authenticator authenticator;
  
  @Autowired
  private SessionManager manager;
  
  /**
   * 
   * @param credentials
   * @return 
   */
  public Optional<String> loginUser(Credentials credentials) {
    Optional<String> authToken = this.authenticator.authenticate(credentials.email, credentials.password);
    if (authToken.isPresent()) { 
      this.manager.store(credentials, authToken.get());
    }
    return authToken;
  }

  /**
   * 
   * @param authToken
   * @return 
   */
  public boolean isLoggedIn(String authToken) {
    return this.manager.existsByToken(authToken);
  }
  
  /**
   * 
   * @param email 
   */
  public void logout(String email) {
    this.manager.remove(email);
  }

  boolean isLoggedInByEmail(String email) {
    return this.manager.existsByEmail(email);
  }

  String getEmail(String token) {
    return this.manager.getEmail(token);
  }
}
