package rm.titansdata.web.user.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
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
  /**
   * 
   * @return 
   */
  public static String getSessionAuthToken()  {
    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    String authToken = attr.getRequest().getHeader("AUTH-TOKEN");
    return authToken;
  }
  
  /**
   * 
   * @param email
   * @return 
   */
  public Optional<String> getAuthToken(String email) {
    Optional<String> result = Optional.ofNullable(this.authorized.get(email));
    return result;
  }
}
