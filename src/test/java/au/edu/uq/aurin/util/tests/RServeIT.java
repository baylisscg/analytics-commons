package au.edu.uq.aurin.util.tests;

import au.org.aurin.testing.RConnectionRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.rosuda.REngine.Rserve.RConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RServeIT {

  private static final Logger LOG = LoggerFactory.getLogger(RServeIT.class);

  public @Rule RConnectionRule rConnectionRule = new RConnectionRule();

  /** Test method to start and stop Rserve */
  @Test(timeout = 5000L)
  public void rserveTest() {

    RConnection c = rConnectionRule.getConnection();

    // Make a connection if we successfully got Rserve running
    Assert.assertNotNull("Cannot Connect to Rserve", c);

    LOG.info("got a connection, so closing the connection");

    Assert.assertTrue("Cannot Close connection to Rserve", c.close());

    // Do not shutdown Rserve as it is needed for other tests
    // Assert.assertTrue("Cannot Shutdown Rserve, Check if Rserve.conf is has "
    // + "control enabled, Check if there are permissions to shut "
    // + "it down if the process is owned by a different user", Rserve.shutdownRserve());

  }
}
