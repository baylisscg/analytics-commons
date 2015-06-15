package au.edu.uq.aurin.util.tests;

import org.junit.Assert;
import org.junit.Test;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WindowsRConnectionTesting {

  private static final Logger LOG = LoggerFactory.getLogger(WindowsRConnectionTesting.class);

  @Test
  public void test2Connections() {
    LOG.info("2Connections: Rserve Multi-Connection Testing ---- " + getClass().getName() + " ----");

    RConnection c = null;
    RConnection d = null;
    try {
      c = new RConnection();
      d = new RConnection();
    } catch (final RserveException e) {
      e.printStackTrace();
      Assert.fail("RConnection null: " + c);
      Assert.fail("RConnection null: " + d);
    }
  }

  @Test
  public void testMoreConnections() {
    LOG.info("More Connections: Rserve Multi-Connection Testing ---- " + getClass().getName() + " ----");

    final int numRconnections = 10;

    RConnection cons = null;
    int ctr = 0;

    try {
      for (ctr = 0; ctr < numRconnections; ctr++) {
        cons = new RConnection();

        Thread.sleep(500); // 0.5 second pause after each connection creation
        LOG.info("Server Info: " + cons.getServerVersion());
        LOG.info("Connection num: " + ctr);
        LOG.info("Eval result: " + cons.eval("capture.output(print(R.version.string))").asString());
        LOG.info("Last Error: " + cons.getLastError());
      }
    } catch (final RserveException e) {
      e.printStackTrace();
      Assert.fail("RConnection: " + ctr + " failure: " + cons);
    } catch (final InterruptedException e) {
      e.printStackTrace();
      Assert.fail("Interrupted: " + e.toString());
    } catch (final REXPMismatchException e) {
      e.printStackTrace();
      Assert.fail("RConnection Expression evaluation Failed: " + ctr + " failure: " + cons);
    } finally {
      LOG.info("Close R Connections Array");
      cons.close();
      LOG.info("Done single variable multiple new Rconnections Rserve testing");
    }
  }

  @Test
  public void connectionsArrayCreatingMoreConnections() {
    LOG.info("More Connections: Rserve Multi-Connection Testing ---- " + getClass().getName() + " ----");

    final int numRconnections = 10;

    final RConnection[] consArray = new RConnection[numRconnections];
    int ctr = 0;

    try {
      for (ctr = 0; ctr < numRconnections; ctr++) {
        consArray[ctr] = new RConnection();

        Thread.sleep(500); // 0.5 second pause after each connection creation
        LOG.info("Server Info: " + consArray[ctr].getServerVersion());
        LOG.info("Connection num: " + ctr);
        LOG.info("Eval result: " + consArray[ctr].eval("capture.output(print(R.home()))").asString());
        LOG.info("Last Error: " + consArray[ctr].getLastError());
      }
    } catch (final RserveException e) {
      e.printStackTrace();
      Assert.fail("RConnection: " + ctr + " failure: " + consArray[ctr]);
    } catch (final InterruptedException e) {
      e.printStackTrace();
      Assert.fail("Interrupted: " + e.toString());
    } catch (final REXPMismatchException e) {
      e.printStackTrace();
      Assert.fail("RConnection Expression evaluation Failed: " + ctr + " failure: " + consArray[ctr]);
    } finally {
      LOG.info("Close R Connections Array");
      for (final RConnection element : consArray) {
        element.close();
      }
      LOG.info("Done Windows Multi Array-Connection Rserve testing");
    }
  }

}
