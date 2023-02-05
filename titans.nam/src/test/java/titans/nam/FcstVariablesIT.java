package titans.nam;

import java.io.File;
import java.util.Set;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import titans.nam.grib.GribFileVarsReader;

/**
 *
 * @author Ricardo Marquez
 */
@RunWith(JUnitParamsRunner.class)
public class FcstVariablesIT {
  
  @Test
  @Parameters("")
  public void test(String nothing) {
    File gribFile = new File("G:\\tests\\data\\grib\\nam.t00z.conusnest.hiresf01.tm00.grib2");
    File degribExe = new File("C:\\ndfd\\degrib\\bin\\degrib.exe");
    GribFileVarsReader reader = new GribFileVarsReader(degribExe, gribFile); 
    Set<String> varNames = reader.parseVarNames();
    varNames.forEach(System.out::println);
  }
  
  @Test
  @Parameters({
    "TMP_2-HTGL, 616.0", 
    "TMP_85000-ISBL, 484.0"
  })
  public void extractVarMsgNumber(String varName, String expresult) {
    File gribFile = new File("G:\\tests\\data\\grib\\nam.t00z.conusnest.hiresf01.tm00.grib2");
    File degribExe = new File("C:\\ndfd\\degrib\\bin\\degrib.exe");
    GribFileVarsReader reader = new GribFileVarsReader(degribExe, gribFile); 
    String varMsgNum = reader.getVarMsgNumber(varName);
    Assert.assertEquals(expresult, varMsgNum);
  }
}
