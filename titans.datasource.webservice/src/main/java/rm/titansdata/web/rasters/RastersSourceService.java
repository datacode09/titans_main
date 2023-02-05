package rm.titansdata.web.rasters;

import common.RmExceptions;
import common.db.DbConnection;
import common.db.RmDbUtils;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.util.Pair;
import org.apache.commons.lang3.mutable.MutableObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class RastersSourceService {

  @Autowired
  @Qualifier("titans.db")
  private DbConnection dbconn;

  /**
   *
   * @param userId
   * @return
   */
  public Map<RasterGroupEntity, List<Long>> getRastersByUserId(Long userId) {
    String query = this.getRastersByUserIdQuery(userId);
    List<Pair<RasterGroupEntity, Long>> list = this.dbconn //
      .executeQuery(query, this::toRasterGroupEntry);
    Map<RasterGroupEntity, List<Long>> r = list.stream() //
      .collect(Collectors.toMap(p -> p.getKey(), this::toValueList, this::add));
    return r;
  }

  /**
   *
   * @param p
   * @return
   */
  private List<Long> toValueList(Pair<RasterGroupEntity, Long> p) {
    return Arrays.asList(p.getValue());
  }

  /**
   *
   * @param t
   * @param u
   * @return
   */
  private List<Long> add(List<Long> t, List<Long> u) {
    List<Long> result = new ArrayList<>();
    result.addAll(u);
    result.addAll(t);
    return result;
  }

  /**
   *
   * @param rs
   * @return
   */
  private Pair<RasterGroupEntity, Long> toRasterGroupEntry(ResultSet rs) {
    long rasterId = RmDbUtils.longValue(rs, "raster_id");
    Long rasterGroupId = RmDbUtils.longValue(rs, "rastergroup_id");
    String groupName = RmDbUtils.stringValue(rs, "rastergroup_name");
    RasterGroupEntity group = new RasterGroupEntity(rasterGroupId, groupName);
    Pair<RasterGroupEntity, Long> result = new Pair<>(group, rasterId);
    return result;
  }

  /**
   *
   * @param userId
   * @return
   */
  private String getRastersByUserIdQuery(Long userId) {
    String result = "select \n"
      + "	rg.rastergroup_id as rastergroup_id\n"
      + "    , rg.NAME as rastergroup_name\n"
      + "    , r.raster_id\n"
      + "from public.rastergroup_by_user g\n"
      + "join public.rastergroup rg\n"
      + "	on rg.rastergroup_id = g.rastergroup_id    \n"
      + "join public.rastergroup_raster_link l\n"
      + "	on l.rastergroup_id = g.rastergroup_id    \n"
      + "join public.raster r\n"
      + "	on r.raster_id = l.raster_id\n"
      + "where g.user_id = " + userId;
    return result;
  }

  /**
   *
   * @param rasterId
   * @return
   */
  public RasterEntity getRaster(Long rasterId) {
    String query = getRasterQuery(rasterId);
    MutableObject<RasterEntity> obj = new MutableObject<>();
    this.dbconn.executeQuery(query, (rs) -> {
      obj.setValue(this.toRasterEntity(rs));
    });
    RasterEntity result = obj.getValue();
    return result;
  }

  /**
   *
   * @param rasterId
   * @return
   */
  private String getRasterQuery(Long rasterId) {
    String query = "select\n"
      + "   r.raster_id \n"
      + "    , s.title\n"
      + "    , s.description\n"
      + "    , t.*\n"
      + "    , p.dx, p.dy, \n"
      + "    postgis.st_asbinary(cast (p.lowerleft as postgis.geometry)) as lowerleft, \n"
      + "    postgis.st_asbinary(cast (p.upperright as postgis.geometry)) as upperright, \n"
      + "    p.srid\n"
      + "from public.raster r\n"
      + "join public.source s\n"
      + "	on s.source_id = r.source_id\n"
      + "join public.rastertype t\n"
      + "	on t.rastertype_id = r.rastertype_id\n"
      + "join public.rastergeomproperties p\n"
      + "	on p.rastergeomproperties_id = r.rastergeomproperties_id\n"
      + String.format("where r.raster_id = %d", rasterId);
    return query;
  }

  /**
   *
   * @param rs
   * @return
   */
  private RasterEntity toRasterEntity(ResultSet rs) {
    int srid = RmDbUtils.intValue(rs, "srid");
    RasterEntity r = new RasterEntity.Builder()
      .setRasterId(RmDbUtils.longValue(rs, "raster_id"))
      .setRasterTypeId(RmDbUtils.longValue(rs, "rastertype_id"))
      .setSourceTitle(RmDbUtils.stringValue(rs, "title"))
      .setSourceDescription(RmDbUtils.stringValue(rs, "description"))
      .setDx(RmDbUtils.doubleValue(rs, "dx"))
      .setDy(RmDbUtils.doubleValue(rs, "dy"))
      .setLowerleft(RmDbUtils.pointValue(rs, "lowerleft", srid))
      .setUpperright(RmDbUtils.pointValue(rs, "upperright", srid))
      .build();
    return r;
  }

  public List<Long> getRastersByGroupId(Long rasterGroupId) {
    String query = "select \n"
      + "    r.raster_id\n"
      + "from public.rastergroup_raster_link l\n"
      + "join public.raster r\n"
      + "	on r.raster_id = l.raster_id\n"
      + "where l.rastergroup_id = " + rasterGroupId;;
    List<Long> result = this.dbconn.executeQuery(query, (rs) -> {
      Long e = RmDbUtils.longValue(rs, "raster_id");
      return e;
    });
    return result;
  }

  /**
   *
   * @param key
   * @return
   */
  public long getRasterIdByKey(String key) {
    MutableObject<Long> obj = new MutableObject<>(Long.MIN_VALUE);
    String query = "select r.raster_id \n"
      + "from public.raster r \n"
      + "join source s \n"
      + "on s.source_id = r.source_id \n"
      + "where s.title = '" + key + "'";
    this.dbconn.executeQuery(query, (rs) -> {
      obj.setValue(RmDbUtils.longValue(rs, "raster_id"));
    });
    if (obj.getValue().equals(Long.MIN_VALUE)) {
      RmExceptions.throwException("Raster Id not found for key '%s'", key);
    }
    long result = obj.getValue();
    return result;
  }

}
