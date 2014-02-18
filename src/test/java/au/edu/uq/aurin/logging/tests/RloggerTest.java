package au.edu.uq.aurin.logging.tests;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.Assert;
import org.junit.Test;

import au.edu.uq.aurin.logging.Rlogger;

public class RloggerTest {

  @Test
  public void test() {

    try {
      // Sample usage from Java variables setup
      String LOG_LEVEL = "BLAH";
      String LOG_DIRECTORY = "/tmp";

      Rlogger.logger(LOG_LEVEL, LOG_DIRECTORY);
      Assert.assertEquals(Rlogger.getLogDirectory(), LOG_DIRECTORY);
      Assert.assertNotNull("REXPLogger 1", Rlogger.getLogOptions().toDebugString());
      System.out.println(Rlogger.getLogOptions().toDebugString());

      LOG_LEVEL = "DEBUG";
      LOG_DIRECTORY = "/tmp";

      Rlogger.logger(LOG_LEVEL, LOG_DIRECTORY);
      Assert.assertEquals(Rlogger.getLogDirectory(), LOG_DIRECTORY);
      Assert.assertNotNull("REXPLogger 2", Rlogger.getLogOptions().toDebugString());
      System.out.println(Rlogger.getLogOptions().toDebugString());

      // Default logging
      Rlogger.logger();
      System.out.println(Rlogger.getLogOptions().toDebugString());

    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail("Test Failed: " + ExceptionUtils.getFullStackTrace(e));
    }
  }

}
