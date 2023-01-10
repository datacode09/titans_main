package rm.titansdata.web.rasters;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import rm.titansdata.RasterFactory;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class RasterModelsRegistry implements InitializingBean {

  private final Map<String, RasterFactory> map = new HashMap<>();

  public RasterModelsRegistry() {
  }
  
  /**
   * 
   * @throws Exception 
   */
  @Override
  public void afterPropertiesSet() throws Exception {
  }
  
  

  /**
   *
   * @param sourceTitle
   * @return
   */
  public RasterFactory get(String sourceTitle) {
    RasterFactory result = this.map.get(sourceTitle);
    return result;
  }

  /**
   * 
   * @param key
   * @param bean 
   */
  public void put(String key, RasterFactory bean) {
    this.map.put(key, bean);    
  }
}
