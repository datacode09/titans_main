package rm.titansdata.web.user.session;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import rm.titansdata.web.user.login.Credentials;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class SessionManager implements ApplicationContextAware {

  private final Map<String, String> authorized = new HashMap<>();
  private ApplicationContext appContext;

  /**
   *
   * @param ac
   * @throws BeansException
   */
  @Override
  public void setApplicationContext(ApplicationContext ac) throws BeansException {
    this.appContext = ac;
  }

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

  /**
   *
   * @param email
   * @return
   */
  public boolean existsByEmail(String email) {
    return this.authorized.containsKey(email);
  }
  
  /**
   * 
   * @param token
   * @return 
   */
  public String getEmail(String token) {
    String result = this.authorized.entrySet().stream()
      .filter(e -> e.getValue().equals(token))
      .map(e -> e.getKey())
      .findFirst().orElse(null);
    return result;
  }

}
