package au.edu.uq.aurin.util.tests;

import org.junit.Assert;
import org.junit.Test;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.edu.uq.aurin.util.Rproperties;
import au.edu.uq.aurin.util.StatisticsException;

public class RpropertiesTest {

  private static final Logger LOG = LoggerFactory.getLogger(RpropertiesTest.class);

  public REXP dataGenerator() {

    REXP data = null;

    final double[] d1 = new double[] { 1.1, 0, 3.3, 11.1, 22.2, REXPDouble.NA, 1234.1 }; // col1
    final double[] d2 = new double[] { 10.0, 20.0, 30.0, REXPDouble.NA, 50.0, 60.0, 1234.2 }; // col2
    final double[] d3 = new double[] { 100.0, 0, 300.0, 400.0, 500.0, 600.0, 1234.3 }; // dcol1
    final double[] d4 = new double[] { 100.1, 200.2, 300.3, 110.1, 220.2, REXPDouble.NA, 1234.4 }; // dcol2

    final RList a = new RList();
    // add each column separately
    a.put("iCol1", new REXPDouble(d1));
    a.put("iCol2", new REXPDouble(d2));
    a.put("dCol1", new REXPDouble(d3));
    a.put("dCol2", new REXPDouble(d4));

    try {
      data = REXP.createDataFrame(a);
    } catch (final REXPMismatchException e) {
      e.printStackTrace();
    }

    return data;
  }

  public REXP dataGenerator2() {

    REXP data = null;

    final double[] d1 = new double[] { 1.1, 0, 3.3, 11.1, 22.2, REXPDouble.NA, 1234.1 }; // col1
    final double[] d2 = new double[] { 10.0, 20.0, 30.0, REXPDouble.NA, 50.0, 60.0, 1234.2 }; // col2
    final double[] d3 = new double[] { 100.0, 0, 300.0, 400.0, 500.0, 600.0, 1234.3 }; // dcol1
    final double[] d4 = new double[] { 100.1, 200.2, 300.3, 110.1, 220.2, REXPDouble.NA, 1234.4 }; // dcol2

    final RList a = new RList();
    // add each column separately
    a.put("iCol1", new REXPDouble(d1));
    a.put("iCol2", new REXPDouble(d2));
    a.put("dCol1", new REXPDouble(d3));
    a.put("dCol2", new REXPDouble(d4));

    try {
      data = REXP.createDataFrame(a);
    } catch (final REXPMismatchException e) {
      e.printStackTrace();
    }

    return data;
  }

  @Test
  public void testdataFrameColumnNames() {
    LOG.info("Column Names check");

    try {
      // 1. dummy R data for REXP population
      final REXP data1 = this.dataGenerator();
      Rproperties.dataFrameColumnNames(data1);

    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testdataFrameCheck() {
    LOG.info("DataFrame structure check");

    try {
      // 1. dummy R data for REXP population
      final REXP data1 = this.dataGenerator();
      Rproperties.dataFrameCheck(data1);

    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testdataFrameColumnNamesFromConnection() {
    LOG.info("Column Names check from an R connection");

    RConnection c = null;
    try {
      // 0. create a connection
      c = new RConnection();

      // 1. dataframe name
      final String dataFrameName = "data1";

      // 2. dummy R data for REXP population
      final REXP data1 = this.dataGenerator();
      c.assign(dataFrameName, data1);

      LOG.info("dataFrameName debug string: {}", data1.toDebugString());
      LOG.info("dataFrameName Attribute type: {}", data1.getAttribute("class").asString());

      // 3. retrieve data1 from connection
      final REXP data1Retrieved = c.get(dataFrameName, null, true);
      Assert.assertArrayEquals(new String[] { "iCol1", "iCol2", "dCol1", "dCol2" },
          Rproperties.dataFrameColumnNames(c, dataFrameName));
      Assert.assertArrayEquals(new String[] { "iCol1", "iCol2", "dCol1", "dCol2" },
          Rproperties.dataFrameColumnNames(data1Retrieved));

    } catch (final RserveException e) {
      Assert.fail("RServe: " + e.getMessage());
    } catch (final REngineException e) {
      Assert.fail("REngine: " + e.getMessage());
    } catch (final REXPMismatchException e) {
      Assert.fail("REXPMismatch: " + e.getMessage());
    } catch (final StatisticsException e) {
      Assert.fail("Statistics: " + e.getMessage());
    } finally {
      if (c != null) {
        c.close();
      }
    }
  }

  @Test
  public void testCompute() {
    LOG.info("Null Check");

    RConnection c = null;
    try {
      // 0. create a connection
      c = new RConnection();

      // 1. dummy R data for REXP population
      final REXP data1 = this.dataGenerator();
      c.assign("data1", data1);
      final REXP data2 = this.dataGenerator();
      c.assign("data2", data2);

      LOG.info("data1 debug string: " + data1.toDebugString());
      LOG.info("data1 Attribute type: " + data1.getAttribute("class").asString());
      LOG.info("data2 debug string: " + data2.toDebugString());
      LOG.info("data2 Attribute type: " + data2.getAttribute("class").asString());

      // debug information about the input connection
      Rproperties.propertiesOfRserve(c);
      Rproperties.objectsOfRSession(c);
      Rproperties.loadedPackagesOfRSession(c);

      // retrieve data1 from connection
      final REXP data1Retrieved = c.get("data1", null, true);
      Rproperties.printDataFrame(data1); // original
      Rproperties.printDataFrame(data1Retrieved); // updated

      final boolean valid1 = Rproperties.compare2DataFrames(data1, data1Retrieved);
      LOG.info("Data frame1 original and retrieved are identical? " + valid1);
      Assert.assertEquals(valid1, true);

      // Retrieve data 2 from connection
      final REXP data2Retrieved = c.get("data2", null, true);
      Rproperties.printDataFrame(data2); // original
      Rproperties.printDataFrame(data2Retrieved); // updated

      final boolean valid2 = Rproperties.compare2DataFrames(data1, data1Retrieved);
      LOG.info("Data frame2 original and retrieved are identical? " + valid2);
      Assert.assertEquals(valid2, true);

      final boolean valid3 = Rproperties.compare2DataFrames(data1Retrieved, data2Retrieved);
      LOG.info("Retrieved Data frames of dataframe 1 and data frame 2 are identical? " + valid3);
      Assert.assertEquals(valid3, true);

    } catch (final RserveException e) {
      Assert.fail("RServe: " + e.getMessage());
    } catch (final REngineException e) {
      Assert.fail("REngine: " + e.getMessage());
    } catch (final REXPMismatchException e) {
      Assert.fail("REXPMismatch: " + e.getMessage());
    } catch (final StatisticsException e) {
      Assert.fail("Statistics: " + e.getMessage());
    } finally {
      if (c != null) {
        c.close();
      }
    }
  }

}
