package titans.nam;

import java.io.File;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import titans.nam.core.NamImporter;
import titans.nam.core.NoaaImporter;
import titans.nam.core.NoaaRasterFactory;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@DependsOn({"nam.gribRootFolder", "nam.degribExe"}) 
public class NamRasterFactory extends NoaaRasterFactory {
  private final NamImporter.Builder namImporterBuilder;
  
  /**
   *
   * @param forecaststep
   * @param datetimeref
   */
  public NamRasterFactory(
    @Qualifier("nam.gribRootFolder") File gribRootFolder, 
    @Qualifier("nam.netCdfRootFolder") File netCdfRootFolder, 
    @Qualifier("nam.degribExe") File degribExe) {
    this.namImporterBuilder = new NamImporter.Builder().setGribRootFolder(gribRootFolder).setNetCdfRootFolder(netCdfRootFolder).setDegribExe(degribExe);
  }
  
  /**
   *
   * @return
   */
  @Override
  public String key() {
    String key = "North American Model Forecasts";
    return key;
  }
  
  /**
   * 
   * @param projectId
   * @return 
   */
  @Override
  protected NoaaImporter getImporter(int projectId) {
    NamImporter result = this.namImporterBuilder //
      .setSubfolderId(projectId)//
      .build(); //
    return result; 
  }

  
}
