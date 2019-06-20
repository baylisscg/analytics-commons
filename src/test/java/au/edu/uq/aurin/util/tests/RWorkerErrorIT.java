package au.edu.uq.aurin.util.tests;

import au.edu.uq.aurin.util.RWorkerError;
import au.edu.uq.aurin.util.StatisticsException;
import au.org.aurin.testing.RConnectionRule;
import org.junit.Rule;
import org.junit.Test;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RWorkerErrorIT {

  private static final Logger LOG = LoggerFactory.getLogger(RWorkerErrorIT.class);

  @Rule public RConnectionRule rConnectionRule = new RConnectionRule();

  @Test
  public void updatedTest() throws StatisticsException, RserveException, REXPMismatchException {

    LOG.info("Test Message: {}", getClass().getName());

    RConnection c;
    REXP worker;

    c = this.rConnectionRule.getConnection();
    worker = c.eval("capture.output(paste(sessionInfo()))");
    RWorkerError.check(worker);

    LOG.info("worker: {}", worker.asString());
  }
}
