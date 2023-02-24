package titans.hrrr.archive;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.measure.unit.SI;
import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import titans.hrrr.core.grib.HrrrInventoryReader;
import titans.nam.NoaaFcstParameterFactory;
import titans.nam.NoaaParameter;
import titans.noaa.core.InventoryReader;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class HrrrArchiveParametersFactory extends NoaaFcstParameterFactory {

  @Override
  protected InventoryReader getInventoryReader() {
    return new HrrrInventoryReader();
  }

  @Override
  protected List<Parameter> getParameters(ZonedDateTime zonedDateTime, int fcststep) {
    List<Parameter> result;
    if (fcststep == -1) {
      String namVar = "TMP_2-HTGL";
      result = IntStream.range(0, 49)
        .mapToObj(d->new NoaaParameter(this.key(), zonedDateTime, d, namVar, SI.CELSIUS))
        .collect(Collectors.toList()); 
    } else {
      String namVar = "TMP_2-HTGL";
      result = new ArrayList<>();
      result.add(new NoaaParameter(this.key(), zonedDateTime, fcststep, namVar, SI.CELSIUS));
    }
    return result;
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

}
