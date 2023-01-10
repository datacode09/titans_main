package rm.titansdata.web.rasters;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rm.titansdata.raster.RasterCells;
import rm.titansdata.web.RequestParser;
import rm.titansdata.web.ResponseHelper;

/**
 *
 * @author Ricardo Marquez
 */
@Controller
public class RastersServlet {

  @Autowired
  private ResponseHelper responseHelper;

  @Autowired
  private RastersSourceService rastersSourceService;
  
  @Autowired
  private RastersValueService rastersValueService;

  /**
   *
   * @param req
   * @param response
   */
  @RequestMapping(path = "/getRasters",
    params = {"userId"},
    method = RequestMethod.GET
  )
  public void getRasters(HttpServletRequest req, HttpServletResponse response) {
    RequestParser parser = new RequestParser(req);
    Long userId = parser.getLong("userId");
    Map<RasterGroupEntity, List<Long>> values = this.rastersSourceService.getRastersByUserId(userId);
    Map<String, Object> map = new HashMap<>();
    map.put("values", values);
    this.responseHelper.send(map, response);
  }

  @RequestMapping(path = "/getRaster",
    params = "rasterId",
    method = RequestMethod.GET)
  public void getRaster(HttpServletRequest req, HttpServletResponse res) {
    RequestParser parser = new RequestParser(req);
    Long rasterId = parser.getLong("rasterId");
    Map<String, Object> map = new HashMap<>();
    RasterEntity value = this.rastersSourceService.getRaster(rasterId); 
    map.put("value", value);
    this.responseHelper.send(map, res);
  }
  
  @RequestMapping(path = "/getRastersByGroupId",
    params = "rasterGroupId",
    method = RequestMethod.GET)
  public void getRastersByGroupId(HttpServletRequest req, HttpServletResponse res) {
    RequestParser parser = new RequestParser(req);
    Long rastersGroupId = parser.getLong("rasterGroupId");
    Map<String, Object> map = new HashMap<>();
    List<Long> values = this.rastersSourceService.getRastersByGroupId(rastersGroupId); 
    map.put("value", values);
    this.responseHelper.send(map, res);
  }
  
  @RequestMapping(
    path="/getRasterValues", 
    params={"rasterId", "geometry", "srid"}, method=RequestMethod.GET)
  public void getRasterValues(HttpServletRequest req, HttpServletResponse res) {
    RequestParser parser = new RequestParser(req);
    Long rasterId = parser.getLong("rasterId");
    int srid = parser.getInteger("srid");
    Geometry geometry = parser.parseGeometry("geometry", srid);
    Map<String, Object> map = new HashMap<>();
    RasterCells values = this.rastersValueService.getRasterValues(rasterId, geometry); 
    map.put("values", values);
    this.responseHelper.send(map, res);
  }
  
  @RequestMapping(
    path="/getRasterValue", 
    params={"rasterId", "point", "srid"}, method=RequestMethod.GET)
  public void getRasterValue(HttpServletRequest req, HttpServletResponse res) {
    RequestParser parser = new RequestParser(req);
    Long rasterId = parser.getLong("rasterId");
    int srid = parser.getInteger("srid");
    Point geometry = (Point) parser.parseGeometry("point", srid);
    Map<String, Object> map = new HashMap<>();
    double value = this.rastersValueService.getRasterValue(rasterId, geometry); 
    map.put("value", value);
    this.responseHelper.send(map, res);
  }
  
}
