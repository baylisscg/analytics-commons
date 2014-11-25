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

import au.edu.uq.aurin.util.Rproperties;
import au.edu.uq.aurin.util.StatisticsException;

public class RpropertiesTest {

  public REXP dataGenerator() {

    REXP data = null;

    double[] d1 = new double[] { 1.1, 0, 3.3, 11.1, 22.2, REXPDouble.NA , 1234.1}; // col1
    double[] d2 = new double[] { 10.0, 20.0, 30.0, REXPDouble.NA, 50.0, 60.0, 1234.2}; // col2
    double[] d3 = new double[] { 100.0, 0, 300.0, 400.0, 500.0, 600.0, 1234.3}; // dcol1
    double[] d4 = new double[] { 100.1, 200.2, 300.3, 110.1, 220.2, REXPDouble.NA, 1234.4}; // dcol2
    
    RList a = new RList();
    // add each column separately
    a.put("iCol1", new REXPDouble(d1));
    a.put("iCol2", new REXPDouble(d2));
    a.put("dCol1", new REXPDouble(d3));
    a.put("dCol2", new REXPDouble(d4));

    try {
      data = REXP.createDataFrame(a);
    } catch (REXPMismatchException e) {
      e.printStackTrace();
    }

    return data;
  }

  public REXP dataGenerator2() {

    REXP data = null;

    double[] d1 = new double[] { 1.1, 0, 3.3, 11.1, 22.2, REXPDouble.NA , 1234.1}; // col1
    double[] d2 = new double[] { 10.0, 20.0, 30.0, REXPDouble.NA, 50.0, 60.0, 1234.2}; // col2
    double[] d3 = new double[] { 100.0, 0, 300.0, 400.0, 500.0, 600.0, 1234.3}; // dcol1
    double[] d4 = new double[] { 100.1, 200.2, 300.3, 110.1, 220.2, REXPDouble.NA, 1234.4}; // dcol2
    
    RList a = new RList();
    // add each column separately
    a.put("iCol1", new REXPDouble(d1));
    a.put("iCol2", new REXPDouble(d2));
    a.put("dCol1", new REXPDouble(d3));
    a.put("dCol2", new REXPDouble(d4));

    try {
      data = REXP.createDataFrame(a);
    } catch (REXPMismatchException e) {
      e.printStackTrace();
    }

    return data;
  }
  
  @Test
  public void testdataFrameColumnNames() {
    System.out.println("Column Names check");

    try {
      // 1. dummy R data for REXP population
      REXP data1 = this.dataGenerator();
      Rproperties.dataFrameColumnNames(data1);

    } catch(Exception e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testdataFrameCheck() {
    System.out.println("DataFrame structure check");

    try {
      // 1. dummy R data for REXP population
      REXP data1 = this.dataGenerator();
      Rproperties.dataFrameCheck(data1);

    } catch(Exception e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testdataFrameColumnNamesFromConnection() {
    System.out.println("Column Names check from an R connection");

    RConnection c = null;
    try {
      // 0. create a connection
      c = new RConnection();

      // 1. dataframe name
      String dataFrameName = "data1";

      // 2. dummy R data for REXP population
      REXP data1 = this.dataGenerator();
      c.assign(dataFrameName, data1);

      System.out.println("dataFrameName debug string: " + data1.toDebugString());
      System.out.println("dataFrameName Attribute type: " + data1.getAttribute("class").asString());

      // 3. retrieve data1 from connection
      REXP data1Retrieved = c.get(dataFrameName, null, true);
      Assert.assertArrayEquals(new String[] {"iCol1", "iCol2", "dCol1", "dCol2"}, Rproperties.dataFrameColumnNames(c, dataFrameName));
      Assert.assertArrayEquals(new String[] {"iCol1", "iCol2", "dCol1", "dCol2"}, Rproperties.dataFrameColumnNames(data1Retrieved));

    } catch (RserveException e) {
      Assert.fail("RServe: " + e.getMessage());
    } catch (REngineException e) {
      Assert.fail("REngine: " + e.getMessage());
    } catch (REXPMismatchException e) {
      Assert.fail("REXPMismatch: " + e.getMessage());
    } catch (StatisticsException e) {
      Assert.fail("Statistics: " + e.getMessage());
    } finally {
      if(c!= null) {
        c.close();
      }
    }
  }

  @Test
  public void testCompute() {
    System.out.println("Null Check");
    
    try {
      // 0. create a connection
      RConnection c = new RConnection();

      // 1. dummy R data for REXP population
      REXP data1 = this.dataGenerator();
      c.assign("data1", data1);
      REXP data2 = this.dataGenerator();
      c.assign("data2", data2);

      System.out.println("data1 debug string: " + data1.toDebugString());
      System.out.println("data1 Attribute type: " + data1.getAttribute("class").asString());
      System.out.println("data2 debug string: " + data2.toDebugString());
      System.out.println("data2 Attribute type: " + data2.getAttribute("class").asString());
      
      // debug information about the input connection
      Rproperties.propertiesOfRserve(c);
      Rproperties.objectsOfRSession(c);
      Rproperties.loadedPackagesOfRSession(c);

      // retrieve data1 from connection
      REXP data1Retrieved = c.get("data1", null, true);
      Rproperties.printDataFrame(data1); // original
      Rproperties.printDataFrame(data1Retrieved); // updated

      boolean valid1 = Rproperties.compare2DataFrames(data1, data1Retrieved);
      System.out.println("Data frame1 original and retrieved are identical? " + valid1);
      Assert.assertEquals(valid1, true);
      
      // Retrieve data 2 from connection
      REXP data2Retrieved = c.get("data2", null, true);
      Rproperties.printDataFrame(data2); // original
      Rproperties.printDataFrame(data2Retrieved); // updated

      boolean valid2 = Rproperties.compare2DataFrames(data1, data1Retrieved);
      System.out.println("Data frame2 original and retrieved are identical? " + valid2);
      Assert.assertEquals(valid2, true);
      
      boolean valid3 = Rproperties.compare2DataFrames(data1Retrieved, data2Retrieved);
      System.out.println("Retrieved Data frames of dataframe 1 and data frame 2 are identical? " + valid3);
      Assert.assertEquals(valid3, true);
      
      c.close();
    } catch (RserveException e) {
      Assert.fail("RServe: " + e.getMessage());
    } catch (REngineException e) {
      Assert.fail("REngine: " + e.getMessage());
    } catch (REXPMismatchException e) {
      Assert.fail("REXPMismatch: " + e.getMessage());
    } catch (StatisticsException e) {
      Assert.fail("Statistics: " + e.getMessage());
    }
  }

}
