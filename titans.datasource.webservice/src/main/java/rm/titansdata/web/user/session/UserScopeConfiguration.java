package rm.titansdata.web.user.session;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;
import rm.titansdata.web.project.ProjectEntity;

/**
 *
 * @author Ricardo Marquez
 */
@Configuration
public class UserScopeConfiguration {

  @Autowired
  private SessionManager sessionManager;
  private final Map<String, SessionScopedBean<UserToken>> tokens = new HashMap<>();
  private final Map<String, SessionScopedBean<ProjectEntity>> projects = new HashMap<>();

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
  @SessionScope
  @Bean("user.project")
  public SessionScopedBean<ProjectEntity> userProject() {
    String key = this.getKey();
    if (!this.projects.containsKey(key)) {
      SessionScopedBean<ProjectEntity> result = new SessionScopedBean<>(this.sessionManager, () -> null);
      this.projects.put(key, result);
    }
    SessionScopedBean<ProjectEntity> result = this.projects.get(key);
    return result;
  }
  
  /**
   * 
   * @return 
   */
  private String getKey() {
    String authToken = SessionManager.getSessionAuthToken();
    String key = (authToken == null ? "" : authToken);
    return key;
  }
}
