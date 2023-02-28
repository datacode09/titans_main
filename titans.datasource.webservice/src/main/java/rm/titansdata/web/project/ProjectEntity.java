package rm.titansdata.web.project;

import java.io.Serializable;
import org.locationtech.jts.geom.Point;
import rm.titansdata.properties.Bounds;

/**
 *
 * @author Ricardo Marquez
 */
public class ProjectEntity implements Serializable{

  public final int projectId;
  public final String projectName;
  public final Point lowerleft;
  public final Point upperright;
  public final long[] rastergroupIds;
  
  /**
   * 
   * @param projectId
   * @param projectName
   * @param lowerleft
   * @param upperright
   * @param rastergroupIds 
   */
  ProjectEntity(int projectId, String projectName, Point lowerleft, Point upperright, long[] rastergroupIds) {
    this.projectId = projectId;
    this.projectName = projectName;
    this.lowerleft = lowerleft;
    this.upperright = upperright;
    this.rastergroupIds = rastergroupIds;
  }
  
  /**
   * 
   * @return 
   */
  public Bounds getBounds() {
    Bounds result = new Bounds(this.lowerleft, this.upperright);
    return result;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public String toString() {
    return "ProjectEntity{" + "projectId=" + projectId + ", projectName=" + projectName + ", lowerleft=" + lowerleft + ", upperright=" + upperright + ", rastergroupIds=" + rastergroupIds + '}';
  }
  
}
