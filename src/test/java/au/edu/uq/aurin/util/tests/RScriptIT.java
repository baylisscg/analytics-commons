package au.edu.uq.aurin.util.tests;

import au.edu.uq.aurin.logging.Rlogger;
import au.edu.uq.aurin.util.Rscript;
import au.edu.uq.aurin.util.StatisticsException;
import au.org.aurin.testing.RConnectionRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPLogical;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RScriptIT {

  private static final Logger LOG = LoggerFactory.getLogger(RScriptIT.class);

  public @Rule RConnectionRule rConnectionRule = new RConnectionRule();

  public double[] multiplier(final double mult) {

    final double[] content = new double[] {11, 22, 33, 44, 55, 66, 77, 88, 99, 00};

    for (int i = 0; i < content.length; i++) {
      content[i] = mult * content[i];
    }
    return content;
  }

  public REXP dataGenerator() throws REXPMismatchException {

    final RList a = new RList();
    // new test data dependents
    for (int i = 0; i < 20; i++) {
      a.put(("col" + i).toString(), new REXPDouble(multiplier(i + Math.random() * 10)));
    }

    return REXP.createDataFrame(a);
  }

  @Test(timeout = 5000L)
  public void test() throws RserveException, REXPMismatchException, StatisticsException {

    LOG.info("Rscript Unit Test");

    RConnection cIn = rConnectionRule.getConnection();

    // 0. Load the script
    final String script = Rscript.load("/au/edu/uq/aurin/rscripts/script.r");
    Assert.assertNotNull("Unable to load given R script", script);

    // 1. create a connection
    cIn.assign("script", script);

    // 2. setup the data inputs
    cIn.assign("dataFrameName", "dataF");
    cIn.assign("dataF", this.dataGenerator());

    // 3. input options
    final RList optList = new RList();
    optList.put("intercept", new REXPLogical(true));
    cIn.assign("optionsM", REXP.createDataFrame(optList));

    // 4. Logging setup
    Rlogger.logger("DEBUG", System.getProperty("java.io.tmpdir"));
    cIn.assign("optionsLogging", Rlogger.getLogOptions());

    // 5. call the function defined in the script
    final REXP worker = cIn.eval("try(eval(parse(text=script)),silent=FALSE)");
    LOG.trace("worker result: {}", worker.toDebugString());
  }
}
