package au.edu.uq.aurin.util.tests;

import au.org.aurin.testing.RConnectionRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RLoadIT {

  private static final Logger LOG = LoggerFactory.getLogger(RLoadIT.class);

  @Rule public RConnectionRule rConnectionRule = new RConnectionRule();

  public double[] multiplier(final double mult) {

    final double[] content =
        new double[] {
          11, 22, 33, 44, 55, 66, 77, 88, 99, 00, 111, 221, 331, 441, 551, 661, 771, 881, 991, 001,
          211, 222, 233, 244, 255, 266, 277, 288, 299, 200, 11, 22, 33, 44, 55, 66, 77, 88, 99, 00,
          111, 221, 331, 441, 551, 661, 771, 881, 991, 001, 211, 222, 233, 244, 255, 266, 277, 288,
          299, 200, 11, 22, 33, 44, 55, 66, 77, 88, 99, 00, 111, 221, 331, 441, 551, 661, 771, 881,
          991, 001, 211, 222, 233, 244, 255, 266, 277, 288, 299, 200, 11, 22, 33, 44, 55, 66, 77,
          88, 99, 00, 111, 221, 331, 441, 551, 661, 771, 881, 991, 001, 211, 222, 233, 244, 255,
          266, 277, 288, 299, 200, 11, 22, 33, 44, 55, 66, 77, 88, 99, 00, 111, 221, 331, 441, 551,
          661, 771, 881, 991, 001, 211, 222, 233, 244, 255, 266, 277, 288, 299, 200, 11, 22, 33, 44,
          55, 66, 77, 88, 99, 00, 111, 221, 331, 441, 551, 661, 771, 881, 991, 001, 211, 222, 233,
          244, 255, 266, 277, 288, 299, 200, 11, 22, 33, 44, 55, 66, 77, 88, 99, 00, 111, 221, 331,
          441, 551, 661, 771, 881, 991, 001, 211, 222, 233, 244, 255, 266, 277, 288, 299, 200, 11,
          22, 33, 44, 55, 66, 77, 88, 99, 00, 111, 221, 331, 441, 551, 661, 771, 881, 991, 001, 211,
          222, 233, 244, 255, 266, 277, 288, 299, 200, 11, 22, 33, 44, 55, 66, 77, 88, 99, 00, 111,
          221, 331, 441, 551, 661, 771, 881, 991, 001, 211, 222, 233, 244, 255, 266, 277, 288, 299,
          200, 11, 22, 33, 44, 55, 66, 77, 88, 99, 00, 111, 221, 331, 441, 551, 661, 771, 881, 991,
          001, 211, 222, 233, 244, 255, 266, 277, 288, 299, 200, 11, 22, 33, 44, 55, 66, 77, 88, 99,
          00, 111, 221, 331, 441, 551, 661, 771, 881, 991, 001, 211, 222, 233, 244, 255, 266, 277,
          288, 299, 200, 11, 22, 33, 44, 55, 66, 77, 88, 99, 00, 111, 221, 331, 441, 551, 661, 771,
          881, 991, 001, 211, 222, 233, 244, 255, 266, 277, 288, 299, 200
        };

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

  @Test(timeout = 5000)
  public void test() throws RserveException, REXPMismatchException {
    LOG.info("RConnection Load Testing");
    REXP worker = null;

    RConnection c = this.rConnectionRule.getConnection();
    // Make a connection if we successfully got Rserve running
    Assert.assertNotNull("Cannot Connect to Rserve", c);

    // Testing big data
    c.assign("dataF", this.dataGenerator());

    worker = c.eval("capture.output(print(paste(object.size(dataF))))");
    // c.setSendBufferSize(104857600); // in bytes total 100MB
    LOG.info("worker SIZE: {} in bytes", worker.asString());
    LOG.info("Worker error inherits: {}", worker.inherits("try-error"));

    LOG.info("Closing the Rconnection");
    Assert.assertTrue("Cannot Close connection to Rserve", c.close());
  }
}
