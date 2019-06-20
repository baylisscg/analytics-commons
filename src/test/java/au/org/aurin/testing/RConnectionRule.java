package au.org.aurin.testing;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.rosuda.REngine.Rserve.RConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RConnectionRule implements TestRule {

  private final String host;
  private final int port;
  private RConnection connection;

  private static final Logger LOGGER = LoggerFactory.getLogger(RConnectionRule.class);

  public RConnectionRule() {
    this.host = System.getProperty("rserve.host", "localhost");
    this.port = Integer.parseInt(System.getProperty("rserve.port", "6311"));
  }

  public RConnection getConnection() {
    return connection;
  }

  @Override
  public Statement apply(final Statement base, final Description description) {

    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        LOGGER.info("Creating new Statement");

        try {
          connection = new RConnection(host, port);

          base.evaluate();

        } finally {
          if (connection != null && connection.isConnected()) {
            LOGGER.info("Closing connection");
            connection.close();
          }
        }
      }
    };
  }
}
