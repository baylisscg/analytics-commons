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

public class Rlogger {

  private static String LEVEL;
  private static String DIRECTORY;

  private static final Logger LOG = LoggerFactory.getLogger(Rlogger.class);

  private static REXP logOptions;

  private Rlogger() {
  }
  
  public static void logger() throws StatisticsException {
    // logLevel = "OFF", logDirectory = java temp directory
    String tmpDir = System.getProperty("java.io.tmpdir");
    logger("OFF", tmpDir);
    LOG.info("Java Temp Path = {}", tmpDir);
  }

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
    op.put("LOG_LEVEL", new REXPString(Rlogger.LEVEL));
    op.put("LOG_DIRECTORY", new REXPString(Rlogger.DIRECTORY));
    try {
      setLogOptions(REXP.createDataFrame(op));
    } catch (REXPMismatchException e) {
      throw new StatisticsException(e);
    }
  }

  public static String getLogLevel() {
    LOG.info("current log level: {}", LEVEL);
    return LEVEL;
  }

  private static void setLogLevel(String logLevel) {
    if (logLevel != null) {
      if (logLevel.isEmpty() || (logLevel.compareToIgnoreCase("OFF") == 0)) {
        LEVEL = "OFF";
      } else if ((logLevel.compareToIgnoreCase("ERROR") == 0) || (logLevel.compareToIgnoreCase("WARN") == 0)) {
        LEVEL = logLevel.toUpperCase();
      } else if((logLevel.compareToIgnoreCase("INFO") == 0) || (logLevel.compareToIgnoreCase("DEBUG") == 0)) {
        LEVEL = logLevel.toUpperCase();
      } else if(logLevel.compareToIgnoreCase("TRACE") == 0) {
        LEVEL = logLevel.toUpperCase();
      } else {
        LEVEL = "OFF";
      }
    }
  }

  public static String getLogDirectory() {
    LOG.info("current log directory: {}", DIRECTORY);
    return DIRECTORY;
  }

  private static void setLogDirectory(String logDirectory)
      throws IOException {
    // default log directory = "."
    DIRECTORY = ".";
    if (logDirectory != null) {
      if ((!logDirectory.isEmpty())
          && (new File(logDirectory).isDirectory())) {
        DIRECTORY = logDirectory;
      } else {
        String msg = "Log directory: " + logDirectory + ", not found.";
        LOG.info(msg);
        // null logging
        DIRECTORY = null;
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
