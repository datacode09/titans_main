package rm.titansdata;


import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import rm.titansdata.raster.Raster;
import rm.titansdata.raster.RasterObj;
import rm.titansdata.testdata.RasterITTestData;

/**
 *
 * @author Ricardo Marquez
 */
@RunWith(JUnitParamsRunner.class)
public class RasterIT {
  
  @Test
  @Parameters({
    "-120.43, 37.36, 1.0", 
    "-120.43, 17.36, NaN",
    "-120.43, 37.36, 1.0",
  })
  public void get_value_byknown_inputs_and_outputs( //
    double lon, double lat, double expValue) throws Exception {
    Raster raster = RasterITTestData.getBasicRaster();
    int srid = 4326;
    Point p = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), srid)
      .createPoint(new Coordinate(lon, lat));
    double value = raster.getValue(p);
    double delta = 0.001 * 0.5 * (Math.abs(value) + Math.abs(expValue));
    Assert.assertEquals("getvalue", expValue, value, delta);
  }
    
  /**
   * 
   * @param lon
   * @param lat
   * @param expValue
   * @throws Exception 
   */
  @Test
  @Parameters({
    "-120.43, 37.36, 60, 1.0", 
    "-120.43, 17.36, 60, NaN",
    "-120.43, 37.36, 60, 1.0",
  })
  public void get_aggregatevalue_byknonwn_inputs_and_outputs( //
    double lon, double lat, double kmsquare, double expValue //
  ) throws Exception {
    Raster raster = RasterITTestData.getBasicRaster();
    int srid = 4326;
    GeometryFactory factory = new GeometryFactory( //
      new PrecisionModel(PrecisionModel.FLOATING), srid);
    MultiPoint p = factory.createMultiPoint(
      new Coordinate[]{new Coordinate(lon, lat)});
    double value = raster.getMeanValue(p);
    double delta = 0.001 * 0.5 * (Math.abs(value) + Math.abs(expValue));
    Assert.assertEquals("getvalue", expValue, value, delta);
  }
  
  @Test
  @Parameters({
    "-121.00, 37.00, -119.00, 40.00, 4326, 62530"
  })
  public void get_raster_by_envelopes(
    double x1, double y1, double x2, double y2, int srid, int expectedSize) {
    PrecisionModel precisionModel = new PrecisionModel(PrecisionModel.FLOATING);
    GeometryFactory factory = new GeometryFactory(precisionModel, srid);
    Polygon p = factory.createPolygon(new Coordinate[]{
      new Coordinate(x1, y1),
      new Coordinate(x2, y1),
      new Coordinate(x2, y2),
      new Coordinate(x1, y2),
      new Coordinate(x1, y1),
    });
    RasterObj rasterobj = RasterITTestData.getBasicRasterObj();
    RasterObj subset = rasterobj.getSubsetRaster("subset", p);
    int size = subset.getNumPixels();
    Assert.assertEquals("get_raster_by_envelopes", expectedSize, size);
  }
  
  @Test
  @Parameters({
    "-120.00, 37.00, -121.50, 40.00, 4326, 1713828"
  }) 
  public void test_02(//
    double x1, double y1, double x2, double y2, int srid, int expectedSize) {
    PrecisionModel precisionModel = new PrecisionModel(PrecisionModel.FLOATING);
    GeometryFactory factory = new GeometryFactory(precisionModel, srid);
    Polygon p = factory.createPolygon(new Coordinate[]{
      new Coordinate(x1, y1),
      new Coordinate(x2, y1),
      new Coordinate(x2, y2),
      new Coordinate(x1, y2),
      new Coordinate(x1, y1),
    });
    Geometry p2 = SridUtils.transform(p, 32610); 
    Geometry p4 = SridUtils.transform(p, 3857); 
    Geometry p3 = SridUtils.transform(p2, srid); 
    System.out.println("p = " + p);
    System.out.println("p2 = " + p2);
    System.out.println("p3 = " + p3);
  }
}
