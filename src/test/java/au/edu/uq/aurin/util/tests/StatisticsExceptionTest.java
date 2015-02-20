package au.edu.uq.aurin.util.tests;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import au.edu.uq.aurin.util.Rserve;
import au.edu.uq.aurin.util.StatisticsException;

public class StatisticsExceptionTest {

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
  public void test1() {

    System.out.println("Ignore this test as it shuts down Rserve service - Other Rserve");
    try {
      Assert.assertTrue("Cannot Shutdown Rserve, Check if there are permissions "
          + "to shut it down if the process is owned by a different user", Rserve.checkLocalRserve());
      Assert.assertTrue("Rserve Running?", Rserve.isRserveRunning());

      final RConnection c = new RConnection();
      final REXP worker = c.eval("capture.output(paste(search()))");

      System.out.println("worker = " + worker.asString());
      // Uncomment line below to Test Statistics Exception
      // System.out.println("worker = " + worker.asDoubleMatrix());

      c.close();
      // Do not shutdown as other tests will fail in analytics-commons
      // Rserve.shutdownRserve();
    } catch (final RserveException e) {
      Assert.fail("Rserve: " + ExceptionUtils.getFullStackTrace(new StatisticsException(e)));
    } catch (final REXPMismatchException e) {
      Assert.fail("Mismatch: " + ExceptionUtils.getFullStackTrace(new StatisticsException(e)));
    }
  }

}
