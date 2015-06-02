package au.edu.uq.aurin.util.tests;

import org.junit.Assert;
import org.junit.Test;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.edu.uq.aurin.util.RWorkerError;
import au.edu.uq.aurin.util.StatisticsException;

public class RWorkerErrorTest {

  private static final Logger LOG = LoggerFactory.getLogger(RWorkerErrorTest.class);

  @Test
  public void updatedTest() throws StatisticsException {

    LOG.info("Test Message: {}", getClass().getName());

    RConnection c;
    REXP worker = null;

    try {
      c = new RConnection();
      worker = c.eval("capture.output(paste(sessionInfo()))");
      RWorkerError.check(worker);

      LOG.info("worker: {}", worker.asString());

    } catch (final RserveException e) {
      Assert.fail("Rserve exception: " + e.getMessage());
    } catch (final REXPMismatchException e) {
      Assert.fail("REXPMismatch exception: " + e.getMessage());
    } catch (final StatisticsException e) {
      Assert.fail("Statistics error: " + e.getMessage());
    }
  }

}
