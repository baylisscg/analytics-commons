package au.edu.uq.aurin.logging;

import java.io.File;
import java.io.IOException;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.RList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.edu.uq.aurin.util.StatisticsException;

/**
 * {@link Rlogger} setup the logging infrastructure in R
 * @author irfan
 *
 */
public final class Rlogger {

  private static final String LOGGEROFF = "OFF";

  private static String level = LOGGEROFF;
  private static String directory = null;

  private static final Logger LOG = LoggerFactory.getLogger(Rlogger.class);
  
  private static REXP logOptions;

  private Rlogger() {
  }
  
  /**
   * Default logging is OFF
   * @throws StatisticsException
   */
  public static void logger() throws StatisticsException {
    // logLevel = "OFF", logDirectory = java temp directory
    String tmpDir = System.getProperty("java.io.tmpdir");
    logger(LOGGEROFF, tmpDir);
    LOG.info("Java Temp Path = {}", tmpDir);
  }

  /**
   * Setup a particular logging level and a custom directory for log files
   * @param logLevel OFF, ERROR, WARN, INFO, DEBUG
   * @param logDirectory a writable system directory
   * @throws StatisticsException
   */
  public static void logger(String logLevel, String logDirectory)
      throws StatisticsException {
    setLogLevel(logLevel);
    try {
      setLogDirectory(logDirectory);
    } catch (IOException e) {
      throw new StatisticsException(e);
    }
    setupREXPLogOptions();
  }

  private static void setupREXPLogOptions() throws StatisticsException {
    RList op = new RList();
    op.put("LOG_LEVEL", new REXPString(Rlogger.level));
    op.put("LOG_DIRECTORY", new REXPString(Rlogger.directory));
    try {
      setLogOptions(REXP.createDataFrame(op));
    } catch (REXPMismatchException e) {
      throw new StatisticsException(e);
    }
  }

  public static String getLogLevel() {
    LOG.info("current log level: {}", level);
    return level;
  }

  private static void setLogLevel(String logLevel) {
    if (logLevel != null) {
      if (logLevel.isEmpty() || (logLevel.compareToIgnoreCase("OFF") == 0)) {
        level = LOGGEROFF;
      } else if ((logLevel.compareToIgnoreCase("ERROR") == 0) || (logLevel.compareToIgnoreCase("WARN") == 0)) {
        level = logLevel.toUpperCase();
      } else if((logLevel.compareToIgnoreCase("INFO") == 0) || (logLevel.compareToIgnoreCase("DEBUG") == 0)) {
        level = logLevel.toUpperCase();
      } else if(logLevel.compareToIgnoreCase("TRACE") == 0) {
        level = logLevel.toUpperCase();
      } else {
        level = LOGGEROFF;
      }
    }
  }

  public static String getLogDirectory() {
    LOG.info("current log directory: {}", directory);
    return directory;
  }

  private static void setLogDirectory(String logDirectory)
      throws IOException {

    if (logDirectory != null) {
      if ((!logDirectory.isEmpty()) && (new File(logDirectory).isDirectory()) && (new File(logDirectory).canWrite())) {
        directory = logDirectory;
      } else {
        // null logging
        String msg = "Log directory: " + logDirectory 
            + ", not found. Using: " + directory + " instead.";
        LOG.info(msg);
      }
    }
  }

  public static REXP getLogOptions() {
    return logOptions;
  }

  public static void setLogOptions(REXP logOptions) {
    Rlogger.logOptions = logOptions;
  }

}
