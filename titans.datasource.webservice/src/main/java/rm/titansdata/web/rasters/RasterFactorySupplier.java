package rm.titansdata.web.rasters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.raster.Raster;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class RasterFactorySupplier {

  @Autowired
  private RasterModelsRegistry rasterModelsRegistry;
  
  /**
   * 
   * @return 
   */
  public Builder builder() {
    Builder result = new Builder(this.rasterModelsRegistry);
    return result;
  }

  public static class Builder {
    private final RasterModelsRegistry rasterModelsRegistry; 
    private Bounds bounds;
    private Dimensions dims;
    private long typeId;
    private String sourceTitle;

    private Builder(RasterModelsRegistry rasterModelsRegistry) {
      this.rasterModelsRegistry = rasterModelsRegistry;
    }
    
    public Builder setBounds(Bounds bounds) {
      this.bounds = bounds;
      return this;
    }

    public Builder setDimensions(Dimensions dims) {
      this.dims = dims;
      return this;
    }

    public Builder setTypeId(long typeId) {
      this.typeId = typeId;
      return this;
    }

    public Builder setSourceTitle(String sourceTitle) {
      this.sourceTitle = sourceTitle;
      return this;
    }

    /**
     *
     * @return
     */
    public Raster createRaster() {
      Raster result;
      switch (Long.valueOf(this.typeId).intValue()) {
        case 0:
          result = rasterModelsRegistry.get(this.sourceTitle).create(bounds, dims);
          break;
        default:
          throw new RuntimeException();
      }
      return result;
    }
  }
}
