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
  
  public static Unit<Dimensionless> Percent = new BaseUnit("Percent"){
    @Override
    public String toString() {
      return "%";
    }
  };
  public static Unit<Proportion> Proportion = new BaseUnit("Proportion"){
    @Override
    public String toString() {
      return "Proportion";
    }
  };
  
  public static Unit<PerSecond> PerSecond = new BaseUnit("PerSecond"){
    @Override
    public String toString() {
      return "1/s";
    }
  };
  public static Unit<Dimensionless> Fraction = new BaseUnit("Fraction"){
    @Override
    public String toString() {
      return "Fraction";
    }
  };
  
  public static Unit<Dimensionless> Dash = new BaseUnit("Dash"){
    @Override
    public String toString() {
      return "-";
    }
  };
  public static Unit<MassRatio> MassRatio = new BaseUnit("MassRatio"){
    @Override
    public String toString() {
      return "kg/kg";
    }
  };
  public static Unit<?> MassDensityRate = new BaseUnit("MassDensityRate"){
    @Override
    public String toString() {
      return "kg/m2s";
    }
  };    
  public static Unit<?> MassLinearDensity = new BaseUnit("MassLinearDensity"){
    @Override
    public String toString() {
      return "kg/m";
    }
  };    
  public static Unit<?> PowerFlux = new BaseUnit("PowerFlux"){
    @Override
    public String toString() {
      return "W/m2";
    }
  };    
    
  public static Unit<?> ForceFlux = new BaseUnit("ForceFlux"){
    @Override
    public String toString() {
      return "N/m2";
    }
  };    
    
  public static Unit<MassDensity> MassDensity = SI.KILOGRAM.divide(SI.SQUARE_METRE)//
    .asType(MassDensity.class);
  
  public static Unit<EnergyPerMass> EnergyPerMass = SI.JOULE.divide(SI.KILOGRAM)//
    .asType(EnergyPerMass.class);
  
  public static Unit<PressureRate> PressureRate = SI.PASCAL.divide(SI.SECOND)//
    .asType(PressureRate.class);
  
  public static Unit<GeoPotentialHeight> GeoPotentialHeight =  new BaseUnit("GeoPotentialHeight"){
    @Override
    public String toString() {
      return "gpm";
    }
  };
  public static Unit<RadarReflectivity> RadarReflectivity =  new BaseUnit("RadarReflectivity"){
    @Override
    public String toString() {
      return "dB";
    }
  };

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
    
    String alttext = unittext.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\^", "").replaceAll(" ", "");
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
    all.add(Percent);
    all.add(Proportion);
    all.add(GeoPotentialHeight);
    all.add(RadarReflectivity);
    
    
    Unit<?> result = all.stream() //
      .filter(a -> a.toString().equals(alttext)) //
      .findFirst() //
      .orElse(null);
    return result;
  }
}
