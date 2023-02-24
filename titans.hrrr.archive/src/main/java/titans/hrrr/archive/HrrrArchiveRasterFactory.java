package titans.hrrr.archive;

import titans.hrrr.archive.core.HrrrArchiveImporter;
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
public class HrrrArchiveRasterFactory extends NoaaRasterFactory {

  private final HrrrArchiveImporter.Builder hrrrImporterBuilder;

  public HrrrArchiveRasterFactory(@Qualifier("hrrr.arch.gribRootFolder") File gribRootFolder,
    @Qualifier("hrrr.arch.netCdfRootFolder") File netCdfRootFolder,
    @Qualifier("hrrr.arch.degribExe") File degribExe) {
    this.hrrrImporterBuilder = new HrrrArchiveImporter.Builder()
      .setDegribExe(degribExe).setGribRootFolder(gribRootFolder).setNetCdfRootFolder(netCdfRootFolder); 
    
  }
  
  /**
   *
   * @return
   */
  @Override
  public String key() {
    String key = "High Resolution Rapid Refresh (Archived)";
    return key;
  }

  /**
   *
   * @param projectId
   * @return
   */
  @Override
  protected NoaaImporter getImporter(int projectId) {
    return this.hrrrImporterBuilder.setSubfolderId(projectId).build();
  }
}
