package rm.titansdata.web.rasters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rm.titansdata.Parameter;
import rm.titansdata.plugin.ClassType;
import rm.titansdata.plugin.Clazz;
import rm.titansdata.properties.Bounds;
import rm.titansdata.raster.RasterCells;
import rm.titansdata.web.RequestParser;
import rm.titansdata.web.ResponseHelper;
import rm.titansdata.web.project.ProjectEntity;
import rm.titansdata.web.user.session.SessionManager;
import rm.titansdata.web.user.session.SessionScopedBean;

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

  @Autowired
  @Qualifier("user.project")
  private SessionScopedBean<ProjectEntity> projectEntity;

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
  @RequestMapping(path = "/getRasterClasses",
    params = {"rasterId"},
    method = RequestMethod.GET
  )
  public void getRasterClasses(HttpServletRequest req, HttpServletResponse response) {
    RequestParser parser = new RequestParser(req);
    Long rasterId = parser.getLong("rasterId");
    RasterEntity raster = this.rastersSourceService.getRaster(rasterId);
    String sourceTitle = raster.sourceTitle;
    Map<ClassType, List<Clazz>> a = this.parameterFactory.getClasses(sourceTitle);
    JSONObject f = new JSONObject();
    a.forEach((k, v) -> {
      JSONArray jsonObj = new JSONArray();
      for (Clazz clazze : v) {
        jsonObj.put(clazze.toJson());
      }
      try {
        f.put(k.name(), jsonObj);
      } catch (JSONException ex) {
        throw new RuntimeException(ex);
      }
    });
    Map<String, Object> map = new HashMap<>();
    map.put("clazzes", f);
    this.responseHelper.send(map, response);
  }

  /**
   *
   * @param req
   * @param response
   */
  @RequestMapping(path = "/getRasterParameters", //
    method = RequestMethod.GET, //
    params = {"rasterId", "clazzes"
    })//
  public void getRasterParameters(HttpServletRequest req, HttpServletResponse response) {
    RequestParser parser = new RequestParser(req);
    Long rasterId = parser.getLong("rasterId");

    RasterEntity raster = this.rastersSourceService.getRaster(rasterId);
    String sourceTitle = raster.sourceTitle;
    JSONArray jsonArray;
    try {
      jsonArray = new JSONArray(req.getParameter("clazzes"));
    } catch (JSONException ex) {
      throw new RuntimeException(ex);
    }
    List<Clazz> clazzes = this.parameterFactory.getClasse(sourceTitle, jsonArray);
    List<JSONObject> params = this.parameterFactory.getParameters(sourceTitle, clazzes)
      .stream()
      .map(p -> p.toJSONObject())
      .collect(Collectors.toList());
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

  /**
   *
   * @param req
   * @param res
   */
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
   * @param req
   * @param res
   */
  @RequestMapping(
    path = "/getRasterPointValues",
    params = {"rasterId", "point", "parameters", "srid"},
    method = RequestMethod.GET
  )
  public void getRasterPointValues(HttpServletRequest req, HttpServletResponse res) {
    RequestParser parser = new RequestParser(req);
    long rasterId = parser.getLong("rasterId");
    int srid = parser.getInteger("srid");
    Point point = (Point) parser.parseGeometry("point", srid);
    JSONArray jsonObject = this.getParametersJsonArray(parser);
    List<Parameter> param = this.parameterFactory.get(jsonObject);
    Map<String, Object> map = new HashMap<>();
    Map<Parameter, Double> values = this.rastersValueService.getPointRasterValues(rasterId, param, point);
    Map<String, Double> s = values.entrySet().stream()
      .collect(Collectors.toMap(d -> d.getKey().getKey(), d -> d.getValue()));
    map.put("values", s);
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

  /**
   *
   * @param parser
   * @return
   */
  private JSONArray getParametersJsonArray(RequestParser parser) {
    JSONArray result;
    try {
      String string = parser.getString("parameters");
      result = new JSONArray(string);
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
    Bounds bounds = this.getBoundsFromProject();
    RasterImageResult image = this.rastersImageService.getRasterImage(rasterId, param, bounds);
    Map<String, Object> map = new HashMap<>();
    map.put("value", image);
    this.responseHelper.send(map, res);
    // {"datetime":"2023012112","parentKey":"North American Model Forecasts","zoneid":"UTC","fcststep":0,"key":"2023012112-0"}
  }

  /**
   *
   * @return
   */
  private Bounds getBoundsFromProject() {
    ProjectEntity project = this.projectEntity.getValue(SessionManager.getSessionAuthToken());
    return new Bounds(project.lowerleft, project.upperright);
  }
}
