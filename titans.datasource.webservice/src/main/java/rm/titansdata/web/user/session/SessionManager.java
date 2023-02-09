package rm.titansdata.web.user.session;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import rm.titansdata.web.user.login.Credentials;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class SessionManager {
  private final Map<String, String> authorized = new HashMap<>();
  
  /**
   * 
   * @param credentials
   * @param authToken 
   */
  public void store(Credentials credentials, String authToken) {
    this.authorized.put(credentials.email, authToken);
  }

  /**
   * 
   * @param authToken
   * @return 
   */
  public boolean existsByToken(String authToken) {
    return this.authorized.containsValue(authToken);
  }

  /**
   * 
   * @param email 
   */
  public void remove(String email) {
    this.authorized.remove(email); 
  }
  
}
