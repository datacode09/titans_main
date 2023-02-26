package titans.mrms;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.measure.unit.Unit;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import rm.titansdata.Parameter;
import rm.titansdata.plugin.Clazz;
import titans.noaa.core.NoaaDateClazz;
import titans.noaa.core.NoaaVarClazz;

/**
 *
 * @author Ricardo Marquez
 */
public class ParameterFactoryIT extends BaseSpringITest {
  
  @Autowired
  private MrmsParametersFactory factory;
  
  @Test
  public void test() {
    String varName = "MultiSensor_QPE_01H_Pass2_00.00";
    String datetext = "2022-12-03T10:00:00";
    LocalDateTime localdatetime = LocalDateTime.parse(datetext, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    ZoneId zoneId = ZoneId.of("UTC");
    ZonedDateTime datetime = ZonedDateTime.of(localdatetime, zoneId);
    Clazz[] range = new Clazz[]{
      new NoaaVarClazz(varName, Unit.ONE)
      , new NoaaDateClazz(datetime)
    };
    List<Parameter> parameters = factory.getParameters(range); 
    System.out.println("parameters = " + parameters.size());
  }  
}
