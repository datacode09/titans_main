package titans.noaa.netcdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import javax.measure.Measure;
import javax.measure.quantity.Length;
import javax.measure.quantity.Quantity;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.DoubleRange;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import rm.titansdata.SridUtils;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimension;
import rm.titansdata.properties.Dimensions;
import rm.titansdata.units.UnitsUtils;
import titans.nam.NoaaParameter;
import titans.noaa.core.NoaaVariable;
import ucar.nc2.dataset.VariableDS;
import ucar.nc2.dt.GridDataset.Gridset;
import ucar.nc2.dt.grid.GridDataset;
import ucar.unidata.geoloc.LatLonRect;

/**
 *
 * @author Ricardo Marquez
 */
public class NetCdfFile {

  private final String varName;
  public final File file;

  private NetCdfFile(String varName, File file) {
    this.varName = varName;
    this.file = file;
  }

  /**
   *
   * @return
   */
  public Dimensions getDimensions() {
    try (GridDataset gds = GridDataset.open(file.getAbsolutePath())) {
      Gridset gridSet = gds.getGridsets().get(0);
      List<ucar.nc2.Dimension> domain = gridSet.getGeoCoordSystem().getDomain();
      ucar.nc2.Dimension x = domain.get(0);
      ucar.nc2.Dimension y = domain.get(1);
      int pixelsX = x.getLength();
      int pixelsY = y.getLength();
      double width = this.getBounds(gds).getLengthX() / pixelsX;
      double height = this.getBounds(gds).getLengthY() / pixelsY;
      Measure<Length> measureX = Measure.valueOf(width, SI.METRE);
      Measure<Length> measureY = Measure.valueOf(height, SI.METRE);
      Dimension dimensionx = new Dimension(measureX, pixelsX);
      Dimension dimensiony = new Dimension(measureY, pixelsY);
      Dimensions result = new Dimensions(dimensionx, dimensiony);
      return result;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   *
   * @return
   */
  public Bounds getBounds() {
    try (GridDataset gds = GridDataset.open(file.getAbsolutePath())) {
      return this.getBounds(gds);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   *
   * @param gds
   * @return
   */
  private Bounds getBounds(final GridDataset gds) {
    LatLonRect bbox = gds.getBoundingBox();
    PrecisionModel precisionModel = new PrecisionModel(PrecisionModel.FLOATING);
    GeometryFactory factory = new GeometryFactory(precisionModel, 4326);
    Coordinate c1 = new Coordinate(bbox.getLonMin(), bbox.getLatMin());
    Coordinate c2 = new Coordinate(bbox.getLonMax(), bbox.getLatMax());
    Point lowerleft = SridUtils.transform(factory.createPoint(c1), 3857);
    Point upperright = SridUtils.transform(factory.createPoint(c2), 3857);
    Bounds result = new Bounds(lowerleft, upperright);
    return result;
  }

  /**
   *
   * @return
   */
  public String getVarName() {
    return this.varName;
  }

  /**
   *
   * @return
   */
  public boolean exists() {
    return this.file.exists();
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 53 * hash + Objects.hashCode(this.file);
    return hash;
  }

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
    final NetCdfFile other = (NetCdfFile) obj;
    if (!Objects.equals(this.file, other.file)) {
      return false;
    }
    return true;
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {
    return "NetCdfFile{" + "varName=" + varName + ", file=" + file + '}';
  }

  /**
   *
   * @return
   */
  public Unit<? extends Quantity> getUnits() {
    Unit<? extends Quantity> result;
    try (GridDataset gds = GridDataset.open(file.getAbsolutePath())) {
      VariableDS datavariable = this.getDataVariable(gds);
      String unitsString = datavariable.getUnitsString();
      if (unitsString == null) {
        result = Unit.ONE;
      } else {
        result = UnitsUtils.valueOf(unitsString);
      }
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    return result;
  }

  /**
   *
   * @return
   */
  public DoubleRange getValueRange() {
    try (GridDataset gds = GridDataset.open(file.getAbsolutePath())) {
      VariableDS datavariable = this.getDataVariable(gds);
      double min = datavariable.getValidMin();
      double max = datavariable.getValidMax();
      DoubleRange result = new DoubleRange(min, max);
      return result;
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   *
   * @param gds
   * @return
   */
  private VariableDS getDataVariable(final GridDataset gds) {
    String replace = varName.replace("-", "_");
    VariableDS result = (VariableDS) gds.getDataVariable(replace);
    if (result == null) {
      result = (VariableDS) gds.getDataVariables().get(0);
    }
    return result;
  }

  /**
   *
   * @param baseFolder
   * @param namParameter
   * @return
   */
  public static NetCdfFile create(File baseFolder, // 
    int subFolderId, // 
    NoaaParameter namParameter) {
    NoaaVariable var = new NoaaVariable(namParameter.noaaVar, namParameter.getUnit());
    String varName = var.getGribVarName();
    NetCdfFileOrganization org = new NetCdfFileOrganization(
      baseFolder, subFolderId, namParameter.fcststep, namParameter.datetime, var);
    File file = org.getFile();
    NetCdfFile instance = new NetCdfFile(varName, file);
    return instance;
  }

  /**
   *
   * @param varName
   * @param file
   * @return
   */
  public static NetCdfFile test(String varName, File file) {
    return new NetCdfFile(varName, file);
  }

  /**
   *
   * @param inputStream
   */
  public void save(InputStream inputStream) {
    try {
      this.createFileIfNotExists();
      FileOutputStream outputStream = this.getFileOutputStream();
      IOUtils.copy(inputStream, outputStream);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  /**
   * 
   * @return 
   */
  private FileOutputStream getFileOutputStream() {
    try {
      return new FileOutputStream(this.file);
    } catch (FileNotFoundException ex) {
      throw new RuntimeException(ex);
    }
  }

  private void createFileIfNotExists() throws IOException {
    if (!this.exists()) {
      this.file.getParentFile().mkdirs();
      this.file.createNewFile();
    }
  }

}
