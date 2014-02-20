package au.edu.uq.aurin.util;

public class StatisticsException extends Exception {

  private static final long serialVersionUID = 1L;

  public StatisticsException() {
    super();
  }

  public StatisticsException(String msg) {
    super(msg);
  }

  public StatisticsException(Throwable cause) {
    super(cause);
  }

  public StatisticsException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
