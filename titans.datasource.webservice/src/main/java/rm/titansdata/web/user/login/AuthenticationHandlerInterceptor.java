package rm.titansdata.web.user.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@CrossOrigin(maxAge = 3600)
@Lazy(false)
public class AuthenticationHandlerInterceptor implements HandlerInterceptor {

  /**
   *
   * @param request
   * @param response
   * @param handler
   * @return
   * @throws Exception
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT");
    response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
    response.setHeader("Access-Control-Allow-Headers", "AUTH-TOKEN,Content-Type, Content-Range, Content-Disposition, Content-Description,Origin, X-Requested-With, sessionId, APPNAME");
    return true;
  }
}
