package titans.nam.grib;

/**
 *
 * @author Ricardo Marquez
 */
public class ForecastTimeReference {
  public int refhour;
  public int fcsthourAhead;

  public ForecastTimeReference(int refhour, int fcsthour) {
    this.refhour = refhour;
    this.fcsthourAhead = fcsthour;
  }

  @Override
  public String toString() {
    return "{" + "refhour=" + refhour + ", fcsthourAhead=" + fcsthourAhead + '}';
  }
  
}
