package rm.titansdata.heirarchy;

import java.util.Objects;
import rm.titansdata.raster.RasterObj;

/**
 *
 * @author Ricardo Marquez
 */
public class Node {
  
  private final RasterObj rasterObj;

  /**
   * 
   * @param rasterObj 
   */
  public Node(RasterObj rasterObj) {
    this.rasterObj = rasterObj;
  }
  
  /**
   * 
   * @return 
   */
  public RasterObj getRasterObj() {
    return rasterObj;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 17 * hash + Objects.hashCode(this.rasterObj);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Node other = (Node) obj;
    if (!Objects.equals(this.rasterObj, other.rasterObj)) {
      return false;
    }
    return true;
  }
  
  
  
  /**
   * 
   * @param rasterObj
   * @return 
   */
  public static Node create(RasterObj rasterObj) {
    Node result = new Node(rasterObj);
    return result;
  }

  /**
   * 
   * @return 
   */
  public String getName() {
    return this.getRasterObj().getName();
  }
  
}
