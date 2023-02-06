package titans.nam.grib;

import java.util.ArrayList;
import java.util.List;
import titans.nam.NoaaParameter;

/**
 *
 * @author Ricardo Marquez
 */
public class NamsHtmlParser {

  private final String htmlText;
  
  public NamsHtmlParser(String htmlText) {
    this.htmlText = htmlText;
  }
  
  
  /**
   * nam.t%sz.conusnest.hiresf%s.tm00.grib2
   * @return 
   */
  public List<NoaaParameter> parse() {
    List<NoaaParameter> result = new ArrayList<>(); 
    return result;
  }
}
