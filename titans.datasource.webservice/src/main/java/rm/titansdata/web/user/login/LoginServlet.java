package rm.titansdata.web.user.login;

import java.util.HashMap;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rm.titansdata.web.RequestParser;
import rm.titansdata.web.ResponseHelper;

/**
 *
 * @author Ricardo Marquez
 */
@Controller
public class LoginServlet {

  @Autowired
  private LoginService service;
  @Autowired
  private ResponseHelper responseHelper;
  
  
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
    Optional<String> authToken = this.service.loginUser(credentials);
    response.setHeader("AUTH-TOKEN", authToken.get());
    this.responseHelper.send(new HashMap<>(), response);
  }
  
  /**
   * 
   * @param req
   * @param response 
   */
  @RequestMapping(path = "/logout",
    params = {"email"},
    method = RequestMethod.POST
  )
  public void logout(HttpServletRequest req, HttpServletResponse response) {
    RequestParser parser = new RequestParser(req);
    String email = parser.getString("email");
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
  
}
