package titans.goes;

import java.io.File;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import titans.noaa.core.NoaaImporter;
import titans.noaa.core.NoaaRasterFactory;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class GoesRasterFactory extends NoaaRasterFactory {

  private final File netCdfRootFolder;
  
  /**
   * 
   * @param netCdfRootFolder 
   */
  public GoesRasterFactory(
    @Qualifier("goes.netCdfRootFolder") File netCdfRootFolder) {
    this.netCdfRootFolder = netCdfRootFolder;
  }

  @Override
  public String key() {
    return "GOES-18";
  }

  @Override
  protected NoaaImporter getImporter(int projectId) {
    return new Goes18Importer(netCdfRootFolder, projectId);
  }
}
