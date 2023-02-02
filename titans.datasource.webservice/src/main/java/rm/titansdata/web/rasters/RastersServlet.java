package rm.titansdata.web.rasters;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rm.titansdata.Parameter;
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

  @Autowired
  private RasterImageService rastersImageService;

  @Autowired
  private AbstractParameterFactory parameterFactory;

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
    JSONObject obj = toJson(values);
    map.put("values", obj.toString());
    this.responseHelper.send(map, response);
  }
  
  /**
   * 
   * @param req
   * @param response 
   */
  @RequestMapping(path="/getRasterParameters", //
    method = RequestMethod.GET, //
    params = {"rasterId"})//
  public void getRasterParameters(HttpServletRequest req, HttpServletResponse response) {
    RequestParser parser = new RequestParser(req);
    Long rasterId = parser.getLong("rasterId");  
    RasterEntity raster = this.rastersSourceService.getRaster(rasterId);
    String sourceTitle = raster.sourceTitle; 
    List<JSONObject> params = this.parameterFactory.getParameters(sourceTitle)
      .stream()    
      .map(p->p.toJSONObject())
      .collect(Collectors.toList())
      ;
    Map<String, Object> map = new HashMap<>();
    map.put("values", params); 
    this.responseHelper.send(map, response);
  }

  /**
   *
   * @param values
   * @return
   * @throws RuntimeException
   */
  private JSONObject toJson(Map<RasterGroupEntity, List<Long>> values) {
    JSONObject obj = new JSONObject();
    JSONArray entries = new JSONArray();
    try {
      obj.put("rastergroups", entries);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    values.entrySet().forEach(e -> {
      RasterGroupEntity k = e.getKey();
      List<Long> rasterIds = e.getValue();
      JSONArray rasterIdsJson = new JSONArray();
      rasterIds.stream().forEach((rasterId) -> {
        try {
          rasterIdsJson.put(rasterIdsJson.length(), rasterId);
        } catch (Exception ex) {
          throw new RuntimeException(ex);
        }
      });
      try {
        JSONObject o = new JSONObject();
        o.put("name", k.name);
        o.put("rasterGroupId", k.rasterGroupId);
        o.put("rasterIds", rasterIdsJson);
        entries.put(entries.length(), o);
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    });
    return obj;
  }

  @RequestMapping(path = "/getRaster",
    params = "rasterId",
    method = RequestMethod.GET
  )
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
    path = "/getRasterValues",
    params = {"rasterId", "geometry", "parameter", "srid"}, method = RequestMethod.GET)
  public void getRasterValues(HttpServletRequest req, HttpServletResponse res) {
    RequestParser parser = new RequestParser(req);
    Long rasterId = parser.getLong("rasterId");
    int srid = parser.getInteger("srid");
    Geometry geometry = parser.parseGeometry("geometry", srid);
    JSONObject jsonObject = this.getParameterJson(parser);
    Parameter param = parameterFactory.get(jsonObject);
    Map<String, Object> map = new HashMap<>();
    RasterCells values = this.rastersValueService.getRasterValues(rasterId, param, geometry);
    map.put("values", values);
    this.responseHelper.send(map, res);
  }

  @RequestMapping(
    path = "/getRasterValue",
    params = {"rasterId", "point", "parameter", "srid"},
    method = RequestMethod.GET
  )
  public void getRasterValue(HttpServletRequest req, HttpServletResponse res) {
    RequestParser parser = new RequestParser(req);
    long rasterId = parser.getLong("rasterId");
    int srid = parser.getInteger("srid");
    Point point = (Point) parser.parseGeometry("point", srid);
    JSONObject jsonObject = this.getParameterJson(parser);
    Parameter param = parameterFactory.get(jsonObject);
    Map<String, Object> map = new HashMap<>();
    double value = this.rastersValueService.getRasterValue(rasterId, param, point);
    map.put("value", value);
    this.responseHelper.send(map, res);
  }

  /**
   *
   * @param parser
   * @return
   */
  private JSONObject getParameterJson(RequestParser parser) {
    JSONObject result;
    try {
      String string = parser.getString("parameter");
      result = new JSONObject(string);
    } catch (JSONException ex) {
      throw new RuntimeException(ex);
    }
    return result;
  }

  @RequestMapping(
    path = "/getRasterImage",
    params = {"rasterId", "parameter"},
    method = RequestMethod.GET
  )
  public void getRasterImage(HttpServletRequest req, HttpServletResponse res) {
    RequestParser parser = new RequestParser(req);
    long rasterId = parser.getLong("rasterId");
    JSONObject jsonObject = this.getParameterJson(parser); 
    Parameter param = parameterFactory.get(jsonObject);
    RasterImageResult image = this.rastersImageService.getRasterImage(rasterId, param);
    Map<String, Object> map = new HashMap<>();
    map.put("value", image);
    this.responseHelper.send(map, res);
    // {"datetime":"2023012112","parentKey":"North American Model Forecasts","zoneid":"UTC","fcststep":0,"key":"2023012112-0"}
  }
}


