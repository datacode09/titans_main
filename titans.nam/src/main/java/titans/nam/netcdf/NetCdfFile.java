package titans.nam.netcdf;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import java.io.File;
import java.io.IOException;
import javax.measure.Measure;
import javax.measure.quantity.Length;
import javax.measure.unit.SI;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimension;
import rm.titansdata.properties.Dimensions;
import ucar.nc2.dt.grid.GridDataset;
import ucar.unidata.geoloc.LatLonRect;

/**
 *
 * @author Ricardo Marquez
 */
public class NetCdfFile {

  final File file;
  private final String varName;

  public NetCdfFile(File file, String varName) {
    this.file = file;
    this.varName = varName;
  }

  /**
   *
   * @return
   */
  Dimensions getDimensions() {
    try (GridDataset gds = GridDataset.open(file.getAbsolutePath())) {
      LatLonRect bbox = gds.getBoundingBox();
      double height = bbox.getHeight();
      double width = bbox.getWidth();
      ucar.nc2.Dimension x = gds.getGridsets().get(0).getGeoCoordSystem().getDomain().get(0);
      ucar.nc2.Dimension y = gds.getGridsets().get(0).getGeoCoordSystem().getDomain().get(1);
      Measure<Length> measureX = Measure.valueOf(width, SI.KILOMETRE);
      Measure<Length> measureY = Measure.valueOf(height, SI.KILOMETRE);
      Dimension dimensiony = new Dimension(measureX, x.getLength());
      Dimension dimensionx = new Dimension(measureY, y.getLength());
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
  Bounds getBounds() {
    try (GridDataset gds = GridDataset.open(file.getAbsolutePath())) {
      LatLonRect bbox = gds.getBoundingBox();
      PrecisionModel precisionModel = new PrecisionModel(PrecisionModel.FLOATING);
      GeometryFactory factory = new GeometryFactory(precisionModel, 4326);
      Coordinate c1 = new Coordinate(bbox.getLonMin(), bbox.getLatMin());
      Coordinate c2 = new Coordinate(bbox.getLonMax(), bbox.getLatMax());
      Point lowerleft = factory.createPoint(c1);
      Point upperright = factory.createPoint(c2);
      Bounds result = new Bounds(lowerleft, upperright);
      return result;
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  String getVarName() {
    return this.varName;
  }

}
