package au.edu.uq.aurin.logging.tests;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import au.edu.uq.aurin.logging.Rlogger;

public class RloggerTest {

  @Rule
  public ExpectedException ee = ExpectedException.none();

  @Test
  public void defaultLogLevelTest() {

    try {
      // Default logging
      Rlogger.logger();
      System.out.println(Rlogger.getLogOptions().toDebugString());

    } catch (final Exception e) {
      e.printStackTrace();
      Assert.fail("Default log level and directory Failed: " + ExceptionUtils.getFullStackTrace(e));
    }
  }

  @Test
  public void correctLogLevelTest() {

    try {
      // Sample usage from Java variables setup
      final String LOG_LEVEL = "DEBUG";
      final String LOG_DIRECTORY = "/tmp";

      Rlogger.logger(LOG_LEVEL, LOG_DIRECTORY);
      Assert.assertEquals(Rlogger.getLogDirectory(), LOG_DIRECTORY);
      Assert.assertNotNull("REXPLogger 2", Rlogger.getLogOptions().toDebugString());
      System.out.println(Rlogger.getLogOptions().toDebugString());

    } catch (final Exception e) {
      e.printStackTrace();
      Assert.fail("Correct log leve and directory Failed: " + ExceptionUtils.getFullStackTrace(e));
    }
  }

  @Test
  public void wrongLogLevelTest() {

    try {
      // Sample usage from Java variables setup
      final String LOG_LEVEL = "BLAH";
      final String LOG_DIRECTORY = "/tmp";

      Rlogger.logger(LOG_LEVEL, LOG_DIRECTORY);
      Assert.assertEquals(Rlogger.getLogDirectory(), LOG_DIRECTORY);
      Assert.assertNotNull("REXPLogger 1", Rlogger.getLogOptions().toDebugString());
      System.out.println(Rlogger.getLogOptions().toDebugString());

    } catch (final Exception e) {
      e.printStackTrace();
      Assert.fail("Invalid log level Failed: " + ExceptionUtils.getFullStackTrace(e));
    }
  }

  @Test
  public void traceLogLevelTest() {

    try {
      // Sample usage from Java variables setup
      final String LOG_LEVEL = "TRACE";
      final String LOG_DIRECTORY = "/tmp";

      Rlogger.logger(LOG_LEVEL, LOG_DIRECTORY);
      Assert.assertEquals(Rlogger.getLogDirectory(), LOG_DIRECTORY);
      Assert.assertNotNull("REXPLogger 1", Rlogger.getLogOptions().toDebugString());
      System.out.println(Rlogger.getLogOptions().toDebugString());

    } catch (final Exception e) {
      e.printStackTrace();
      Assert.fail("Invalid log level Failed: " + ExceptionUtils.getFullStackTrace(e));
    }
  }

  @Test(expected = AssertionError.class)
  public void traceLogLevelWithNonExistingDirTest() {

    try {
      // Sample usage from Java variables setup
      final String LOG_LEVEL = "TRACE";
      final String LOG_DIRECTORY = "/tm";

      Rlogger.logger(LOG_LEVEL, LOG_DIRECTORY);
      Assert.assertEquals(Rlogger.getLogDirectory(), LOG_DIRECTORY);
      Assert.assertNotNull("REXPLogger 1", Rlogger.getLogOptions().toDebugString());
      System.out.println(Rlogger.getLogOptions().toDebugString());

    } catch (final Exception e) {
      e.printStackTrace();
      Assert.fail("Invalid log level Failed: " + ExceptionUtils.getFullStackTrace(e));
    }
  }

  @Test(expected = AssertionError.class)
  public void notWritableLogDirectoryTest() {

    try {
      // Sample usage from Java variables setup
      final String LOG_LEVEL = "DEBUG";
      final String LOG_DIRECTORY = "/lib";

      Rlogger.logger(LOG_LEVEL, LOG_DIRECTORY);
      Assert.assertEquals(Rlogger.getLogDirectory(), LOG_DIRECTORY);
      Assert.assertNotNull("REXPLogger 2", Rlogger.getLogOptions().toDebugString());
      System.out.println(Rlogger.getLogOptions().toDebugString());

    } catch (final Exception e) {
      e.printStackTrace();
      Assert.fail("Writable directory Failed: " + ExceptionUtils.getFullStackTrace(e));
    }
  }

}
