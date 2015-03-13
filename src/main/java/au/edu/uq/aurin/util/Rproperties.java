package au.edu.uq.aurin.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPFactor;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPLogical;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * R connection, session, memory and dataframe properties and compare utilities
 *
 * @author irfan
 */
public final class Rproperties {

  private static final Logger LOG = LoggerFactory.getLogger(Rproperties.class);

  private static final String MSG = "Connection is closed or null: ";
  private static final String PARSE_ERROR = "Unable to parse content: ";
  private static final String OTHER_R_ERROR = "Unable to connect: ";
  private static final String ATTRIBUTE_TYPE = "class";
  private static final String CONTENT_EQUAL = "Content Equal? ";
  private static final String UNABLE_TO_GET_DATAFRAME = "Unable to get DataFrame: ";

  private Rproperties() {
  }

  /**
   * Print the properties of the {@link RConnection}
   *
   * @param c
   *          an {@link RConnection}
   * @throws StatisticsException
   */
  public static void propertiesOfRserve(final RConnection c) throws StatisticsException {

    if (c != null && c.isConnected()) {
      LOG.info("RServe version: " + c.getServerVersion());
      LOG.info("Valid R Connection: " + c.isConnected());
      LOG.info("Login needed? " + c.needLogin());
      LOG.info("Last Error: " + c.getLastError());
      LOG.info("class = " + c.getClass());
      LOG.info("---- Server support features ----");
      LOG.info("Environment support? " + c.supportsEnvironments());
      LOG.info("Locking? " + c.supportsLocking());
      LOG.info("Reference support? " + c.supportsReferences());
      LOG.info("REPL support? " + c.supportsREPL());
    } else {
      LOG.error(MSG + c);
      throw new StatisticsException(MSG + c);
    }
  }

  /**
   * List the current R objects in memory of the {@link RConnection} session
   *
   * @param c
   *          an {@link RConnection}
   * @throws StatisticsException
   */
  public static void objectsOfRSession(final RConnection c) throws StatisticsException {

    try {
      if (c != null && c.isConnected()) {
        LOG.info("--- connected ---");
        final REXP tRexp = c.parseAndEval("print(capture.output(ls()));");
        LOG.info(tRexp.toDebugString());
      } else {
        LOG.error(MSG + c);
        throw new StatisticsException(MSG + c);
      }
    } catch (final REXPMismatchException e) {
      throw new StatisticsException(PARSE_ERROR, e);
    } catch (final REngineException e) {
      throw new StatisticsException(OTHER_R_ERROR, e);
    }
  }

  /**
   * List the R packages loaded in the {@link RConnection} session
   *
   * @param c
   *          an {@link RConnection}
   * @throws StatisticsException
   */
  public static void loadedPackagesOfRSession(final RConnection c) throws StatisticsException {

    try {
      if (c != null && c.isConnected()) {
        LOG.info("--- connected ---");
        final REXP tRexp = c.parseAndEval("print(capture.output(search()));");
        LOG.info(tRexp.toDebugString());
      } else {
        LOG.error(MSG + c);
        throw new StatisticsException(MSG + c);
      }
    } catch (final REXPMismatchException e) {
      throw new StatisticsException(PARSE_ERROR, e);
    } catch (final REngineException e) {
      throw new StatisticsException(OTHER_R_ERROR, e);
    }
  }

  /**
   * Memory profile of an existing {@link RConnection} session
   *
   * @param c
   *          an {@link RConnection}
   * @throws StatisticsException
   *           unable to retrieve {@link RConnection} information
   */
  public static void memoryProfileOfRSession(final RConnection c) throws StatisticsException {

    try {
      if (c.isConnected()) {

        final REXP tRexp = c.parseAndEval("print(capture.output(memory.profile()));");
        LOG.info(tRexp.toDebugString());
      } else {
        LOG.error(MSG + c);
        throw new StatisticsException(MSG + c);
      }
    } catch (final REXPMismatchException e) {
      throw new StatisticsException(PARSE_ERROR, e);
    } catch (final REngineException e) {
      throw new StatisticsException(OTHER_R_ERROR, e);
    }
  }

  /**
   * Print the DataFrame from an {@link RConnection}
   *
   * @param dataFrame
   *          {@link REXP} data frame object
   * @throws StatisticsException
   *           invalid data frame or unable to parse input dataFrame
   */
  public static void printDataFrame(final Object dataFrame) throws StatisticsException {

    try {
      if (dataFrame == null) {
        final String msg = "Input DataFrame is NULL";
        LOG.info(msg);
        throw new StatisticsException(msg);
      } else {
        // df is not null
        final REXP df = (REXP) dataFrame;

        if (df.isList() == true) {
          LOG.info("---- DataFrame ----");
          final RList content = df.asList();

          // check metadata for the list
          final Iterator<?> cmetadata = content.names.iterator();
          LOG.info(content.names.toString());
          // check contents within the list
          // column content
          final Iterator<?> it2 = content.iterator();
          while (it2.hasNext() || cmetadata.hasNext()) {
            // print column name first
            final String cmdata = (String) cmetadata.next();
            LOG.info(cmdata);

            final REXP cont = (REXP) it2.next();
            if (cont instanceof REXPLogical) {
              LOG.info("Logical input");
              final String[] cdata = cont.asStrings();
              LOG.info(cont.toDebugString());
              for (final String d : cdata) {
                LOG.info(d);
              }
            } else if (cont instanceof REXPFactor) {
              LOG.info("Factor input");
              final String[] cdata = cont.asStrings();
              LOG.info(cont.toDebugString());
              for (final String d : cdata) {
                LOG.info(d);
              }
            } else if (cont instanceof REXPString) {
              LOG.info("String input");
              final String[] cdata = cont.asStrings();
              LOG.info(cont.toDebugString());
              for (final String d : cdata) {
                LOG.info(d);
              }
            } else if (cont instanceof REXPInteger) {
              LOG.info("Integer input");
              final int[] cdata = cont.asIntegers();
              LOG.info(cont.toDebugString());
              for (final int d : cdata) {
                if (REXPInteger.isNA(d)) {
                  LOG.info("NA, but actual Integer value is: {}", String.valueOf(d));
                } else {
                  LOG.info(String.valueOf(d));
                }
              }
            } else if (cont instanceof REXPDouble) {
              LOG.info("Double input");
              final double[] cdata = cont.asDoubles();
              LOG.info(cont.toDebugString());
              for (final double d : cdata) {
                if (REXPDouble.isNA(d)) {
                  LOG.info("NA");
                } else {
                  LOG.info(String.valueOf(d));
                }
              }
            } else {
              final String msg = "Unknown type of Input: " + cont.getClass() + ". Content: " + cont.toDebugString();
              LOG.error(msg);
              throw new StatisticsException(msg);
            }
            LOG.info("----------------");
          }
        }
      }
    } catch (final REXPMismatchException e) {
      throw new StatisticsException(PARSE_ERROR, e);
    }
  }

  /**
   * Validation of a dataframe structure. This checks if the {@link REXP} object has
   * the attributes: data-frame, column lists, named columns and REXP* column data types
   *
   * @param dataFrame
   *          of {@link REXP} data frame object
   * @throws StatisticsException
   *           invalid data frame or unable to parse input dataFrame
   */
  public static void dataFrameCheck(final Object dataFrame) throws StatisticsException {

    try {
      if (dataFrame == null) {
        final String msg = "Input DataFrame is NULL ";
        LOG.error(msg);
        throw new StatisticsException(msg);
      }

      // get the dataframe objects
      final REXP df1 = (REXP) dataFrame;

      LOG.trace("df 1 = " + df1.toDebugString());
      if (df1.isList() == false) {
        final String msg = "Invalid list to contain a dataFrame";
        LOG.error(msg);
        throw new StatisticsException(msg);
      }

      LOG.debug("A valid list to contain a dataFrame");
      if (df1.hasAttribute(ATTRIBUTE_TYPE) == false) {
        final String msg = "Content should have the dataframe class attribute";
        LOG.error(msg);
        throw new StatisticsException(msg);
      }

      // Now we can check the names of the dataFrame
      final RList content1 = df1.asList();
      if (content1 == null || content1.isNamed() == false) {
        final String msg = "Column Names are missing in the dataframe";
        LOG.error(msg);
        throw new StatisticsException(msg);
      }
      LOG.debug("dataframe column names: {}", content1.names.toString());

      // Now we can check the contents of the dataFrame
      final Iterator<?> names = content1.names.iterator();
      while (names.hasNext()) {
        final String name = (String) names.next();
        final Object cd1 = content1.at(name);
        if (cd1 instanceof REXP) {
          LOG.info("Column of type: " + cd1.getClass().toString());
        } else {
          final String msg = "Unknown Column type: " + cd1.getClass().toString();
          LOG.error(msg);
          throw new StatisticsException(msg);
        }
      }
    } catch (final REXPMismatchException e) {
      throw new StatisticsException(PARSE_ERROR, e);
    }
  }

  /**
   * Column names of a given dataFrame
   *
   * @param dataframe
   *          {@link REXP} object
   * @return column names of the dataFrame
   */
  public static String[] dataFrameColumnNames(final Object dataframe) throws StatisticsException {

    String[] columnNames = null;
    try {
      // get the dataframe objects
      dataFrameCheck(dataframe);

      // Now we can check the names of the dataFrame
      final REXP df1 = (REXP) dataframe;
      final RList content1 = df1.asList();

      LOG.info("dataframe column names = " + content1.names.toString());
      columnNames = (String[]) content1.names.toArray(new String[content1.names.size()]);
      LOG.info("DataFrame Colnames:");
      for (final String e : columnNames) {
        LOG.info(e);
      }

    } catch (final REXPMismatchException e) {
      throw new StatisticsException(PARSE_ERROR + " dataFrame: " + dataframe, e);
    }
    return columnNames;
  }

  /**
   * Extract column names from an {@link RConnection} and a dataframe name
   *
   * @param c
   *          A valid {@link RConnection} input
   * @param dataFrameName
   *          A valid name of dataframe
   * @return {@link String} array of column names
   * @throws StatisticsException
   *           Unable to locate named dataframe or parse dataframe
   */
  public static String[] dataFrameColumnNames(final RConnection c, final String dataFrameName)
      throws StatisticsException {

    String[] columnNames = null;
    try {
      if (c != null && c.isConnected()) {
        final REXP df = c.get(dataFrameName, null, true);
        columnNames = dataFrameColumnNames(df);
      } else {
        LOG.error(MSG + c);
        throw new StatisticsException(MSG + c);
      }
    } catch (final REngineException e) {
      throw new StatisticsException(PARSE_ERROR + dataFrameName, e);
    }
    return columnNames;
  }

  /**
   * Compare dataframes in an {@link RConnection} for equality
   *
   * @param dframe1
   *          {@link REXP} data frame object
   * @param dframe2
   *          {@link REXP} data frame object
   * @return true if the data frames are equal in column names, data types and data
   * @throws StatisticsException
   *           unable to parse the input data frames
   */
  public static boolean compare2DataFrames(final Object dframe1, final Object dframe2) throws StatisticsException {

    // Result store
    final ConcurrentHashMap<String, Boolean> validMap = new ConcurrentHashMap<String, Boolean>();
    boolean valid = false;

    try {
      // get the dataframe objects
      final REXP df1 = (REXP) dframe1;
      final REXP df2 = (REXP) dframe2;

      if (df1 == null || df2 == null) {
        final String msg = "Input DataFrame 1: " + dframe1 + " DataFrame 2: " + dframe2;
        LOG.info(msg);
        throw new StatisticsException(msg);
      }

      LOG.trace("df 1 = " + df1.toDebugString());
      LOG.trace("df 2 = " + df2.toDebugString());

      LOG.info("1 dataFrame attribute valid? " + df1.hasAttribute(ATTRIBUTE_TYPE));
      LOG.info("2 dataFrame attribute valid? " + df2.hasAttribute(ATTRIBUTE_TYPE));

      if (df1.isList() == false || df2.isList() == false) {
        final String msg = "List 1 && List 2 are Invalid lists to contain a dataFrame";
        LOG.info(msg);
        throw new StatisticsException(msg);
      }

      LOG.info("List 1 && List 2 are valid lists to contain a dataFrame");
      if (df1.hasAttribute(ATTRIBUTE_TYPE) == false || df2.hasAttribute(ATTRIBUTE_TYPE) == false) {
        final String msg = "content should have the class attribute";
        LOG.info(msg);
        throw new StatisticsException(msg);
      }

      if (!df1.getAttribute(ATTRIBUTE_TYPE).asString().contentEquals(df2.getAttribute(ATTRIBUTE_TYPE).asString())) {
        final String msg = "The contents of the class attribute must contain the value 'data.frame'";
        LOG.info(msg);
        throw new StatisticsException(msg);
      }

      // Now we can check the names and content inside the 2 dataFrames
      final RList content1 = df1.asList();
      final RList content2 = df2.asList();

      LOG.info("1 names = " + content1.names.toString());
      LOG.info("2 names = " + content2.names.toString());
      LOG.info("Vector equal? " + content1.names.containsAll(content2.names));

      final Iterator<?> names = content1.names.iterator();
      // check if the column names are the same
      if (content1.names.containsAll(content2.names)) {
        // all names are contained in both the dataframes
        while (names.hasNext()) {
          final String name = (String) names.next();
          LOG.info("name = " + name);

          final Object cd1 = content1.at(name);
          final Object cd2 = content2.at(name);

          if (cd1.getClass().equals(cd2.getClass())) {
            // now the class types are equal
            LOG.info("cd1 class: " + cd1.getClass() + ", cd2 class: " + cd2.getClass());
            if (cd1 instanceof REXPLogical && cd2 instanceof REXPLogical) {
              LOG.info("Logical");
              final String[] cd1data = ((REXPLogical) cd1).asStrings();
              final String[] cd2data = ((REXPLogical) cd2).asStrings();

              valid = Arrays.equals(cd1data, cd2data);
              validMap.put(names.next().toString(), valid);
              LOG.info(CONTENT_EQUAL + valid);
              for (final String e1 : cd1data) {
                LOG.info(e1);
              }
            } else if (cd1 instanceof REXPFactor && cd2 instanceof REXPFactor) {
              LOG.info("Factor");
              final String[] cd1data = ((REXPFactor) cd1).asStrings();
              final String[] cd2data = ((REXPFactor) cd2).asStrings();

              valid = Arrays.equals(cd1data, cd2data);
              validMap.put(names.next().toString(), valid);
              LOG.info(CONTENT_EQUAL + valid);
              for (final String e1 : cd1data) {
                LOG.info(e1);
              }
            } else if (cd1 instanceof REXPString && cd2 instanceof REXPString) {
              LOG.info("Strings");
              final String[] cd1data = ((REXPString) cd1).asStrings();
              final String[] cd2data = ((REXPString) cd2).asStrings();

              valid = Arrays.equals(cd1data, cd2data);
              validMap.put(names.next().toString(), valid);
              LOG.info(CONTENT_EQUAL + valid);
              for (final String e1 : cd1data) {
                LOG.info(e1);
              }
            } else if (cd1 instanceof REXPInteger && cd2 instanceof REXPInteger) {
              LOG.info("integers");
              final int[] cd1data = ((REXPInteger) cd1).asIntegers();
              final int[] cd2data = ((REXPInteger) cd2).asIntegers();

              valid = Arrays.equals(cd1data, cd2data);
              validMap.put(names.next().toString(), valid);
              LOG.info(CONTENT_EQUAL + valid);
              for (final int e1 : cd1data) {
                LOG.info(String.valueOf(e1));
              }
            } else if (cd1 instanceof REXPDouble && cd2 instanceof REXPDouble) {
              LOG.info("doubles");
              final double[] cd1data = ((REXPDouble) cd1).asDoubles();
              final double[] cd2data = ((REXPDouble) cd2).asDoubles();

              valid = Arrays.equals(cd1data, cd2data);
              validMap.put(names.next().toString(), valid);
              LOG.info(CONTENT_EQUAL + valid);
              for (final double e1 : cd1data) {
                LOG.info(String.valueOf(e1));
              }
            } else {
              validMap.put(names.next().toString(), false);
              final String msg = "Error: Unknown column content types: " + cd1.getClass() + " or " + cd2.getClass();
              LOG.info(msg);
              return false;
            }
          } else {
            final String msg = "Error: Got different content/class types for dataFrame 1: " + cd1.getClass()
                + " and dataFrame 2: " + cd2.getClass();
            LOG.info(msg);
            return false;
          }
        }
      }
    } catch (final REXPMismatchException e) {
      throw new StatisticsException(PARSE_ERROR, e);
    }

    return !validMap.containsValue(false);
  }

  /**
   * Compare 2 dataframes from a single {@link RConnection} for equality
   *
   * @param c
   *          {@link RConnection} a valid connection to R
   * @param dataFrameName1
   *          {@link String} data frame name
   * @param dataFrameName2
   *          {@link String} data frame name
   * @return true if the data frames are equal in column names, data types and data
   * @throws StatisticsException
   *           unable to parse the input data frames
   */
  public static boolean compare2DataFrames(final RConnection c, final String dataFrameName1, final String dataFrameName2)
      throws StatisticsException {

    boolean result = false;
    try {
      if (c != null && c.isConnected()) {
        final REXP df1 = c.get(dataFrameName1, null, true);
        final REXP df2 = c.get(dataFrameName2, null, true);
        result = compare2DataFrames(df1, df2);
      } else {
        LOG.error(MSG + c);
        throw new StatisticsException(MSG + c);
      }
    } catch (final REngineException e) {
      throw new StatisticsException(UNABLE_TO_GET_DATAFRAME + dataFrameName1 + ", " + dataFrameName2, e);
    }
    return result;
  }

  /**
   * Compare 2 dataframes from 2 different {@link RConnection}'s for equality
   *
   * @param c1
   *          First {@link RConnection} input with attached dataFrame
   * @param dataFrameName1
   *          {@link String} data frame name
   * @param c2
   *          Second {@link RConnection} input with attached dataFrame
   * @param dataFrameName2
   *          {@link String} data frame name
   * @return true if the data frames are equal in column names, data types and data
   * @throws StatisticsException
   *           unable to parse the input data frames
   */
  public static boolean compare2DataFrames(final RConnection c1, final String dataFrameName1, final RConnection c2,
      final String dataFrameName2) throws StatisticsException {

    boolean result = false;

    REXP df1 = null;
    REXP df2 = null;

    try {
      if (c1 != null && c1.isConnected()) {
        df1 = c1.get(dataFrameName1, null, true);
      } else {
        LOG.error(MSG + c1);
        throw new StatisticsException(MSG + c1);
      }
      if (c2 != null && c2.isConnected()) {
        df2 = c2.get(dataFrameName2, null, true);
      } else {
        LOG.error(MSG + c2);
        throw new StatisticsException(MSG + c2);
      }
      result = compare2DataFrames(df1, df2);
    } catch (final REngineException e) {
      throw new StatisticsException(UNABLE_TO_GET_DATAFRAME + dataFrameName1 + ", " + dataFrameName2, e);
    }
    return result;
  }

  /**
   * Print an R data frame from an {@link RConnection} and a dataframe name
   *
   * @param c
   *          A valid {@link RConnection} input
   * @param dataFrameName
   *          A valid Name of dataframe
   * @throws StatisticsException
   *           Unable to locate named dataframe or parse dataframe
   */
  public static void printDataFrame(final RConnection c, final String dataFrameName) throws StatisticsException {

    try {
      if (c != null && c.isConnected()) {
        final REXP df = c.get(dataFrameName, null, true);
        printDataFrame(df);
      } else {
        LOG.error(MSG + c);
        throw new StatisticsException(MSG + c);
      }
    } catch (final REngineException e) {
      throw new StatisticsException(PARSE_ERROR + dataFrameName, e);
    }
  }

  /**
   * Print an R data frame from a single {@link RConnection} attached with two dataframes by name
   *
   * @param c
   *          A valid {@link RConnection} input with 2 attached dataFrames
   * @param dataFrameName1
   *          A valid Name of first dataframe
   * @param dataFrameName2
   *          A valid Name of second dataframe
   * @throws StatisticsException
   *           Unable to locate named dataframes or parse dataframes
   */
  public static void printDataFrame(final RConnection c, final String dataFrameName1, final String dataFrameName2)
      throws StatisticsException {

    try {
      if (c != null && c.isConnected()) {
        final REXP df1 = c.get(dataFrameName1, null, true);
        final REXP df2 = c.get(dataFrameName2, null, true);
        printDataFrame(df1);
        printDataFrame(df2);
      } else {
        LOG.error(MSG + c);
        throw new StatisticsException(MSG + c);
      }
    } catch (final REngineException e) {
      throw new StatisticsException(UNABLE_TO_GET_DATAFRAME + dataFrameName1 + ", " + dataFrameName2, e);
    }
  }

  /**
   * Print an R data frame from 2 different {@link RConnection}'s attached with dataframe by name
   *
   * @param c1
   *          First {@link RConnection} input with attached dataFrame
   * @param dataFrameName1
   *          A valid Name of first dataframe
   * @param c2
   *          Second {@link RConnection} input with attached dataFrame
   * @param dataFrameName2
   *          A valid Name of second dataframe
   * @throws StatisticsException
   *           Unable to connect or locate named dataframes or parse dataframes
   */
  public static void printDataFrame(final RConnection c1, final String dataFrameName1, final RConnection c2,
      final String dataFrameName2) throws StatisticsException {

    REXP df1 = null;
    REXP df2 = null;

    try {
      if (c1 != null && c1.isConnected()) {
        df1 = c1.get(dataFrameName1, null, true);
      } else {
        LOG.error(MSG + c1);
        throw new StatisticsException(MSG + c1);
      }
      if (c2 != null && c2.isConnected()) {
        df2 = c2.get(dataFrameName2, null, true);
      } else {
        LOG.error(MSG + c2);
        throw new StatisticsException(MSG + c2);
      }

      printDataFrame(df1);
      printDataFrame(df2);
    } catch (final REngineException e) {
      throw new StatisticsException(UNABLE_TO_GET_DATAFRAME + dataFrameName1 + ", " + dataFrameName2, e);
    }
  }

}
