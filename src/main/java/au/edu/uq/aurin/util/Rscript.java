package au.edu.uq.aurin.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rscript {

	protected final static Logger LOG = LoggerFactory.getLogger(Rscript.class);

	// constructor
	public Rscript() {

	}

	/**
	 * Loads the given R script and returns a String representation
	 * 
	 * @param scriptName
	 *            path and the name of R script. 
	 *            For example: "/scripts/scriptName.r"
	 * @return {@link String} representation of the loaded R script
	 * @throws IOException
	 *             If unable to load the given scriptName
	 */
	public static String load(String scriptName) throws IOException {

		String script = null;

		InputStream is = Rscript.class.getResourceAsStream(scriptName);
		if (is == null || scriptName.length() == 0) {
			throw new IOException("Invalid Input Rscript: " + scriptName);
		} else {
			script = IOUtils.toString(is).replaceAll(
					System.getProperty("line.separator"),
					IOUtils.LINE_SEPARATOR_UNIX);
			LOG.info("R Script Size: " + script.length() + " Bytes for " + scriptName);
			LOG.debug("R Script: " + scriptName + "\n" + script + "\n");
			is.close();
		}
		return script;
	}
}
