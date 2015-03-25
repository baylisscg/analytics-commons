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
 * {@link Rlogger} setup the logging infrastructure in R.
 * This is used with AURIN WORKFLOW to setup the
 * log levels and the logging directory.
 *
 * Default log level: OFF
 * Default log directory: TMP_LOG_DIRECTORY which is set by:
 * System.getProperty("java.io.tmpdir")
 *
 * Invalid log level is defaulted to OFF
 * Invalid log directory is defaulted to TMP_LOG_DIRECTORY
 *
 * R supports the following log levels:
 * OFF, ERROR, WARN, INFO, DEBUG
 *
 * R does not support log level: TRACE
 *
 * @author irfan
 *
 */
public final class Rlogger {

  private static final String LOGGEROFF = "OFF";

  private static String level = LOGGEROFF;
  private static String directory;

  private static final String TMP_LOG_DIRECTORY = System.getProperty("java.io.tmpdir");

  private static final Logger LOG = LoggerFactory.getLogger(Rlogger.class);

  private static REXP logOptions;

  private Rlogger() {
  }

  /**
   * Default logging is OFF
   *
   * @throws StatisticsException
   */
  public static void logger() throws StatisticsException {
    // logLevel = "OFF", logDirectory = java temp directory
    final String tmpDir = TMP_LOG_DIRECTORY;
    logger(LOGGEROFF, tmpDir);
    LOG.trace("Java Temp Path: {}", tmpDir);
  }

  /**
   * Setup a particular logging level and a custom directory for log files
   *
   * @param logLevel
   *          OFF, ERROR, WARN, INFO, DEBUG
   * @param logDirectory
   *          a writable system directory
   * @throws StatisticsException
   */
  public static void logger(final String logLevel, final String logDirectory) throws StatisticsException {
    setLogLevel(logLevel);
    try {
      setLogDirectory(logDirectory);
    } catch (final IOException e) {
      throw new StatisticsException(e.getMessage(), e);
    }
    setupREXPLogOptions();
  }

  private static void setupREXPLogOptions() throws StatisticsException {
    LOG.trace("final level: {}", level);
    LOG.trace("final directory: {}", directory);

    final RList op = new RList();
    op.put("LOG_LEVEL", new REXPString(Rlogger.level));
    op.put("LOG_DIRECTORY", new REXPString(Rlogger.directory));
    try {
      setLogOptions(REXP.createDataFrame(op));
    } catch (final REXPMismatchException e) {
      throw new StatisticsException(e.getMessage(), e);
    }
  }

  public static String getLogLevel() {
    LOG.trace("current log level: {}", level);
    return level;
  }

  private static void setLogLevel(final String logLevel) {

    LOG.trace("setting log level: {}", logLevel);

    if (logLevel == null || logLevel.isEmpty() || logLevel.compareToIgnoreCase("OFF") == 0) {
      level = LOGGEROFF;
    } else if (logLevel.compareToIgnoreCase("ERROR") == 0 || logLevel.compareToIgnoreCase("WARN") == 0) {
      level = logLevel.toUpperCase();
    } else if (logLevel.compareToIgnoreCase("INFO") == 0 || logLevel.compareToIgnoreCase("DEBUG") == 0) {
      level = logLevel.toUpperCase();
    } else if (logLevel.compareToIgnoreCase("TRACE") == 0) {
      // R does not support 'TRACE' so we default to DEBUG
      LOG.warn("R does not support 'TRACE', defaulting to: 'DEBUG'");
      level = "DEBUG";
    } else {
      level = LOGGEROFF;
    }
  }

  public static String getLogDirectory() {
    LOG.trace("current log directory: {}", directory);
    return directory;
  }

  private static void setLogDirectory(final String logDirectory) throws IOException {

    LOG.trace("got initial logDirectoy: {}", logDirectory);
    if (logDirectory != null) {
      if (!logDirectory.isEmpty() && new File(logDirectory).isDirectory() && new File(logDirectory).canWrite()) {
        directory = logDirectory;
        LOG.trace("setting directory: {}", directory);
      } else {
        // null logging
        directory = TMP_LOG_DIRECTORY;
        final String msg = "Unable to use: " + logDirectory + ". Using: " + TMP_LOG_DIRECTORY + " instead.";
        LOG.trace("Null logging directory: {}", msg);
      }
    }

  }

  public static REXP getLogOptions() {
    return logOptions;
  }

  public static void setLogOptions(final REXP logOptions) {
    Rlogger.logOptions = logOptions;
  }

}
