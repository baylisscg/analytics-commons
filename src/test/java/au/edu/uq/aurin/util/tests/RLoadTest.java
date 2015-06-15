package au.edu.uq.aurin.util.tests;

import org.junit.Assert;
import org.junit.Test;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.edu.uq.aurin.util.Rserve;

public class RLoadTest {

  private static final Logger LOG = LoggerFactory.getLogger(RLoadTest.class);

  public double[] multiplier(final double mult) {

    final double[] content = new double[] { 11, 22, 33, 44, 55, 66, 77, 88, 99, 00, 111, 221, 331, 441, 551, 661, 771,
        881, 991, 001, 211, 222, 233, 244, 255, 266, 277, 288, 299, 200, 11, 22, 33, 44, 55, 66, 77, 88, 99, 00, 111,
        221, 331, 441, 551, 661, 771, 881, 991, 001, 211, 222, 233, 244, 255, 266, 277, 288, 299, 200, 11, 22, 33, 44,
        55, 66, 77, 88, 99, 00, 111, 221, 331, 441, 551, 661, 771, 881, 991, 001, 211, 222, 233, 244, 255, 266, 277,
        288, 299, 200, 11, 22, 33, 44, 55, 66, 77, 88, 99, 00, 111, 221, 331, 441, 551, 661, 771, 881, 991, 001, 211,
        222, 233, 244, 255, 266, 277, 288, 299, 200, 11, 22, 33, 44, 55, 66, 77, 88, 99, 00, 111, 221, 331, 441, 551,
        661, 771, 881, 991, 001, 211, 222, 233, 244, 255, 266, 277, 288, 299, 200, 11, 22, 33, 44, 55, 66, 77, 88, 99,
        00, 111, 221, 331, 441, 551, 661, 771, 881, 991, 001, 211, 222, 233, 244, 255, 266, 277, 288, 299, 200, 11, 22,
        33, 44, 55, 66, 77, 88, 99, 00, 111, 221, 331, 441, 551, 661, 771, 881, 991, 001, 211, 222, 233, 244, 255, 266,
        277, 288, 299, 200, 11, 22, 33, 44, 55, 66, 77, 88, 99, 00, 111, 221, 331, 441, 551, 661, 771, 881, 991, 001,
        211, 222, 233, 244, 255, 266, 277, 288, 299, 200, 11, 22, 33, 44, 55, 66, 77, 88, 99, 00, 111, 221, 331, 441,
        551, 661, 771, 881, 991, 001, 211, 222, 233, 244, 255, 266, 277, 288, 299, 200, 11, 22, 33, 44, 55, 66, 77, 88,
        99, 00, 111, 221, 331, 441, 551, 661, 771, 881, 991, 001, 211, 222, 233, 244, 255, 266, 277, 288, 299, 200, 11,
        22, 33, 44, 55, 66, 77, 88, 99, 00, 111, 221, 331, 441, 551, 661, 771, 881, 991, 001, 211, 222, 233, 244, 255,
        266, 277, 288, 299, 200, 11, 22, 33, 44, 55, 66, 77, 88, 99, 00, 111, 221, 331, 441, 551, 661, 771, 881, 991,
        001, 211, 222, 233, 244, 255, 266, 277, 288, 299, 200 };

    for (int i = 0; i < content.length; i++) {
      content[i] = mult * content[i];
    }
    return content;
  }

  public REXP dataGenerator() {

    REXP data = null;
    final RList a = new RList();

    // Large test data (120000)
    for (int i = 0; i < 12000; i++) {
      a.put(("Col" + i).toString(), new REXPDouble(multiplier(i + Math.random() * 10)));
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
    LOG.info("RConnection Load Testing");
    REXP worker = null;

    try {
      Assert.assertTrue("Cannot Shutdown Rserve, Check if there are permissions "
          + "to shut it down if the process is owned by a different user", Rserve.checkLocalRserve());

      Assert.assertTrue("Rserve Running?", Rserve.isRserveRunning());

      RConnection c = null;
      // Make a connection if we successfully got Rserve running
      Assert.assertNotNull("Cannot Connect to Rserve", c = new RConnection());

      // Testing big data
      c.assign("dataF", this.dataGenerator());

      worker = c.eval("capture.output(print(paste(object.size(dataF))))");
      // c.setSendBufferSize(104857600); // in bytes total 100MB
      LOG.info("worker SIZE: {} in bytes", worker.asString());
      LOG.info("Worker error inherits: {}", worker.inherits("try-error"));

      LOG.info("Closing the Rconnection");
      Assert.assertTrue("Cannot Close connection to Rserve", c.close());
    } catch (final RserveException e) {
      LOG.error("RSERVE error inherits: {}", e.getMessage());
      Assert.fail(e.getMessage());
    } catch (final REXPMismatchException e) {
      LOG.error("Worker error inherits: {}", worker.inherits("try-error"));
      Assert.fail(e.getMessage());
    }
  }

}
