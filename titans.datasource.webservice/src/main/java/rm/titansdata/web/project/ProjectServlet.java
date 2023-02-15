package rm.titansdata.web.project;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rm.titansdata.web.RequestParser;
import rm.titansdata.web.ResponseHelper;
import rm.titansdata.web.user.session.SessionScopedBean;

/**
 *
 * @author Ricardo Marquez
 */
@Controller
public class ProjectServlet extends HttpServlet {

  @Autowired
  private ResponseHelper responseHelper;

  @Autowired
  private ProjectService service;

  @Autowired
  @Qualifier("user.project")
  private SessionScopedBean<ProjectEntity> project;

  /**
   *
   * @param req
   * @param response
   */
  @RequestMapping(
    path = "/setProject",
    params = {"project_id"},
    method = RequestMethod.POST
  )
  public void setProject(HttpServletRequest req, HttpServletResponse response) {
    String authToken = req.getHeader("AUTH-TOKEN");
    if (!req.getParameter("project_id").isEmpty()) {
      int projectId = new RequestParser(req).getInteger("project_id");
      ProjectEntity p = this.service.getProjects().stream().filter(a -> Objects.equals(a.projectId, projectId))
        .findFirst().orElse(null);
      this.project.setValue(authToken, p);
    } else {
      this.project.setValue(authToken, null);
    }
  }

  /**
   *
   * @param req
   * @param response
   */
  @RequestMapping(path = "/projectExists",
    params = {"project_id"},
    method = RequestMethod.GET
  )
  public void projectExists(HttpServletRequest req, HttpServletResponse response) {
    int project_id = new RequestParser(req).getInteger("project_id");
    boolean exists = this.service.projectExists(project_id);
    HashMap<String, Object> result = new HashMap<>();
    result.put("exists", exists);
    this.responseHelper.send(result, response);
  }

  /**
   *
   * @param req
   * @param response
   */
  @RequestMapping(path = "/createProject",
    params = {"project_id", "project_name"},
    method = RequestMethod.POST
  )
  public void createProject(HttpServletRequest req, HttpServletResponse response) {
    int project_id = new RequestParser(req).getInteger("project_id");
    String name = new RequestParser(req).getString("project_name");
    this.service.createProject(project_id, name);
    this.responseHelper.send(new HashMap<>(), response);
  }

  /**
   *
   * @param req
   * @param response
   */
  @RequestMapping(path = "/removeProject",
    params = {"project_id"},
    method = RequestMethod.POST
  )
  public void removeProject(HttpServletRequest req, HttpServletResponse response) {
    int project_id = new RequestParser(req).getInteger("project_id");
    this.service.removeProject(project_id);
    this.responseHelper.send(new HashMap<>(), response);
  }

  /**
   *
   * @param req
   * @param response
   */
  @RequestMapping(path = "/setProjectGeometry",
    params = {"project_id", "lowerleft", "upperright", "srid"},
    method = RequestMethod.POST
  )
  public void setProjectGeometry(HttpServletRequest req, HttpServletResponse response) {
    int project_id = new RequestParser(req).getInteger("project_id");
    if (!req.getParameter("srid").isEmpty()) {
      int srid = new RequestParser(req).getInteger("srid");
      Point lowerleft = new RequestParser(req).getPoint("lowerleft", srid);
      Point upperright = new RequestParser(req).getPoint("upperright", srid);
      this.service.setProjectGeometry(project_id, lowerleft, upperright);
    } else {
      this.service.setProjectGeometry(project_id, null, null);
    }
    this.responseHelper.send(new HashMap<>(), response);
  }

  /**
   *
   * @param req
   * @param response
   */
  @RequestMapping(path = "/addProjectDataSources",
    params = {"project_id", "rastergroupids"},
    method = RequestMethod.POST
  )
  public void addProjectDataSources(HttpServletRequest req, HttpServletResponse response) {
    int project_id = new RequestParser(req).getInteger("project_id");
    long[] raster_ids = new RequestParser(req).getLongArray("rastergroupids");
    this.service.addProjectDataSources(project_id, raster_ids);
    this.responseHelper.send(new HashMap<>(), response);
  }

  /**
   *
   * @param req
   * @param response
   */
  @RequestMapping(path = "/removeProjectDataSources",
    params = {"project_id", "rastergroupids"},
    method = RequestMethod.POST
  )
  public void removeProjectDataSources(HttpServletRequest req, HttpServletResponse response) {
    int project_id = new RequestParser(req).getInteger("project_id");
    long[] raster_ids = new RequestParser(req).getLongArray("rastergroupids");
    this.service.removeProjectDataSources(project_id, raster_ids);
    this.responseHelper.send(new HashMap<>(), response);
  }

  /**
   *
   * @param req
   * @param response
   */
  @RequestMapping(path = "/getProjects",
    params = {},
    method = RequestMethod.GET
  )
  public void getProjects(HttpServletRequest req, HttpServletResponse response) {
    List<ProjectEntity> projects = this.service.getProjects();
    HashMap<String, Object> map = new HashMap<>();
    map.put("projects", projects);
    this.responseHelper.send(map, response);
  }

}
