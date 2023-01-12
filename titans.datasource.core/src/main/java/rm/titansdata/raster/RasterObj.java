package rm.titansdata.raster;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import java.util.List;
import java.util.Objects;
import javafx.util.Pair;
import javax.measure.Measure;
import javax.measure.quantity.Length;
import rm.titansdata.SridUtils;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.properties.Properties;

/**
 *
 * @author Ricardo Marquez
 */
public class RasterObj {

  private final String name;
  private final Properties properties;
  private final Raster raster;

  public RasterObj(String name, Properties properties, Raster raster) {
    this.name = name;
    this.properties = properties;
    this.raster = raster;
  }

  public String getName() {
    return name;
  }

  public Bounds getBounds() {
    return properties.getBounds();
  }
  
  public int getNumPixels() {
    return getDimensions().numPixels();
  }

  /**
   *
   * @param point
   * @return
   */
  public double getValue(Point point) {
    return this.raster.getValue(point);
  }

  /**
   *
   * @return
   */
  public RasterCells interleave() {
    RasterSearch helper = getRasterSearchHelper();
    double[] values = new double[helper.size()];
    helper.stream(this::getValue)
      .forEach((pair) -> this.interleaveMapping(values, pair));
    return new RasterCells(this.getBounds(), getDimensions(), values);
  }

  /**
   *
   * @return
   */
  public Dimensions getDimensions() {
    return this.properties.getDimensions();
  }

  /**
   *
   * @param p
   * @return
   */
  public RasterObj getSubsetRaster(String name, Geometry p) {
    Geometry correctedEnvelope = this.correctEnvelopeSrid(p);
    List<Cell> cells = this.getCells(correctedEnvelope);
    RasterObj result = this.getSubsetRaster(name, correctedEnvelope, cells);
    return result;
  }

  /**
   *
   * @return
   */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 53 * hash + Objects.hashCode(this.name);
    return hash;
  }

  /**
   *
   * @param obj
   * @return
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final RasterObj other = (RasterObj) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    return true;
  }

  private RasterSearch getRasterSearchHelper() {
    Dimensions dimensions = getDimensions();
    RasterSearch helper = new RasterSearch(this.getBounds(), dimensions);
    return helper;
  }

  private void interleaveMapping(double[] values, Pair<Integer, Cell> pair) {
    int index = pair.getKey();
    Cell cell = pair.getValue();
    values[index] = cell.getValue();
  }

  /**
   *
   * @param correctedEnvelope
   * @param name1
   * @param cells
   * @return
   */
  private RasterObj getSubsetRaster(String name1, // 
    Geometry correctedEnvelope, List<Cell> cells) {
    Dimensions dims = getDimensions();
    Measure<Length> dx = dims.x.length;
    Measure<Length> dy = dims.y.length;
    Envelope envelopeInternal = correctedEnvelope.getEnvelopeInternal();
    Properties newproperties = Properties.create(this.getFactory(), envelopeInternal, dx, dy);
    CellsRaster newraster = new CellsRaster(cells, newproperties.getBounds(), dx, dy);
    RasterObj result = new RasterObj(name1, newproperties, newraster);
    return result;
  }

  /**
   *
   * @param envelope
   * @return
   */
  private List<Cell> getCells(Geometry envelope) {
    RasterSearch helper = this.getRasterSearchHelper();
    List<Cell> cells = helper.getCells(envelope, point -> this.getValue(point));
    return cells;
  }
  
  /**
   * 
   * @param p
   * @return 
   */
  private Geometry correctEnvelopeSrid(Geometry p) {
    int srid = this.getFactory().getSRID();
    Geometry transformed = SridUtils.transform(p, srid);
    Geometry envelope = transformed.getEnvelope();
    return envelope;
  }
  
  /**
   * 
   * @return 
   */
  private GeometryFactory getFactory() {
    GeometryFactory result = this.getBounds().getFactory();
    return result;
  }

  @Override
  public String toString() {
    return "{" + "name=" + name + ", properties=" + properties + '}';
  }
  
  /**
   * 
   * @return 
   */
  public Raster getRaster() {
    return this.raster;
  }

  public static class Builder {

    private String name;
    private Properties properties;
    private Raster raster;

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setProperties(Properties properties) {
      this.properties = properties;
      return this;
    }

    public Builder setRaster(Raster raster) {
      this.raster = raster;
      return this;
    }

    /**
     *
     * @return
     */
    public RasterObj build() {
      RasterObj result = new RasterObj(name, properties, this.raster);
      return result;
    }
  }

}
