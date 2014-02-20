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

    System.out.println("Test StatisticsException Class");

    try {
      try {
        Assert
            .assertTrue(
                "Cannot Shutdown Rserve, Check if there are permissions "
                    + "to shut it down if the process is owned by a different user",
                Rserve.checkLocalRserve());
        Assert.assertTrue("Rserve Running?", Rserve.isRserveRunning());

        RConnection c = new RConnection();
        REXP worker = c.eval("capture.output(paste(search()))");
        // Uncomment line below to Test Statistics Exception
        // System.out.println("worker = " + worker.asDoubleMatrix());
        System.out.println("worker = " + worker.asString());

        c.close();
        Rserve.shutdownRserve();
      } catch (RserveException e) {
        throw new StatisticsException(e);
      } catch (REXPMismatchException e) {
        throw new StatisticsException(e);
      }
    } catch (StatisticsException e) {
      Assert.fail("Test Failed: " + ExceptionUtils.getFullStackTrace(e));
    }
  }

}