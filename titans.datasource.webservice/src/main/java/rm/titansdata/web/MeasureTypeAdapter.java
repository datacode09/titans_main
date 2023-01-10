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
public class MeasureTypeAdapter extends TypeAdapter<Measure> {

  @Override
  public void write(JsonWriter writer, Measure t) throws IOException {
    writer.beginObject();
    if (t != null && !Double.isNaN(t.getValue().doubleValue())) {
      writer.name("value").value(t.getValue());
      writer.name("unit").value(t.getUnit().toString());
    }
    writer.endObject();
    writer.flush();
  }

  @Override
  public Measure read(JsonReader reader) throws IOException {
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
