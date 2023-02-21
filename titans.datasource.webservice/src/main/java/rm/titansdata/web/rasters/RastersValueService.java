package rm.titansdata.web.rasters;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.math3.util.Pair;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.properties.Properties;
import rm.titansdata.raster.Raster;
import rm.titansdata.raster.RasterCells;
import rm.titansdata.raster.RasterObj;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class RastersValueService {

  @Autowired
  private RastersSourceService sourceService;

  @Autowired
  private RasterFactorySupplier supplier;

  /**
   *
   * @param rasterId
   * @param geometry
   * @return
   */
  public RasterCells getRasterValues(Long rasterId, int projectId, Parameter p, Geometry geometry) {
    RasterEntity rasterEntity = this.sourceService.getRaster(rasterId);
    String sourceTitle = rasterEntity.sourceTitle;
    RasterObj rasterObj = this.getRasterObj(rasterEntity, sourceTitle, projectId, p);
    if (geometry.getGeometryType().equals("Point")) {
      throw new RuntimeException("Does not support 'Point' geometry type");
    }
    RasterObj subset = rasterObj.getSubsetRaster(sourceTitle, geometry);
    RasterCells result = subset.interleave();
    return result;
  }
  
  /**
   * 
   * @param rasterId
   * @param p
   * @param point
   * @return 
   */
  double getRasterValue(Long rasterId, int projectId, Parameter p, Point point) {
    RasterEntity rasterEntity = this.sourceService.getRaster(rasterId);
    RasterObj rasterObj = this.getRasterObj(rasterEntity, rasterEntity.sourceTitle,  //
      projectId, p, rasterEntity.getBounds()); 
    double result = rasterObj.getValue(point);
    return result;
  }

  /**
   *
   * @param rasterId
   * @return
   */
  public RasterObj getRasterObj(Long rasterId,  int projectId, Parameter p, Bounds bounds) {
    RasterEntity rasterEntity = this.sourceService.getRaster(rasterId);
    String sourceTitle = rasterEntity.sourceTitle;
    RasterObj result = this.getRasterObj(rasterEntity, sourceTitle, projectId, p, bounds);
    return result;
  }

  private RasterObj getRasterObj(RasterEntity rasterEntity, String key, int projectId, Parameter p) {
    return this.getRasterObj(rasterEntity, key, projectId, p, rasterEntity.getBounds());
  }

  /**
   *
   * @param rasterEntity
   * @param key
   * @return
   */
  private RasterObj getRasterObj(RasterEntity rasterEntity, String key, int projectId, // 
    Parameter p, Bounds bounds) {
    Dimensions dims = rasterEntity.getDimensions();
    long typeId = rasterEntity.rasterTypeId;
    Raster raster = this.supplier.builder()
      .setTypeId(typeId)
      .setSourceTitle(key)
      .setBounds(bounds)
      .setDimensions(dims)
      .createRaster(projectId, p);
    Properties properties = new Properties(bounds, dims.x.length, dims.y.length);
    RasterObj rasterObj = new RasterObj(key, properties, raster);
    return rasterObj;
  }

  /**
   *
   * @param rasterId
   * @param parameters
   * @param point
   * @return
   */
  Map<Parameter, Double> getPointRasterValues( //
    long rasterId, int projectId,  List<Parameter> parameters, Point point) {
    Map<Parameter, Double> result = parameters.stream()
      .map(param -> Pair.create(param, this.getRasterValue(rasterId, projectId, param, point)))
      .collect(Collectors.toMap(pair -> pair.getKey(), pair -> pair.getValue()));
    return result;
  }
}
