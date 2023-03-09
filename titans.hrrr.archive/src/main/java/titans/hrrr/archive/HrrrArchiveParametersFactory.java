package titans.hrrr.archive;

import common.types.DateTimeRange;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.measure.quantity.Temperature;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import titans.hrrr.core.grib.HrrrCombinedInventoryReader;
import titans.nam.NoaaFcstParameterFactory;
import titans.nam.NoaaParameter;
import titans.noaa.core.FcstDateRange;
import titans.noaa.core.InventoryReader;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class HrrrArchiveParametersFactory extends NoaaFcstParameterFactory {
  
  /**
   * 
   * @return 
   */
  @Override
  protected InventoryReader getInventoryReader() {
    return new HrrrCombinedInventoryReader();
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
   * @param range
   * @return 
   */
  @Override
  protected List<Parameter> getParameters(FcstDateRange range) {
    List<Parameter> result;
    int fcststep = range.fcststep;
    ZonedDateTime zonedDateTime = range.datetime1;
    ZonedDateTime zonedDateTime2 = range.datetime2;
    String namVar = "TMP_2-HTGL";
    Unit<Temperature> unit = SI.CELSIUS;
    if (fcststep == -1) {
      result = IntStream.range(0, 49)
        .mapToObj(d -> new NoaaParameter(this.key(), zonedDateTime, d, namVar, unit))
        .collect(Collectors.toList());
    } else if (zonedDateTime2 == null) {
      result = new ArrayList<>();
      result.add(new NoaaParameter(this.key(), zonedDateTime, fcststep, namVar, unit));
    } else {
      Duration ofHours = Duration.ofHours(1);
      DateTimeRange dateTimeRange = new DateTimeRange(zonedDateTime, zonedDateTime2);
      result = new ArrayList<>();
      dateTimeRange.iterator(ofHours).forEach(d -> {
        result.add(new NoaaParameter(this.key(), d, fcststep, namVar, unit));
      });
    }
    return result;
  }

}
