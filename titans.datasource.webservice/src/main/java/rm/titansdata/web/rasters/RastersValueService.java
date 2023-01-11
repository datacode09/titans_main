package rm.titansdata.web.rasters;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
  public RasterCells getRasterValues(Long rasterId, Geometry geometry) {
    RasterEntity rasterEntity = this.sourceService.getRaster(rasterId);
    String sourceTitle = rasterEntity.sourceTitle;
    RasterObj rasterObj = this.getRasterObj(rasterEntity, sourceTitle);
    if (geometry.getGeometryType().equals("Point")) {
      throw new RuntimeException("Does not support 'Point' geometry type");
    }
    RasterObj subset = rasterObj.getSubsetRaster(sourceTitle, geometry);
    RasterCells result = subset.interleave();
    return result;
  }

  double getRasterValue(Long rasterId, Point point) {
    RasterObj rasterObj = this.getRasterObj(rasterId);
    double result = rasterObj.getValue(point);
    return result;
  }

  /**
   *
   * @param rasterId
   * @return
   */
  public RasterObj getRasterObj(Long rasterId) {
    RasterEntity rasterEntity = this.sourceService.getRaster(rasterId);
    String sourceTitle = rasterEntity.sourceTitle;
    RasterObj result = getRasterObj(rasterEntity, sourceTitle);
    return result;
  }

  /**
   *
   * @param rasterEntity
   * @param sourceTitle
   * @return
   */
  private RasterObj getRasterObj(RasterEntity rasterEntity, String sourceTitle) {
    Bounds bounds = rasterEntity.getBounds();
    Dimensions dims = rasterEntity.getDimensions();
    long typeId = rasterEntity.rasterTypeId;
    Raster raster = this.supplier.builder()
      .setTypeId(typeId)
      .setSourceTitle(sourceTitle)
      .setBounds(bounds)
      .setDimensions(dims)
      .createRaster();
    Properties properties = new Properties(bounds, dims.x.length, dims.y.length);
    RasterObj rasterObj = new RasterObj(sourceTitle, properties, raster);
    return rasterObj;
  }

}
