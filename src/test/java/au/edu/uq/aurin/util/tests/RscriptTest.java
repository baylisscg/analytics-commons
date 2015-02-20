package au.edu.uq.aurin.util.tests;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPLogical;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import au.edu.uq.aurin.logging.Rlogger;
import au.edu.uq.aurin.util.Rscript;
import au.edu.uq.aurin.util.Rserve;
import au.edu.uq.aurin.util.StatisticsException;

public class RscriptTest {

  public double[] multiplier(final double mult) {

    final double[] content = new double[] { 11, 22, 33, 44, 55, 66, 77, 88, 99, 00 };

    for (int i = 0; i < content.length; i++) {
      content[i] = mult * content[i];
    }
    return content;
  }

  public REXP dataGenerator() {

    REXP data = null;

    final RList a = new RList();
    // new test data dependents
    for (int i = 0; i < 20; i++) {
      a.put(("col" + i).toString(), new REXPDouble(multiplier(i + Math.random() * 10)));
    }

    try {
      data = REXP.createDataFrame(a);
    } catch (final REXPMismatchException e) {
      e.printStackTrace();
    }

    return data;
  }

  @Test
  public void test() {

    System.out.println("Rscript Unit Test");

    RConnection cIn = null;
    try {
      Assert.assertTrue("Cannot Shutdown Rserve, Check if there are permissions "
          + "to shut it down if the process is owned by a different user", Rserve.checkLocalRserve());

      Assert.assertTrue("Rserve Running?", Rserve.isRserveRunning());

      // 0. Load the script
      final String script = Rscript.load("/au/edu/uq/aurin/rscripts/script.r");
      Assert.assertNotNull("Unable to load given R script", script);

      // System.out.println(System.getProperties().toString());
      // ClassLoader cl = ClassLoader.getSystemClassLoader();
      // URL[] urls = ((URLClassLoader)cl).getURLs();
      // for(URL url: urls){
      // System.out.println(url.getFile());
      // }

      // 1. create a connection
      cIn = new RConnection();
      cIn.assign("script", script);

      // 2. setup the data inputs
      cIn.assign("dataFrameName", "dataF");
      cIn.assign("dataF", this.dataGenerator());

      // 3. input options
      final RList optList = new RList();
      optList.put("intercept", new REXPLogical(true));
      cIn.assign("optionsM", REXP.createDataFrame(optList));

      // 4. Logging setup
      Rlogger.logger("DEBUG", "/tmp");
      cIn.assign("optionsLogging", Rlogger.getLogOptions());

      // 5. call the function defined in the script
      final REXP worker = cIn.eval("try(eval(parse(text=script)),silent=FALSE)");
      System.out.println("worker result: " + worker.toDebugString());

      cIn.close();
    } catch (final RserveException x) {
      Assert.fail("Test Failed: Rserve" + ExceptionUtils.getFullStackTrace(x));
    } catch (final REXPMismatchException e) {
      Assert.fail("Test Failed: REXPMismatch" + ExceptionUtils.getFullStackTrace(e));
    } catch (final StatisticsException e) {
      Assert.fail("Test Failed: " + ExceptionUtils.getFullStackTrace(e));
    }
  }

}
