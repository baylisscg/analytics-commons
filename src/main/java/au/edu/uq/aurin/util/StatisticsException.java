package au.edu.uq.aurin.util;

import au.edu.uq.aurin.interfaces.Statistics;

/**
 * {@link StatisticsException} class for the {@link Statistics} Interface
 * 
 * @author irfan
 */
public class StatisticsException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Default Exception without a custom message
   */
  public StatisticsException() {
    super();
  }

  /**
   * Exception with a custom error message
   * @param msg
   */
  public StatisticsException(String msg) {
    super(msg);
  }

  /**
   * Exception with a cause
   * @param cause
   */
  public StatisticsException(Throwable cause) {
    super(cause);
  }

  /**
   * Exception with a message and a cause
   * @param msg
   * @param cause
   */
  public StatisticsException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
