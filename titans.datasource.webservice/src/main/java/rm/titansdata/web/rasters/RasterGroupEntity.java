package rm.titansdata.web.rasters;

/**
 *
 * @author Ricardo Marquez
 */
public class RasterGroupEntity {
  
  public final Long rasterGroupId;
  public final String name;
  
  public RasterGroupEntity(Long rasterGroupId, String name) {
    this.rasterGroupId = rasterGroupId;
    this.name = name;
  }
  
  
  @Override
  public String toString() {
    return "{" + "rasterGroupId=" + rasterGroupId + ", name=" + name + '}';
  }
  
  
  
}
