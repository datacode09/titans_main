package rm.titansdata.web.projects;

import common.db.DbConnection;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import rm.titansdata.web.project.ProjectStore;

/**
 *
 * @author Ricardo Marquez
 */
public class ProjectIT {

  private DbConnection conn;

  @Before
  public void setup() {
    this.conn = new DbConnection.Builder()
      .setUrl("localhost")
      .setPort(5434)
      .setDatabaseName("titans.application")
      .setUser("postgres")
      .setPassword("postgres")
      .createDbConnection();
    this.conn.executeStatement("truncate projects.project cascade");
  }

  /**
   *
   */
  @Test
  public void test() {
    ProjectStore store = new ProjectStore(conn);
    int projectId = 0;
    String name = "First Project";
    int srid = 4326;
    store.createProject(projectId, name);
    GeometryFactory factory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), srid);
    Point lowerleft = factory.createPoint(new Coordinate(-120.43, 37.36));
    Point upperright = factory.createPoint(new Coordinate(-120.53, 37.46));
    store.setProjectGeometry(projectId, lowerleft, upperright);
    long[] rasterids = new long[]{1, 2};
    long[] rasteridsremove = new long[]{1};
    store.addProjectDataSources(projectId, rasterids);
    store.removeProjectDataSources(projectId, rasteridsremove);
  }

}
