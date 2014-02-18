package au.edu.uq.aurin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.exception.ExceptionUtils;

public class StatisticsException extends Exception {

  private static final long serialVersionUID = 1L;
  private static final String MSGHEADER = "Statistics Exception: ";

  private static final Logger LOG = LoggerFactory.getLogger(StatisticsException.class);

  
  public StatisticsException() {
    super();
    LOG.info(MSGHEADER);
  }

  public StatisticsException(String msg) {
    super(msg);
    LOG.info(MSGHEADER + msg);
  }

  public StatisticsException(Throwable cause) {
    super(cause);
    LOG.info(MSGHEADER + ExceptionUtils.getFullStackTrace(cause));
  }

  public StatisticsException(String msg, Throwable cause) {
    super(msg, cause);
    LOG.info(MSGHEADER + msg + " " + ExceptionUtils.getFullStackTrace(cause));
  }
}
