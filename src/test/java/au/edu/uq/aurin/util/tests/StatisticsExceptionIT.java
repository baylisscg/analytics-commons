package au.edu.uq.aurin.util.tests;

import au.edu.uq.aurin.util.Rserve;
import au.edu.uq.aurin.util.StatisticsException;
import au.org.aurin.testing.RConnectionRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatisticsExceptionIT {

  private static final Logger LOG = LoggerFactory.getLogger(StatisticsExceptionIT.class);

  @Rule public RConnectionRule rConnectionRule = new RConnectionRule();

  @Test
  public void test0() {

    StatisticsException t = new StatisticsException();
    Assert.assertNotNull(t);

    t = new StatisticsException("Ex Message");
    Assert.assertEquals("Ex Message", t.getMessage());

    t = new StatisticsException(new Throwable("Throwable Message"));
    Assert.assertNotNull(t.getClass());

    t = new StatisticsException("EMessage", new Throwable("throwable message"));
    Assert.assertEquals("EMessage", t.getMessage());
    Assert.assertEquals("throwable message", t.getCause().getMessage());
  }

  @Test
  public void test1() throws RserveException, REXPMismatchException {

    System.out.println("Ignore this test as it shuts down Rserve service - Other Rserve");

    Assert.assertTrue(
        "Cannot Shutdown Rserve, Check if there are permissions "
            + "to shut it down if the process is owned by a different user",
        Rserve.checkLocalRserve());
    Assert.assertTrue("Rserve Running?", Rserve.isRserveRunning());

    final RConnection c = rConnectionRule.getConnection();
    final REXP worker = c.eval("capture.output(paste(search()))");

    LOG.info("worker: {}", worker.asString());
  }
}
