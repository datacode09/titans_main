package rm.titansdata.web.user.session;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author Ricardo Marquez
 */
@Configuration
public class UserScopeConfiguration {

  @Autowired
  private SessionManager sessionManager;

  private final Map<String, SessionScopedBean<UserToken>> tokens = new HashMap<>();

  /**
   *
   * @return
   */
  @SessionScope
  @Bean("user.token")
  public SessionScopedBean<UserToken> userToken() {
    String key = this.getKey();   
    if (!this.tokens.containsKey(key)) {
      UserToken value = new UserToken();
      SessionScopedBean<UserToken> result = new SessionScopedBean<>(this.sessionManager, () -> value);
      this.tokens.put(key, result);
    }
    SessionScopedBean<UserToken> result = this.tokens.get(key);
    return result;
  }
  
  /**
   * 
   * @return 
   */
  private String getKey() {
    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    String authToken = attr.getRequest().getHeader("AUTH-TOKEN");
    String key = (authToken == null ? "" : authToken);
    return key;
  }
}
