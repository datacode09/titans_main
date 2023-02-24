package titans.hrrr;

import java.io.File;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import titans.hrrr.core.HrrrImporter;
import titans.noaa.core.NoaaImporter;
import titans.noaa.core.NoaaRasterFactory;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@DependsOn({"hrrr.gribRootFolder", "hrrr.degribExe"})
public class HrrrRasterFactory extends NoaaRasterFactory {
  
  private final HrrrImporter.Builder hrrrImporterBuilder;
  
  /**
   * 
   * @param gribRootFolder
   * @param netCdfRootFolder
   * @param degribExe 
   */
  public HrrrRasterFactory(
    @Qualifier("hrrr.gribRootFolder") File gribRootFolder,
    @Qualifier("hrrr.netCdfRootFolder") File netCdfRootFolder,
    @Qualifier("hrrr.degribExe") File degribExe
  ) {
    this.hrrrImporterBuilder = new HrrrImporter.Builder()
      .setDegribExe(degribExe).setGribRootFolder(gribRootFolder).setNetCdfRootFolder(netCdfRootFolder)
      ;
  }

  /**
   *
   * @return
   */
  @Override
  public String key() {
    String key = "High Resolution Rapid Refresh";
    return key;
  }
  
  
  /**
   * 
   * @param projectId
   * @return 
   */
  @Override
  protected NoaaImporter getImporter(int projectId) {
    return this.hrrrImporterBuilder
      .setSubfolderId(projectId).build();
  }

}
