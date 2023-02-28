package rm.titansdata.web.project;

import common.db.DbConnection;
import java.util.List;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class ProjectService {
  
  private final DbConnection dbconn;
  private final ProjectStore store;
  private final ProjectSource source;
  
  /**
   * 
   * @param dbconn 
   */
  public ProjectService(@Qualifier("titans.db") DbConnection dbconn) {
    this.dbconn = dbconn;  
    this.source = new ProjectSource(this.dbconn);
    this.store = new ProjectStore(this.dbconn);
  }
  
  /**
   * 
   * @param project_id
   * @param name 
   */
  public void createProject(int project_id, String name) {
    this.store.createProject(project_id, name);
  }
  
  /**
   * 
   * @param project_id 
   */
  void removeProject(int project_id) {
    this.store.removeProject(project_id);
  }

  /**
   * 
   * @param project_id
   * @param lowerleft
   * @param upperright 
   */
  void setProjectGeometry(int project_id, Point lowerleft, Point upperright) {
    this.store.setProjectGeometry(project_id, lowerleft, upperright);
  }

  /**
   * 
   * @param project_id
   * @param raster_ids 
   */
  void addProjectDataSources(int project_id, long[] raster_ids) {
    this.store.addProjectDataSources(project_id, raster_ids);
  }

  /**
   * 
   * @param project_id
   * @param raster_ids 
   */
  void removeProjectDataSources(int project_id, long[] raster_ids) {
    this.store.removeProjectDataSources(project_id, raster_ids);
  }
  
  /**
   * 
   * @param projectId
   * @return 
   */
  boolean projectExists(int projectId) {
    return this.source.projectExists(projectId);
  }

  /**
   * 
   * @return 
   */
  public List<ProjectEntity> getProjects() {
    List<ProjectEntity> projects = this.source.getProjects();
    return projects;
  }
  
  /**
   * 
   * @return 
   */
  public ProjectEntity getProject(int projectId) {
    List<ProjectEntity> projects = this.source.getProjects();
    ProjectEntity result = projects.stream().filter(p->p.projectId == projectId).findFirst().orElse(null); 
    return result;
  }
  
}
