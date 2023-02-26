package titans.mrms;

import common.types.DateTimeRange;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.measure.unit.Unit;
import org.springframework.stereotype.Component;
import rm.titansdata.Parameter;
import titans.nam.NoaaFcstParameterFactory;
import titans.nam.NoaaParameter;
import titans.noaa.core.FcstDateRange;
import titans.noaa.core.InventoryReader;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class MrmsParametersFactory extends NoaaFcstParameterFactory {

  public MrmsParametersFactory() {
  }

  /**
   *
   * @return
   */
  @Override
  public String key() {
    return "Multi-Radar Multi-Sensor";
  }

  /**
   *
   * @return
   */
  @Override
  protected InventoryReader getInventoryReader() {
    return new MrmsInventoryReader();
  }

  /**
   *
   * @param range
   * @return
   */
  @Override
  protected List<Parameter> getParameters(FcstDateRange range) {
    int fcststep = range.fcststep;
    ZonedDateTime zonedDateTime = range.datetime1;
    ZonedDateTime zonedDateTime2 = range.datetime2;
    String namVar = "";
    Unit<?> unit = Unit.ONE;
    List<Parameter> result;
    if (zonedDateTime2 == null) {
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
