package au.edu.uq.aurin.interfaces;

import au.edu.uq.aurin.util.StatisticsException;

/**
 * All R statistics packages must implement the {@link Statistics} interface
 * 
 * @author irfan
 * 
 */
public interface Statistics {

  /**
   * Computes the desired statistical algorithm implemented as an R-script with
   * an Rserve backend, connected via {@link Rserve}.
   * 
   * @throws StatisticsException
   *           Unable to compute Statistical Computation
   */
  void compute() throws StatisticsException;

  /**
   * Print the results in a readable, printable format
   *
   *@return String representation of a Statistical Computation
   *
   * @throws StatisticsException
   *           Unable to parse Statistical Computation
   */
  String prettyPrint() throws StatisticsException;

}
