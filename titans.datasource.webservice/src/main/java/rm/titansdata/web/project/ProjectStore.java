package rm.titansdata.web.project;

import com.google.common.base.Objects;
import common.db.DbConnection;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.locationtech.jts.geom.Point;

/**
 *
 * @author Ricardo Marquez
 */
public class ProjectStore {

  private final DbConnection conn;

  public ProjectStore(DbConnection conn) {
    this.conn = conn;
  }

  /**
   *
   * @param projectId
   * @param name
   */
  public void createProject(int projectId, String name) {
    String statement = this.getCreateProjectStatement(projectId, name);
    this.conn.executeStatement(statement);
  }

  /**
   *
   * @param projectId
   */
  public void removeProject(int projectId) {
    String statement = this.getRemoveProjectStatement(projectId);
    this.conn.executeStatement(statement);
  }

  /**
   *
   */
  public void setProjectGeometry(int projectId, Point lowerleft, Point upperight) {
    String statement = this.getSaveProjectGeometry(projectId, lowerleft, upperight);
    this.conn.executeStatement(statement);
  }

  /**
   *
   */
  public void addProjectDataSources(int projectId, long[] rasterIds) {
    String statement = this.getAddProjectDataSourcesQuery(projectId, rasterIds);
    this.conn.executeStatement(statement);
  }

  /**
   *
   */
  public void removeProjectDataSources(int projectId, long[] rasterIds) {
    String statement = this.getRemoveProjectDataSources(projectId, rasterIds);
    this.conn.executeStatement(statement);
  }

  /**
   *
   * @param projectId
   * @param name
   * @return
   */
  private String getCreateProjectStatement(int projectId, String name) {
    String result = "insert into projects.project(project_id, name) values \n"
      + String.format("(%d, '%s')", projectId, name);
    return result;
  }

  /**
   *
   * @param projectId
   * @return
   */
  private String getRemoveProjectStatement(int projectId) {
    String result = "delete from projects.project p \n"
      + String.format("where p.project_id = %d", projectId);
    return result;
  }

  /**
   *
   * @param projectId
   * @return
   */
  private String getSaveProjectGeometry(int projectId, Point lowerleft, Point upperight) {
    int srid = upperight.getSRID();
    if (!Objects.equal(upperight.getSRID(), lowerleft.getSRID())) {
      throw new RuntimeException("Srids are not equal");
    }
    String result = "insert into \n"
      + "projects.project_envelope(project_id, lowerleft, upperright, srid) \n"
      + String.format("values (%d, POINT(%f,%f), POINT(%f,%f), %d) \n", 
        projectId, lowerleft.getX(), lowerleft.getY(), upperight.getX(), upperight.getY(), srid)
      + "on conflict (project_id) do update \n"
      + "set lowerleft=excluded.lowerleft\n"
      + ", upperright=excluded.lowerleft\n"
      + ", srid=excluded.srid"
      ;
    return result;
  }

  /**
   *
   * @param projectId
   * @param rasterIds
   * @return
   */
  private String getRemoveProjectDataSources(int projectId, long[] rasterIds) {
    String q = Arrays.stream(rasterIds)
      .mapToObj(Long::toString)
      .collect(Collectors.joining(","));
    String query = "delete from projects.projectdatasource where \n"
      + String.format("project_id = %d\n", projectId)  
      + String.format(" and rastergroup_id = any(array[%s])", q);
    return query;
  }

  /**
   *
   * @param projectId
   * @param rasterIds
   * @return
   */
  private String getAddProjectDataSourcesQuery(int projectId, long[] rasterIds) {
    String q = Arrays.stream(rasterIds)
      .mapToObj(Long::toString)
      .collect(Collectors.joining(","));
    String query = "with params as (\n"
      + String.format("	select unnest(array[%s]) as rastergroup_id\n", q)
      + ")\n"
      + "insert into projects.projectdatasource (project_id, rastergroup_id) \n"
      + String.format("select %d, p.rastergroup_id from params p", projectId);
    return query;
  }

}
