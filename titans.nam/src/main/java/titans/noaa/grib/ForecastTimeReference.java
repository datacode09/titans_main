package titans.noaa.grib;

/**
 *
 * @author Ricardo Marquez
 */
public class ForecastTimeReference {
  public int refhour;
  public int fcsthourAhead;

  /**
   * 
   * @param refhour
   * @param fcsthour 
   */
  public ForecastTimeReference(int refhour, int fcsthour) {
    this.refhour = refhour;
    this.fcsthourAhead = fcsthour;
  }

  @Override
  public String toString() {
    return "{" + "refhour=" + refhour + ", fcsthourAhead=" + fcsthourAhead + '}';
  }
  
}
