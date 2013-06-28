package au.edu.uq.aurin.logging.tests;

import junit.framework.Assert;
import org.junit.Test;

import au.edu.uq.aurin.logging.Rlogger;

public class RloggerTest {

	@Test
	public void test() {
		System.out.println("--- RLogging Test ---");

		try {
			
			// Sample usage from Java variables setup
			int LOG_LEVEL = 40;
			String LOG_DIRECTORY = "/tmp";
			
			Rlogger.logger(LOG_LEVEL, LOG_DIRECTORY);
			Assert.assertEquals(Rlogger.getLog_level(), (LOG_LEVEL > 5) ? 5: LOG_LEVEL);
			Assert.assertEquals(Rlogger.getLog_directory(), LOG_DIRECTORY);
			Assert.assertNotNull("REXPLogger 1", Rlogger.logOptions.toDebugString());
//			System.out.println(Rlogger.logOptions.toDebugString());

			LOG_LEVEL = 2;
			LOG_DIRECTORY = "/tmp";
			
			Rlogger.logger(LOG_LEVEL, LOG_DIRECTORY);
			Assert.assertEquals(Rlogger.getLog_level(), (LOG_LEVEL > 5) ? 5: LOG_LEVEL);
			Assert.assertEquals(Rlogger.getLog_directory(), LOG_DIRECTORY);
			Assert.assertNotNull("REXPLogger 2", Rlogger.logOptions.toDebugString());
//			System.out.println(Rlogger.logOptions.toDebugString());

			// Default logging
			Rlogger.logger();
			
			System.out.println("--- Done R Logging Test ---");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("R Logging Failure");
		}

	}

}
