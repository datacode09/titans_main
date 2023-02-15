package rm.titansdata.web.project;

import common.db.DbConnection;
import common.db.RmDbUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.postgresql.geometric.PGpoint;
import org.postgresql.jdbc.PgArray;

/**
 *
 * @author Ricardo Marquez
 */
public class ProjectSource {

  private final DbConnection dbconn;

  public ProjectSource(DbConnection dbconn) {
    this.dbconn = dbconn;
  }

  /**
   *
   * @param projectId
   * @return
   */
  boolean projectExists(int projectId) {
    String query = String.format("select * from projects.project p where p.project_id = %d", projectId);
    List<ResultSet> a = this.dbconn.executeQuery(query, (rs) -> rs);
    boolean result = a.size() == 1;
    if (a.size() > 1) {
      throw new RuntimeException();
    }
    return result;
  }

  List<ProjectEntity> getProjects() {
    String query = "select \n"
      + " p.project_id, p.name, \n"
      + " Point(avg( e.lowerleft[0]) , avg(e.lowerleft[1])) as lowerleft , \n"
      + " Point(avg( e.upperright[0]), avg(e.upperright[1])) as upperright , \n"
      + " e.srid, \n"
      + " array_agg(d.rastergroup_id) as raster_ids \n"
      + "from projects.project p\n"
      + "left join projects.project_envelope e\n"
      + " on e.project_id = p.project_id\n"
      + "left join projects.projectdatasource d\n"
      + " on d.project_id = p.project_id\n"
      + "group by p.project_id, p.name, e.srid";
    List<ProjectEntity> result = this.dbconn //
      .executeQuery(query, this::toProjectEntity);
    return result;
  }

  /**
   *
   * @param rs
   * @return
   */
  private ProjectEntity toProjectEntity(ResultSet rs) {
    int projectId = RmDbUtils.intValue(rs, "project_id");
    String projectName = RmDbUtils.stringValue(rs, "name");
    int srid = RmDbUtils.intValue(rs, "srid");
    Point lowerleft;
    Point upperright;
    if (srid != 0) {
      try {
        PGpoint llpgPoint = (PGpoint) rs.getObject("lowerleft");
        PGpoint urpgPoint = (PGpoint) rs.getObject("upperright");
        GeometryFactory factory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), srid);
        lowerleft = factory.createPoint(new Coordinate(llpgPoint.x, llpgPoint.y));
        upperright = factory.createPoint(new Coordinate(urpgPoint.x, urpgPoint.y));
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    } else {
      lowerleft = null;
      upperright = null;
    }
    long[] rasterids;
    try {
      PgArray p = (PgArray) rs.getObject("raster_ids");
      Integer[] a = (Integer[]) p.getArray();
      if (a.length == 1 && a[0] == null) {
        rasterids = new long[0];
      } else {
        rasterids = Arrays.stream(a).mapToLong(b -> b.longValue()).toArray();
      }
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
    ProjectEntity result = new ProjectEntity(projectId, projectName, //
      lowerleft, upperright, rasterids);
    System.out.println("result = " + lowerleft + "," + upperright);
    return result;
  }

}
