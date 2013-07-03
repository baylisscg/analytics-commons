package au.edu.uq.aurin.logging;

import java.io.File;
import java.io.IOException;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.RList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rlogger {

	private static int LEVEL;
	private static String DIRECTORY;

	protected static final Logger LOG = LoggerFactory.getLogger(Rlogger.class);

	public static REXP logOptions;

	public static void logger() throws REXPMismatchException, IOException {
		logger(0, System.getProperty("java.io.tmpdir")); // logLevel = 0, logDirectory = java temp directory
		LOG.info("Java Temp Path = {}", System.getProperty("java.io.tmpdir"));
	}

	public static void logger(int log_level, String log_directory) throws REXPMismatchException, IOException {
		setLog_level(log_level);
		setLog_directory(log_directory);
		setupREXPLogOptions();
	}

	private static void setupREXPLogOptions() throws REXPMismatchException {
		RList op = new RList();
		op.put("LOG_LEVEL", new REXPInteger(Rlogger.LEVEL));
		op.put("LOG_DIRECTORY", new REXPString(Rlogger.DIRECTORY));
		logOptions = REXP.createDataFrame(op);
	}

	public static int getLog_level() {
		LOG.info("current log level: {}", LEVEL);
		return LEVEL;
	}

	private static void setLog_level(int log_level) {
		if (log_level > 0 && log_level < 6) {
			LEVEL = log_level;
		} else if (log_level > 6) {
			LEVEL = 5; // max log level
		} else {
			LEVEL = 0; // no logging
		}
	}

	public static String getLog_directory() {
		LOG.info("current log directory: {}", DIRECTORY);
		return DIRECTORY;
	}

	private static void setLog_directory(String log_directory) throws IOException {
		DIRECTORY = "."; // default log directory = "."
		if (log_directory != null) {
			if ((!log_directory.isEmpty()) && (new File(log_directory).isDirectory())) {
				DIRECTORY = log_directory;
			} else {
				String msg = "Log directory: " + log_directory + ", not found."; 
				LOG.info(msg);
				DIRECTORY = null; // null logging
//				throw new IOException(msg);
			}
		}
	}

}
