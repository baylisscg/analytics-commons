package au.edu.uq.aurin.util.tests;


import junit.framework.Assert;

import org.junit.Test;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import au.edu.uq.aurin.util.Rserve;

public class RserveTest {

	/**
	 * Test method to start and stop Rserve
	 */
	@Test
	public void rserveTest() {

		Assert.assertTrue(
				"Cannot Shutdown Rserve, Check if there are permissions "
						+ "to shut it down if the process is owned by a different user",
				Rserve.checkLocalRserve());

		RConnection c = null;

		try {
			// Make a connection if we successfully got Rserve running
			Assert.assertNotNull("Cannot Connect to Rserve",
					c = new RConnection());

			System.out.println("got a connection, so closing the connection");

			Assert.assertTrue("Cannot Close connection to Rserve", c.close());

			Assert.assertTrue(
					"Cannot Shutdown Rserve, Check if Rserve.conf is has "
							+ "control enabled, Check if there are permissions to shut "
							+ "it down if the process is owned by a different user",
					Rserve.shutdownRserve());
		} catch (RserveException re) {
			re.printStackTrace();
			Assert.fail("Rserve Exception");
		} finally {
			if (c != null)
				c.close();
		}
	}
}
