package au.edu.uq.aurin.util;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
public class Rproperties {

  private static final Logger LOG = LoggerFactory.getLogger(Rproperties.class);

  private Rproperties() {
  }

  /**
   * Print the properties of the {@link RConnection}
   *
   * @param c an {@link RConnection}
   * @throws StatisticsException
   */
  public static void propertiesOfRserve(RConnection c) throws StatisticsException {

    if(c!= null && c.isConnected()) {
      LOG.info("RServe version: " + c.getServerVersion());
      LOG.info("Valid R Connection: " + c.isConnected());
      LOG.info("Login needed? " + c.needLogin());
      LOG.info("Last Error: " + c.getLastError());
      LOG.info("class = " + c.getClass());
      LOG.info("---- Server support features ----");
      LOG.info("Environment support? "+ c.supportsEnvironments());
      LOG.info("Locking? " + c.supportsLocking());
      LOG.info("Reference support? " + c.supportsReferences());
      LOG.info("REPL support? " + c.supportsREPL());
    } else {
      String msg = "Connection is closed or null: " + c;
      LOG.error(msg);
      throw new StatisticsException(msg);
    }
  }

  /**
   * List the current R objects in memory of the {@link RConnection} session
   *
   * @param c an {@link RConnection}
   * @throws StatisticsException
   */
  public static void objectsOfRSession(RConnection c) throws StatisticsException {

    try {
      if(c!= null && c.isConnected()) {
        LOG.info("--- connected ---");
        REXP tRexp = c.parseAndEval("print(capture.output(ls()));");
        LOG.info(tRexp.toDebugString());
      } else {
        String msg = "Connection is closed or null: " + c;
        LOG.error(msg);
        throw new StatisticsException(msg);
      }
    } catch (REXPMismatchException e) {
      throw new StatisticsException("Unable to parse: " + e.getMessage());
    } catch (REngineException e) {
      throw new StatisticsException("Unable to connect: " + e.getMessage());
    }
  }

  /**
   * List the R packages loaded in the {@link RConnection} session
   *
   * @param c an {@link RConnection}
   * @throws StatisticsException
   */
  public static void loadedPackagesOfRSession(RConnection c) throws StatisticsException {

    try {
      if(c!= null && c.isConnected()) {
        LOG.info("--- connected ---");
        REXP tRexp = c.parseAndEval("print(capture.output(search()));");
        LOG.info(tRexp.toDebugString());
      } else {
        String msg = "Connection is closed or null: " + c;
        LOG.error(msg);
        throw new StatisticsException(msg);
      }
    } catch (REXPMismatchException e) {
      throw new StatisticsException("Unable to parse: " + e.getMessage());
    } catch (REngineException e) {
      throw new StatisticsException("Unable to connect: " + e.getMessage());
    }
  }

  /**
   * Memory profile of an existing {@link RConnection} session
   *
   * @param c an {@link RConnection}
   * @throws StatisticsException unable to retrieve {@link RConnection} information
   */
  public void memoryProfileOfRSession(RConnection c) throws StatisticsException {

    try {
      if(c.isConnected()) {

        REXP tRexp = c.parseAndEval("print(capture.output(memory.profile()));"); //gc(TRUE); print(capture.output(memory.profile()))");
//        tRexp = c.parseAndEval("gc(TRUE);");
//        tRexp = c.parseAndEval("print(capture.output(ls()));");
//        tRexp = c.parseAndEval("print(capture.output(memory.profile()));");
//        tRexp = c.parseAndEval("print(capture.output(search()));");
        LOG.info(tRexp.toDebugString());
      } else {
        LOG.info("Connection is closed or null: " + c);
      }
    } catch (Exception e) {
      throw new StatisticsException("Unable to connect: " + e.getMessage());
    }
  }

  /**
   * Print the DataFrame from an {@link RConnection}
   *
   * @param dataFrame {@link REXP} data frame object
   * @throws StatisticsException invalid data frame or unable to parse input dataFrame
   */
  public static void printDataFrame(Object dataFrame) throws StatisticsException {

    try {
      REXP df = (REXP) dataFrame;

      if(df == null) {
        String msg = "Input DataFrame is: " + dataFrame;
        LOG.info(msg);
        throw new StatisticsException(msg);
      } else {
        // df is not null
        if(df.isList() == true) {
          LOG.info("---- DataFrame ----");
          RList content = df.asList();

          // check metadata for the list
          Iterator<?> cmetadata = content.names.iterator();
          LOG.info(content.names.toString());
          // check contents within the list
          // column content
          Iterator<?> it2 = content.iterator();
          while (it2.hasNext() || cmetadata.hasNext()) {
            // print column name first
            String cmdata = (String) cmetadata.next();
            LOG.info(cmdata);

            REXP cont = (REXP) it2.next();
            if(cont instanceof REXPLogical) {
              LOG.info("Logical input");
              String[] cdata = cont.asStrings();
              LOG.info(cont.toDebugString());
              for (String d : cdata) {
                LOG.info(d + "\t");
              }
            } else if(cont instanceof REXPFactor ) {
              LOG.info("Factor input");
              String[] cdata = cont.asStrings();
              LOG.info(cont.toDebugString());
              for (String d : cdata) {
                LOG.info(d + "\t");
              }
            } else if (cont instanceof REXPString) {
              LOG.info("String input");
              String[] cdata = cont.asStrings();
              LOG.info(cont.toDebugString());
              for (String d : cdata) {
                LOG.info(d + "\t");
              }
            } else if (cont instanceof REXPInteger) {
              LOG.info("Integer input");
              int[] cdata = cont.asIntegers();
              LOG.info(cont.toDebugString());
              for (int d : cdata) {
                LOG.info(d + "\t");
              }
            } else if(cont instanceof REXPDouble) {
                LOG.info("Double input");
                double[] cdata = cont.asDoubles();
                LOG.info(cont.toDebugString());
                for (double d : cdata) {
                  LOG.info(d + "\t");
                }
            } else {
              String msg = "Unknown type of Input: " + cont.getClass() + ". Content: " + cont.toDebugString();
              LOG.error(msg);
              throw new StatisticsException(msg);
            }
            LOG.info("----------------");
          }
        }
      }
    } catch (REXPMismatchException e) {
      throw new StatisticsException("Unable to parse dataFrame: " + e.getMessage());
    }
  }

  /**
   * Compare dataframes in an {@link RConnection} for equality
   *
   * @param dframe1 {@link REXP} data frame object
   * @param dframe2 {@link REXP} data frame object
   * @return true if the data frames are equal in column names, data types and data
   * @throws StatisticsException unable to parse the input data frames
   */
  public static boolean compare2DataFrames(Object dframe1, Object dframe2) throws StatisticsException {
    
    Map<String, Boolean> validMap = null;
    boolean valid = false;
    
    try {
      // get the dataframe objects
      REXP df1 = (REXP) dframe1;
      REXP df2 = (REXP) dframe2;

      LOG.info("1 dataFrame attribute valid? " + df1.hasAttribute("class"));
      LOG.info("2 dataFrame attribute valid? " + df2.hasAttribute("class"));
      if(df1 == null || df2 == null) {
        String msg = "Input DataFrame 1: " + dframe1 + " DataFrame 2: " + dframe2;
        LOG.info(msg);
        throw new StatisticsException(msg);
      }
      
      if(df1.isList() == false || df2.isList() == false) {
        String msg = "List 1 && List 2 are Invalid lists to contain a dataFrame";
        LOG.info(msg);
        throw new StatisticsException(msg);
      }

      LOG.info("List 1 && List 2 are valid lists to contain a dataFrame");
      if(df1.hasAttribute("class") == false || df2.hasAttribute("class") == false) {
        String msg = "content should have the class attribute";
        LOG.info(msg);
        throw new StatisticsException(msg);
      }

      if(!df1.getAttribute("class").asString().contentEquals(df2.getAttribute("class").asString())) {
        String msg = "The contents of the class attribute must contain the value 'data.frame'";
        LOG.info(msg);
        throw new StatisticsException(msg);
      }

      // Now we can check the names and content inside the 2 dataFrames
      RList content1 = df1.asList();
      RList content2 = df2.asList();

      LOG.info("1 names = " + content1.names.toString());
      LOG.info("2 names = " + content2.names.toString());
      LOG.info("Vector equal? " + content1.names.equals(content2.names));

      Iterator<?> names = content1.names.iterator();
      // check if the column names are the same
      if(content1.names.equals(content2.names)) {
        // result store
        validMap = new HashMap<String, Boolean>();
        
        Iterator<?> cont1it = content1.iterator();
        Iterator<?> cont2it = content2.iterator();
        while (cont1it.hasNext() && cont2it.hasNext()) {
          Object cd1 = cont1it.next();
          Object cd2 = cont2it.next();
          if(cd1.getClass().equals(cd2.getClass())) {
            // now the class types are equal
            LOG.info("cd1 class: " + cd1.getClass() + ", cd2 class: " + cd2.getClass());
            if(cd1 instanceof REXPLogical && cd2 instanceof REXPLogical) {
              LOG.info("Logical");
              String[] cd1data = ((REXPLogical) cd1).asStrings();
              String[] cd2data = ((REXPLogical) cd2).asStrings();

              valid = Arrays.equals(cd1data, cd2data);
              validMap.put(names.next().toString(), valid);
              LOG.info("Content Equal? " + valid);
              for (String e1 : cd1data) {
                LOG.info(e1);
              }
            } else if(cd1 instanceof REXPFactor && cd2 instanceof REXPFactor) {
              LOG.info("Factor");
              String[] cd1data = ((REXPFactor) cd1).asStrings();
              String[] cd2data = ((REXPFactor) cd2).asStrings();

              valid = Arrays.equals(cd1data, cd2data);
              validMap.put(names.next().toString(), valid);
              LOG.info("Content Equal? " + valid);
              for (String e1 : cd1data) {
                LOG.info(e1);
              }
            } else if(cd1 instanceof REXPString && cd2 instanceof REXPString) {
              LOG.info("Strings");
              String[] cd1data = ((REXPString) cd1).asStrings();
              String[] cd2data = ((REXPString) cd2).asStrings();

              valid = Arrays.equals(cd1data, cd2data);
              validMap.put(names.next().toString(), valid);
              LOG.info("Content Equal? " + valid);
              for (String e1 : cd1data) {
                LOG.info(e1);
              }
            } else if(cd1 instanceof REXPInteger && cd2 instanceof REXPInteger) {
              LOG.info("integers");
              int[] cd1data = ((REXPInteger) cd1).asIntegers();
              int[] cd2data = ((REXPInteger) cd2).asIntegers();

              valid = Arrays.equals(cd1data, cd2data);
              validMap.put(names.next().toString(), valid);
              LOG.info("Content Equal? " + valid);
              for (int e1 : cd1data) {
                LOG.info(String.valueOf(e1));
              }
            } else if(cd1 instanceof REXPDouble && cd2 instanceof REXPDouble) {
              LOG.info("doubles");
              double[] cd1data = ((REXPDouble) cd1).asDoubles();
              double[] cd2data = ((REXPDouble) cd2).asDoubles();

              valid = Arrays.equals(cd1data, cd2data);
              validMap.put(names.next().toString(), valid);
              LOG.info("Content Equal? " + valid);
              for (double e1 : cd1data) {
                LOG.info(String.valueOf(e1));
              }
            } else {
              validMap.put(names.next().toString(), false);
              String msg = "Error: Unknown column content types: " + cd1.getClass() + " or " + cd2.getClass();
              LOG.info(msg);
              return false;
            }
          } else {
            String msg = "Error: Got different content/class types for dataFrame 1: " + cd1.getClass() + " and dataFrame 2: " + cd2.getClass();
            LOG.info(msg);
            return false;
          }
        }
      } else {
        String msg = "Column Names: " + content1.names.toString() + " and " + content2.names.toString() + " do not match.";
        LOG.info(msg);
        return false;
      }
    } catch (REXPMismatchException e) {
      throw new StatisticsException("Unable to parse dataFrame: " + e.getMessage());
    }
    
    return (!validMap.containsValue(false));
  }

  /**
   * Compare 2 dataframes from a single {@link RConnection} for equality
   *
   * @param dataFrameName1 {@link String} data frame name
   * @param dataFrameName2 {@link String} data frame name
   * @return true if the data frames are equal in column names, data types and data
   * @throws StatisticsException unable to parse the input data frames
   */
  public static boolean compare2DataFrames(RConnection c, String dataFrameName1, String dataFrameName2) throws StatisticsException {

    boolean result = false;
    try {
      if (c!= null && c.isConnected()) {
        REXP df1 = c.get(dataFrameName1, null, true);
        REXP df2 = c.get(dataFrameName2, null, true);
        result = compare2DataFrames(df1, df2);
      } else {
        String msg = "Connection is closed or null: " + c;
        LOG.error(msg);
        throw new StatisticsException(msg);
      }
    } catch (REngineException e) {
      throw new StatisticsException("Unable to get dataFrame1: " + dataFrameName1 +" and/or dataFrame2: " + dataFrameName2 + " " + e.getMessage());
    }
    return result;
  }

  /**
   * Compare 2 dataframes from 2 different {@link RConnection}'s for equality
   *
   * @param c1 First {@link RConnection} input with attached dataFrame
   * @param dataFrameName1 {@link String} data frame name
   * @param c2 Second {@link RConnection} input with attached dataFrame
   * @param dataFrameName2 {@link String} data frame name
   * @return true if the data frames are equal in column names, data types and data
   * @throws StatisticsException unable to parse the input data frames
   */
  public static boolean compare2DataFrames(RConnection c1, String dataFrameName1, RConnection c2, String dataFrameName2) throws StatisticsException {

    boolean result = false;

    REXP df1 = null;
    REXP df2 = null;

    try {
      if (c1!= null && c1.isConnected()) {
        df1 = c1.get(dataFrameName1, null, true);
      } else {
        String msg = "Connection is closed or null: " + c1;
        LOG.error(msg);
        throw new StatisticsException(msg);
      }
      if (c2!= null && c2.isConnected()) {
        df2 = c2.get(dataFrameName2, null, true);
      } else {
        String msg = "Connection is closed or null: " + c2;
        LOG.error(msg);
        throw new StatisticsException(msg);
      }
      result = compare2DataFrames(df1, df2);
    } catch (REngineException e) {
      throw new StatisticsException("Unable to get dataFrame1: " + dataFrameName1 +" and/or dataFrame2: " + dataFrameName2 + " " + e.getMessage());
    }
    return result;
  }

  /**
   * Print an R data frame from an {@link RConnection} and a dataframe name
   *
   * @param c A valid {@link RConnection} input
   * @param dataFrameName A valid Name of dataframe
   * @throws StatisticsException Unable to locate named dataframe or parse dataframe
   */
  public static void printDataFrame(RConnection c, String dataFrameName) throws StatisticsException {

    try {
      if (c!= null && c.isConnected()) {
        REXP df = c.get(dataFrameName, null, true);
        printDataFrame(df);
      } else {
        String msg = "Connection is closed or null: " + c;
        LOG.error(msg);
        throw new StatisticsException(msg);
      }
    } catch (REngineException e) {
      throw new StatisticsException("Unable to get dataFrame: " + dataFrameName +" " + e.getMessage());
    }
  }

  /**
   * Print an R data frame from a single {@link RConnection} attached with two dataframes by name
   *
   * @param c A valid {@link RConnection} input with 2 attached dataFrames
   * @param dataFrameName1 A valid Name of first dataframe
   * @param dataFrameName2 A valid Name of second dataframe
   * @throws StatisticsException Unable to locate named dataframes or parse dataframes
   */
  public static void printDataFrame(RConnection c, String dataFrameName1, String dataFrameName2) throws StatisticsException {

    try {
      if (c!= null && c.isConnected()) {
        REXP df1 = c.get(dataFrameName1, null, true);
        REXP df2 = c.get(dataFrameName2, null, true);
        printDataFrame(df1);
        printDataFrame(df2);
      } else {
        String msg = "Connection is closed or null: " + c;
        LOG.error(msg);
        throw new StatisticsException(msg);
      }
    } catch (REngineException e) {
      throw new StatisticsException("Unable to get dataFrame1: " + dataFrameName1 +" and/or dataFrame2: " + dataFrameName2 + " " + e.getMessage());
    }
  }
  
  /**
   * Print an R data frame from 2 different {@link RConnection}'s attached with dataframe by name
   *
   * @param c1 First {@link RConnection} input with attached dataFrame
   * @param dataFrameName1 A valid Name of first dataframe
   * @param c2 Second {@link RConnection} input with attached dataFrame
   * @param dataFrameName2 A valid Name of second dataframe
   * @throws StatisticsException Unable to connect or locate named dataframes or parse dataframes
   */
  public static void printDataFrame(RConnection c1, String dataFrameName1, RConnection c2, String dataFrameName2) throws StatisticsException {

    REXP df1 = null;
    REXP df2 = null;

    try {
      if (c1!= null && c1.isConnected()) {
        df1 = c1.get(dataFrameName1, null, true);
      } else {
        String msg = "Connection is closed or null: " + c1;
        LOG.error(msg);
        throw new StatisticsException(msg);
      }
      if (c2!= null && c2.isConnected()) {
        df2 = c2.get(dataFrameName2, null, true);
      } else {
        String msg = "Connection is closed or null: " + c2;
        LOG.error(msg);
        throw new StatisticsException(msg);
      }

      printDataFrame(df1);
      printDataFrame(df2);
    } catch (REngineException e) {
      throw new StatisticsException("Unable to get dataFrame1: " + dataFrameName1 +" and/or dataFrame2: " + dataFrameName2 + " " + e.getMessage());
    }
  }

}
