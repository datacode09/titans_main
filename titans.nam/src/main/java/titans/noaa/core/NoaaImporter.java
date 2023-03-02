package titans.noaa.core;

import java.time.ZonedDateTime;
import rm.titansdata.raster.RasterObj;

/**
 *
 * @author Ricardo Marquez
 */
public interface NoaaImporter {

  RasterObj getRaster(NoaaVariable var, ZonedDateTime datetime, int fcststep);
  
}
