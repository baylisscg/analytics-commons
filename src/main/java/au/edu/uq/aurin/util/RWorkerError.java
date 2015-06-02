package au.edu.uq.aurin.util;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Check for the R worker result errors
 *
 * @author irfan
 *
 */
public class RWorkerError {

  private static final Logger LOG = LoggerFactory.getLogger(RWorkerError.class);

  /**
   * Static Utility Class
   */
  private RWorkerError() {
  }

  /**
   * Checks if the worker returned a valid result
   *
   * @param worker
   *          R evaluation result
   * @throws StatisticsException
   *           If the R evaluation inherited an error
   */
  public static void check(final REXP worker) throws StatisticsException {

    String errMsg = null;

    try {
      if (worker == null || worker.isNull()) {
        errMsg = "No Result returned from R";
        LOG.error(errMsg);
        throw new StatisticsException(errMsg);
      } else if (worker.inherits("error")) {
        // R Error returned
        errMsg = worker.asList().at("message").asString();
        LOG.error(errMsg);
        throw new StatisticsException(errMsg);
      } else if (worker.inherits("warning")) {
        errMsg = worker.asList().at("message").asString();
        LOG.warn(errMsg);
      } else if (worker.isString() && worker.inherits("error")) {
        errMsg = worker.asString();
        LOG.error(errMsg);
        throw new StatisticsException(errMsg);
      }
    } catch (final REXPMismatchException e) {
      errMsg = "Unknown Exception: " + e.getMessage();
      LOG.error(errMsg, e.getCause());
      throw new StatisticsException(errMsg, e);
    }

  }
}
