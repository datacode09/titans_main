package titans.noaa.core;

import java.time.ZonedDateTime;

/**
 *
 * @author Ricardo Marquez
 */
public class FcstDateRange {

  public final ZonedDateTime datetime1;
  public final ZonedDateTime datetime2;
  public final int fcststep;

  public FcstDateRange(ZonedDateTime datetime1, ZonedDateTime datetime2, int fcststep) {
    this.datetime1 = datetime1;
    this.datetime2 = datetime2;
    this.fcststep = fcststep;
  }

  @Override
  public String toString() {
    return "{" + "datetime1=" + datetime1 + ", datetime2=" + datetime2 + ", fcststep=" + fcststep + '}';
  }
  
}
