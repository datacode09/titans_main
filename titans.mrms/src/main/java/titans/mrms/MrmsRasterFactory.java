package titans.mrms;

import java.io.File;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import titans.noaa.core.NoaaGribImporter;
import titans.noaa.core.NoaaRasterFactory;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class MrmsRasterFactory extends NoaaRasterFactory {

  private final MrmsImporter.Builder importer;

  public MrmsRasterFactory(
    @Qualifier("mrms.gribRootFolder") File gribRootFolder, 
    @Qualifier("mrms.netCdfRootFolder") File netCdfRootFolder, 
    @Qualifier("mrms.degribExe") File degribExe) {
    this.importer = new MrmsImporter.Builder().setGribRootFolder(gribRootFolder).setNetCdfRootFolder(netCdfRootFolder).setDegribExe(degribExe);
  }

  @Override
  public String key() {
    return "Multi-Radar Multi-Sensor"; 
  }
  
  /**
   * 
   * @param projectId
   * @return 
   */
  @Override
  protected NoaaGribImporter getImporter(int projectId) {
    return this.importer.setSubfolderId(projectId).build();
  }
}
