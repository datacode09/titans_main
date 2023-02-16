package rm.titansdata.plugin.classes;

import rm.titansdata.plugin.Clazz;

/**
 *
 * @author Ricardo Marquez
 */
public class ForecastStepClazz implements Clazz {

  public final int step;

  public ForecastStepClazz(int step) {
    this.step = step;
  }

  /**
   *
   * @return
   */
  @Override
  public String getKey() {
    return "" + step;
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {
    return "{" + "step=" + step + '}';
  }
}
