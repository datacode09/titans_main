package rm.titansdata.web.rasters.colormap;

import common.db.DbConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import rm.titansdata.colormap.ColorMap;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class ColorMapService {

  @Autowired
  @Qualifier("titans.db")
  private DbConnection dbconn;
  
  /**
   * 
   * @param userId
   * @param rasterId
   * @return 
   */
  public ColorMap getColorMap(Long userId, Long rasterId) {
    return null;
  }
  
  /**
   * 
   * @param colorMap
   * @param userId
   * @param rasterId 
   */
  public void saveColorMap(ColorMap colorMap, Long userId, Long rasterId) {
    
  }
}
