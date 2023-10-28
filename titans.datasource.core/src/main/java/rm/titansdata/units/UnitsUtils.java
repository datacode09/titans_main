package rm.titansdata.units;

import java.util.HashSet;
import java.util.Set;
import javax.measure.quantity.Dimensionless;
import javax.measure.unit.BaseUnit;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

/**
 *
 * @author Ricardo Marquez
 */
public class UnitsUtils {
  public static Unit<Dimensionless> Percent = new BaseUnit("%");
  public static Unit<Proportion> Proportion = new BaseUnit("Proportion");
  public static Unit<PerSecond> PerSecond = new BaseUnit("1/s");
  public static Unit<Dimensionless> Fraction = new BaseUnit("Fraction");
  public static Unit<Dimensionless> Dash = new BaseUnit("-");
  public static Unit<MassRatio> MassRatio = new BaseUnit("kg/kg");
  public static Unit<?> MassDensityRate = new BaseUnit("kg/m2s");    
  public static Unit<?> MassLinearDensity = new BaseUnit("kg/m");    
  public static Unit<?> PowerFlux = new BaseUnit("W/m2");
  public static Unit<?> ForceFlux = new BaseUnit("N/m2");    
    
  public static Unit<MassDensity> MassDensity = SI.KILOGRAM.divide(SI.SQUARE_METRE)//
    .asType(MassDensity.class);
  
  public static Unit<EnergyPerMass> EnergyPerMass = SI.JOULE.divide(SI.KILOGRAM)//
    .asType(EnergyPerMass.class);
  
  public static Unit<PressureRate> PressureRate = SI.PASCAL.divide(SI.SECOND)//
    .asType(PressureRate.class);
  
  public static Unit<GeoPotentialHeight> GeoPotentialHeight =  new BaseUnit("gpm");
  public static Unit<RadarReflectivity> RadarReflectivity =  new BaseUnit("dB");

  /**
   *
   */
  private UnitsUtils() {
  }

  /**
   *
   * @param unittext
   * @return
   */
  public static Unit<?> valueOf(String unittext) {
    String alttext = unittext
      .replaceAll("\\(", "") //
      .replaceAll("\\)", "") //
      .replaceAll("\\^", "") //
      .replaceAll(" ", "");
    Set<Unit<?>> all = new HashSet<>(SI.getInstance().getUnits());
    all.add(Dash);
    all.add(EnergyPerMass);
    all.add(Fraction);
    all.add(MassDensity);
    all.add(MassDensityRate);
    all.add(MassLinearDensity);
    all.add(MassRatio);
    all.add(PerSecond);
    all.add(Percent);
    all.add(PowerFlux);
    all.add(PressureRate);
    all.add(Proportion);
    all.add(GeoPotentialHeight);
    all.add(RadarReflectivity);
    Unit<?> result = all.stream() //
      .filter(a -> hasMatchingSymbol(a, alttext)) //
      .findFirst() //
      .orElse(null);
    return result;
  }
  
  /**
   * 
   * @param a
   * @param alttext
   * @return 
   */
  private static boolean hasMatchingSymbol(Unit<?> a, String alttext) {
    if (a == null) {
      return false;
    }
    boolean result = (a instanceof BaseUnit)  //
      ? ((BaseUnit) a).getSymbol().equals(alttext) //
      : a.toString().equals(alttext); 
    return result; 
  }
}
