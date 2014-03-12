package au.edu.uq.aurin.util.tests;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class RLoadTest {

  public double[] multiplier(double mult) {

    double[] content = new double[] { 
        11, 22, 33, 44, 55, 66, 77, 88, 99, 00,
        111, 221, 331, 441, 551, 661, 771, 881, 991, 001,
        211, 222, 233, 244, 255, 266, 277, 288, 299, 200,
        11, 22, 33, 44, 55, 66, 77, 88, 99, 00,
        111, 221, 331, 441, 551, 661, 771, 881, 991, 001,
        211, 222, 233, 244, 255, 266, 277, 288, 299, 200,
        11, 22, 33, 44, 55, 66, 77, 88, 99, 00,
        111, 221, 331, 441, 551, 661, 771, 881, 991, 001,
        211, 222, 233, 244, 255, 266, 277, 288, 299, 200,
        11, 22, 33, 44, 55, 66, 77, 88, 99, 00,
        111, 221, 331, 441, 551, 661, 771, 881, 991, 001,
        211, 222, 233, 244, 255, 266, 277, 288, 299, 200,
        11, 22, 33, 44, 55, 66, 77, 88, 99, 00,
        111, 221, 331, 441, 551, 661, 771, 881, 991, 001,
        211, 222, 233, 244, 255, 266, 277, 288, 299, 200,
        11, 22, 33, 44, 55, 66, 77, 88, 99, 00,
        111, 221, 331, 441, 551, 661, 771, 881, 991, 001,
        211, 222, 233, 244, 255, 266, 277, 288, 299, 200,
        11, 22, 33, 44, 55, 66, 77, 88, 99, 00,
        111, 221, 331, 441, 551, 661, 771, 881, 991, 001,
        211, 222, 233, 244, 255, 266, 277, 288, 299, 200,
        11, 22, 33, 44, 55, 66, 77, 88, 99, 00,
        111, 221, 331, 441, 551, 661, 771, 881, 991, 001,
        211, 222, 233, 244, 255, 266, 277, 288, 299, 200,
        11, 22, 33, 44, 55, 66, 77, 88, 99, 00,
        111, 221, 331, 441, 551, 661, 771, 881, 991, 001,
        211, 222, 233, 244, 255, 266, 277, 288, 299, 200,
        11, 22, 33, 44, 55, 66, 77, 88, 99, 00,
        111, 221, 331, 441, 551, 661, 771, 881, 991, 001,
        211, 222, 233, 244, 255, 266, 277, 288, 299, 200,
        11, 22, 33, 44, 55, 66, 77, 88, 99, 00,
        111, 221, 331, 441, 551, 661, 771, 881, 991, 001,
        211, 222, 233, 244, 255, 266, 277, 288, 299, 200,
        11, 22, 33, 44, 55, 66, 77, 88, 99, 00,
        111, 221, 331, 441, 551, 661, 771, 881, 991, 001,
        211, 222, 233, 244, 255, 266, 277, 288, 299, 200 };

    for (int i = 0; i < content.length; i++) {
      content[i] = mult * content[i];
    }
    return content;
  }

  public REXP dataGenerator() {

    REXP data = null;
    RList a = new RList();
    
    // Large test data (120000)
    for (int i = 0; i < 12000; i++) {
      a.put(("Col" + i).toString(), new REXPDouble(multiplier(i + Math.random() * 10)));
    }

    try {
      data = REXP.createDataFrame(a);
    } catch (REXPMismatchException e) {
      e.printStackTrace();
    }

    return data;
  }
  
  @Test
  public void test() {
    System.out.println("RConnection Load Testing");

    REXP worker = null;
    
    try {
//      Rserve.checkLocalRserve();
//
//      Assert.assertTrue("Rserve Running?", Rserve.isRserveRunning());
      RConnection c = null;

      // Make a connection if we successfully got Rserve running
      Assert.assertNotNull("Cannot Connect to Rserve", c = new RConnection());
      
      // Testing big data
      c.assign("dataF", this.dataGenerator());

      worker = c.eval("capture.output(print(paste(object.size(dataF))))");
//      c.setSendBufferSize(104857600); // in bytes total 100MB
      System.out.println("worker = " + worker.asString() + " in bytes");
      System.out.println("error inherits = " + worker.inherits("try-error"));
      
      System.out.println("got a connection, so closing the connection");
      Assert.assertTrue("Cannot Close connection to Rserve", c.close());
    } catch (RserveException e) {
      System.out.println("RSERVE error inherits = " + worker.inherits("try-error"));

      Assert.fail(e.getMessage());
    } catch (REXPMismatchException e) {
      System.out.println("REXP error inherits = " + worker.inherits("try-error"));

      Assert.fail(e.getMessage());
    }
  }

}
