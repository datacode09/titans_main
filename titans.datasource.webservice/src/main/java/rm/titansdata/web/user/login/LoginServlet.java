package rm.titansdata.web.user.login;

import java.util.HashMap;
import java.util.Optional;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rm.titansdata.web.RequestParser;
import rm.titansdata.web.ResponseHelper;
import rm.titansdata.web.user.session.SessionScopedBean;
import rm.titansdata.web.user.session.UserToken;

/**
 *
 * @author Ricardo Marquez
 */
@Controller
public class LoginServlet extends HttpServlet{

  @Autowired
  private LoginService service;
  @Autowired
  private ResponseHelper responseHelper;

  @Autowired
  @Qualifier("user.token")
  private SessionScopedBean<UserToken> token;

  /**
   *
   * @param req
   * @param response
   */
  @RequestMapping(path = "/login",
    params = {"email"},
    method = RequestMethod.POST,
    headers = {"KEY"}
  )
  public void login(HttpServletRequest req, HttpServletResponse response) {
    RequestParser parser = new RequestParser(req);
    String email = parser.getString("email");
    String password = req.getHeader("KEY");
    Credentials credentials = new Credentials(email, password);
    Optional<String> authToken ;
    if (this.service.isLoggedInByEmail(email)) {
      boolean check = this.service.checkCredentials(credentials);
      if (check) {
        authToken = this.service.getAuthToken(email);
      } else {
        authToken = Optional.empty();
      }
    }  else {
      authToken = this.service.loginUser(credentials);
    }
    
    response.setHeader("AUTH-TOKEN", authToken.orElse(null));
    
    HashMap<String, Object> map = new HashMap<>();
    map.put("token", authToken.orElse(null));
    this.responseHelper.send(map, response);
  }

  /**
   *
   * @param req
   * @param response
   */
  @RequestMapping(path = "/logout",
    headers = {"AUTH-TOKEN"},
    method = RequestMethod.POST
  )
  public void logout(HttpServletRequest req, HttpServletResponse response) {
    String header = req.getHeader("AUTH-TOKEN");
    String email = this.service.getEmail(header);
    this.service.logout(email);
    this.responseHelper.send(new HashMap<>(), response);
  }

  /**
   *
   * @param req
   * @param response
   */
  @RequestMapping(path = "/isLoggedIn",
    method = RequestMethod.GET,
    headers = {"AUTH-TOKEN"}
  )
  public void isLoggedIn(HttpServletRequest req, HttpServletResponse response) {
    String authToken = req.getHeader("AUTH-TOKEN");
    boolean answer = this.service.isLoggedIn(authToken);
    HashMap<String, Object> result = new HashMap<>();
    result.put("result", answer);
    this.responseHelper.send(result, response);
  }

  /**
   *
   */
  @RequestMapping(path = "/dummyrequest",
    method = RequestMethod.GET
  )
  public void dummyrequest(HttpServletRequest req, HttpServletResponse response) {
    System.out.println(this.token);
    HttpSession session = req.getSession(false);
    if (session == null) {
      session = req.getSession(true); 
    }
    System.out.println(session.getAttribute("blah"));
    req.getSession().setAttribute("blah", "blah blah");
    System.out.println(this.token);
    System.out.println("session = " + session.getId());
  }

}
