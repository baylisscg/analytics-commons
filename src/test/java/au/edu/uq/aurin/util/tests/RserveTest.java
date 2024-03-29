package au.edu.uq.aurin.util.tests;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.edu.uq.aurin.util.Rserve;

public class RserveTest {

  private static final Logger LOG = LoggerFactory.getLogger(RserveTest.class);

  /**
   * Test method to start and stop Rserve
   */
  @Test
  public void rserveTest() {

    Assert.assertTrue("Cannot Shutdown Rserve, Check if there are permissions "
        + "to shut it down if the process is owned by a different user", Rserve.checkLocalRserve());

    Assert.assertTrue("Rserve Running?", Rserve.isRserveRunning());
    RConnection c = null;

    try {
      // Make a connection if we successfully got Rserve running
      Assert.assertNotNull("Cannot Connect to Rserve", c = new RConnection());

      LOG.info("got a connection, so closing the connection");

      Assert.assertTrue("Cannot Close connection to Rserve", c.close());

      // Do not shutdown Rserve as it is needed for other tests
      // Assert.assertTrue("Cannot Shutdown Rserve, Check if Rserve.conf is has "
      // + "control enabled, Check if there are permissions to shut "
      // + "it down if the process is owned by a different user", Rserve.shutdownRserve());

    } catch (final RserveException re) {
      Assert.fail("Test Failed: " + ExceptionUtils.getFullStackTrace(re));
    }
  }
}
