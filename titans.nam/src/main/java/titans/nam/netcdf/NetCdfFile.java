package titans.nam.netcdf;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import java.io.File;
import java.io.IOException;
import javax.measure.Measure;
import javax.measure.quantity.Length;
import javax.measure.unit.SI;
import rm.titansdata.SridUtils;
import rm.titansdata.properties.Bounds;
import rm.titansdata.properties.Dimension;
import rm.titansdata.properties.Dimensions;
import ucar.nc2.dt.GridDataset.Gridset;
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
      Gridset gridSet = gds.getGridsets().get(0);
      ucar.nc2.Dimension x = gridSet.getGeoCoordSystem().getDomain().get(0);
      ucar.nc2.Dimension y = gridSet.getGeoCoordSystem().getDomain().get(1);
      int pixelsX = x.getLength();
      int pixelsY = y.getLength();
      double width = this.getBounds().getLengthX()/pixelsX;
      double height = this.getBounds().getLengthY()/pixelsY;
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
  Bounds getBounds() {
    try (GridDataset gds = GridDataset.open(file.getAbsolutePath())) {
      LatLonRect bbox = gds.getBoundingBox();
      PrecisionModel precisionModel = new PrecisionModel(PrecisionModel.FLOATING);
      GeometryFactory factory = new GeometryFactory(precisionModel, 4326);
      Coordinate c1 = new Coordinate(bbox.getLonMin(), bbox.getLatMin());
      Coordinate c2 = new Coordinate(bbox.getLonMax(), bbox.getLatMax());
      Point lowerleft = SridUtils.transform(factory.createPoint(c1), 3857);
      Point upperright = SridUtils.transform(factory.createPoint(c2), 3857);
      Bounds result = new Bounds(lowerleft, upperright);
      return result;
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  /**
   * 
   * @return 
   */
  String getVarName() {
    return this.varName;
  }

  /**
   * 
   * @return 
   */
  public boolean exists() {
    return this.file.exists();
  }

}
