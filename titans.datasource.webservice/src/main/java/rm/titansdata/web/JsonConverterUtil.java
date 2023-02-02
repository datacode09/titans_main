package rm.titansdata.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.locationtech.jts.geom.Point;
import javax.measure.Measure;
import javax.measure.quantity.Angle;
import javax.measure.quantity.ElectricCurrent;
import javax.measure.quantity.ElectricPotential;
import javax.measure.quantity.ElectricResistance;
import javax.measure.quantity.Length;
import javax.measure.quantity.Temperature;
import javax.measure.quantity.Velocity;

/**
 *
 * @author Ricardo Marquez
 */
public class JsonConverterUtil {  
  
  public static String toJson(Object obect) {     
    Gson gson = new GsonBuilder()
      .registerTypeAdapter((new TypeToken<Measure<ElectricCurrent>>() {}).getType(), new MeasureTypeAdapter())
      .registerTypeAdapter((new TypeToken<Measure<Temperature>>() {}).getType(), new MeasureTypeAdapter())
      .registerTypeAdapter((new TypeToken<Measure<Velocity>>() {}).getType(), new MeasureTypeAdapter())
      .registerTypeAdapter((new TypeToken<Measure<Length>>() {}).getType(), new MeasureTypeAdapter())
      .registerTypeAdapter((new TypeToken<Measure<Angle>>() {}).getType(), new MeasureTypeAdapter()) 
//      .registerTypeAdapter((new TypeToken<Measure<PowerFlux>>() {}).getType(), new MeasureTypeAdapter())
//      .registerTypeAdapter((new TypeToken<Measure<LinearPowerFlux>>() {}).getType(), new MeasureTypeAdapter())
      .registerTypeAdapter((new TypeToken<Measure<ElectricResistance>>() {}).getType(), new MeasureTypeAdapter())
      .registerTypeAdapter((new TypeToken<Measure<ElectricPotential>>() {}).getType(), new MeasureTypeAdapter())
      .registerTypeAdapter((new TypeToken<Point>() {}).getType(), new PointTypeAdapter())
      .serializeSpecialFloatingPointValues()
      .create();
    return gson.toJson(obect); 
  }

}
