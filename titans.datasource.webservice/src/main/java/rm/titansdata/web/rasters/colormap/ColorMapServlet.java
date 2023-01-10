package rm.titansdata.web.rasters.colormap;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rm.titansdata.colormap.ColorMap;
import rm.titansdata.web.RequestParser;
import rm.titansdata.web.ResponseHelper;

/**
 *
 * @author Ricardo Marquez
 */
@Controller
public class ColorMapServlet {

  @Autowired
  private ResponseHelper responseHelper;

  @Autowired
  private ColorMapService service;
  /**
   *
   * @param req
   * @param response
   */
  @RequestMapping(path = "/getColorMap",
    params = {"userId", "rasterId"},
    method = RequestMethod.GET
  )
  public void getColorMap(HttpServletRequest req, HttpServletResponse response) {
    RequestParser parser = new RequestParser(req);
    Long userId = parser.getLong("userId");
    Long rasterId = parser.getLong("rasterId");
    this.service.getColorMap(userId, rasterId);
    Map<String, Object> map = new HashMap<>();
    this.responseHelper.send(map, response);
  }
    
  /**
   * 
   * @param req
   * @param response 
   */
  @RequestMapping(
    path = "/saveColorMap", 
    params = {"userId", "rasterId", "colorMap", "colorMapType"},
    method = RequestMethod.POST)
  public void saveColorMap(HttpServletRequest req, HttpServletResponse response) {
    RequestParser parser = new RequestParser(req);
    Long userId = parser.getLong("userId");
    Long rasterId = parser.getLong("rasterId");
    ColorMap colorMap = ColorMap.parse(parser.getString("colorMap"));
    this.service.saveColorMap(colorMap, userId, rasterId);
    Map<String, Object> map = new HashMap<>();
    this.responseHelper.send(map, response);
    
  }
}
