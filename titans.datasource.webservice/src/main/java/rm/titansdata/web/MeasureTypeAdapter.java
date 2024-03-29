package rm.titansdata.web;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import javax.measure.Measure;

/**
 *
 * @author Ricardo Marquez
 */
public class MeasureTypeAdapter extends TypeAdapter<Measure<?>> {
  
  /**
   * 
   * @param writer
   * @param t
   * @throws IOException 
   */
  @Override
  public void write(JsonWriter writer, Measure<?> t) throws IOException {
    writer.beginObject();
    if (t != null && !Double.isNaN(t.getValue().doubleValue())) {
      writer.name("value").value(t.getValue());
      writer.name("unit").value(t.getUnit().toString());
    }
    writer.endObject();
    writer.flush();
  }
  
  /**
   * 
   * @param reader
   * @return
   * @throws IOException 
   */
  @Override
  public Measure read(JsonReader reader) throws IOException {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
