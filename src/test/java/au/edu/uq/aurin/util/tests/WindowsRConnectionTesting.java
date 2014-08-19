package au.edu.uq.aurin.util.tests;


import org.junit.Assert;
import org.junit.Test;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class WindowsRConnectionTesting {

  @Test
  public void test2Connections() {
    System.out.println("2Connections: Rserve Multi-Connection Testing ---- " + getClass().getName() + " ----");
    
    RConnection c = null;
    RConnection d = null;
    try {
      c = new RConnection();
      d = new RConnection();
    } catch(RserveException e) {
      e.printStackTrace();
      Assert.fail("RConnection null: " + c);
      Assert.fail("RConnection null: " + d);
    }
  }
  
  @Test
  public void testMoreConnections() {
    System.out.println("More Connections: Rserve Multi-Connection Testing ---- " + getClass().getName() + " ----");
    
    final int numRconnections = 10;
    
    RConnection cons = null;
    int ctr = 0;
    
    try {
      for (ctr = 0; ctr < numRconnections; ctr++) {
        cons = new RConnection();
        
        Thread.sleep(500); // 0.5 second pause after each connection creation
        System.out.println("Server Info: " + cons.getServerVersion());
        System.out.println("Connection num: " + ctr);
        System.out.println("Eval result: " + cons.eval("capture.output(print(R.version.string))").asString());
        System.out.println("Last Error: " + cons.getLastError());
      }
    } catch(RserveException e) {
      e.printStackTrace();
      Assert.fail("RConnection: " + ctr + " failure: " + cons);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Assert.fail("Interrupted: " + e.toString());
    } catch (REXPMismatchException e) {
      e.printStackTrace();
      Assert.fail("RConnection Expression evaluation Failed: " + ctr + " failure: " + cons);
    } finally {
      System.out.println("Close R Connections Array");
      cons.close();
      System.out.println("Done single variable multiple new Rconnections Rserve testing");
    }
  }

  @Test
  public void connectionsArrayCreatingMoreConnections() {
    System.out.println("More Connections: Rserve Multi-Connection Testing ---- " + getClass().getName() + " ----");
    
    final int numRconnections = 10;
    
    RConnection[] consArray = new RConnection[numRconnections];
    int ctr = 0;
    
    try {
      for (ctr = 0; ctr < numRconnections; ctr++) {
        consArray[ctr] = new RConnection();
        
        Thread.sleep(500); // 0.5 second pause after each connection creation
        System.out.println("Server Info: " + consArray[ctr].getServerVersion());
        System.out.println("Connection num: " + ctr);
        System.out.println("Eval result: " + consArray[ctr].eval("capture.output(print(R.home()))").asString());
        System.out.println("Last Error: " + consArray[ctr].getLastError());       
      }
    } catch(RserveException e) {
      e.printStackTrace();
      Assert.fail("RConnection: " + ctr + " failure: " + consArray[ctr]);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Assert.fail("Interrupted: " + e.toString());
    } catch (REXPMismatchException e) {
      e.printStackTrace();
      Assert.fail("RConnection Expression evaluation Failed: " + ctr + " failure: " + consArray[ctr]);
    } finally {
      System.out.println("Close R Connections Array");
      for (int i = 0; i < consArray.length; i++) {
        consArray[i].close();
      }
      System.out.println("Done Windows Multi Array-Connection Rserve testing");
    }
  }
  
}
