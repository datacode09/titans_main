package rm.titansdata.web.user.login;

import java.util.HashMap;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
//    if (this.service.isLoggedInByEmail(email)) {
//      throw new RuntimeException("Email has already been logged in");
//    }   
    Credentials credentials = new Credentials(email, password);
    Optional<String> authToken = this.service.loginUser(credentials);
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
    HttpSession session = req.getSession();    
    System.out.println("session = " + session.getId());
  }
  
}
