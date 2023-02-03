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
  public RasterCells getRasterValues(Long rasterId, Parameter p, Geometry geometry) {
    RasterEntity rasterEntity = this.sourceService.getRaster(rasterId);
    String sourceTitle = rasterEntity.sourceTitle;
    RasterObj rasterObj = this.getRasterObj(rasterEntity, sourceTitle, p);
    if (geometry.getGeometryType().equals("Point")) {
      throw new RuntimeException("Does not support 'Point' geometry type");
    }
    RasterObj subset = rasterObj.getSubsetRaster(sourceTitle, geometry);
    RasterCells result = subset.interleave();
    return result;
  }

  double getRasterValue(Long rasterId, Parameter p, Point point) {
    RasterObj rasterObj = this.getRasterObj(rasterId, p);
    double result = rasterObj.getValue(point);
    return result;
  }

  /**
   *
   * @param rasterId
   * @return
   */
  public RasterObj getRasterObj(Long rasterId, Parameter p) {
    RasterEntity rasterEntity = this.sourceService.getRaster(rasterId);
    String sourceTitle = rasterEntity.sourceTitle;
    RasterObj result = getRasterObj(rasterEntity, sourceTitle, p);
    return result;
  }

  /**
   *
   * @param rasterEntity
   * @param key
   * @return
   */
  private RasterObj getRasterObj(RasterEntity rasterEntity, String key, Parameter p) {
    Bounds bounds = rasterEntity.getBounds();
    Dimensions dims = rasterEntity.getDimensions();
    long typeId = rasterEntity.rasterTypeId;
    Raster raster = this.supplier.builder()
      .setTypeId(typeId)
      .setSourceTitle(key)
      .setBounds(bounds)
      .setDimensions(dims)
      .createRaster(p);
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
  Map<Parameter, Double> getPointRasterValues(long rasterId, List<Parameter> parameters, Point point) {
    Map<Parameter, Double> result = parameters.stream()
      .map(param -> Pair.create(param, this.getRasterValue(rasterId, param, point)))
      .collect(Collectors.toMap(pair -> pair.getKey(), pair -> pair.getValue()));
    return result;
  }
}
