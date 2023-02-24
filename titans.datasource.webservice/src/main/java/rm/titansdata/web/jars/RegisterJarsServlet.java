package rm.titansdata.web.jars;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rm.titansdata.web.ResponseHelper;

/**
 *
 * @author Ricardo Marquez
 */
@Controller
public class RegisterJarsServlet {

  @Autowired
  private ResponseHelper responseHelper;

  @Autowired
  private CustomClassLoader jarClassLoader;

  @RequestMapping(path = "/registerJar",
     params = {"jarfile", "class"},
     
     method = RequestMethod.POST
  )
  public void registerJar(HttpServletRequest req, HttpServletResponse res) {
    String f = req.getParameter("jarfile");
    String classe = req.getParameter("class");
    String[] classes = new String[]{classe};
    File jar = new File(f);
    this.jarClassLoader.loadLibrary(jar, classes);
    this.jarClassLoader.postLoad();
    Map<String, Object> map = new HashMap<>();
    map.put("result", true);
    this.responseHelper.send(map, res);
  }
}
