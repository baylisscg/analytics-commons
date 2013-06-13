package au.edu.uq.aurin.interfaces;

import java.io.IOException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;

import au.edu.uq.aurin.util.Rserve;

/**
 * All R statistics packages must implement the
 * {@link Statistics} interface
 * 
 * @author irfan
 * 
 */
public interface Statistics {

	/**
	 * Computes the desired statistical algorithm implemented as an R-script
	 * with an Rserve backend, connected via {@link Rserve}.
	 * 
	 * @throws RserveException
	 *             Unable to connect to the Rserve instance
	 * @throws IOException 
	 *             Unable to load the R-script file for execution
	 * @throws REXPMismatchException
	 *             Result does not match the expected value
	 */
	public abstract void compute() throws RserveException, IOException,
			REXPMismatchException;
	
	/**
	 * Print the results in a readable, printable format
	 */
	public abstract String prettyPrint();
	
}
