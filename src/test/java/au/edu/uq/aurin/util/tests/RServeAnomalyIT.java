package au.edu.uq.aurin.util.tests;

import au.edu.uq.aurin.util.Rproperties;
import au.edu.uq.aurin.util.StatisticsException;
import au.org.aurin.testing.RConnectionRule;
import org.junit.Rule;
import org.junit.Test;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author pgreenwood, irfan */
public class RServeAnomalyIT {

  private static final Logger LOG = LoggerFactory.getLogger(RServeAnomalyIT.class);

  public @Rule RConnectionRule rConnectionRule = new RConnectionRule();

  /*
   * Test data in a dataFrame that generates correct attribute sizes for columns:
   * nullCol02 and stringCol
   */
  public REXP dataGenerator() throws REXPMismatchException {

    REXP data = null;

    final double[] doubleCol = new double[] {1.1, 122.45564463, 0.0, 22.2, REXPDouble.NA};
    final int[] integerCol = new int[] {4, 543435423, REXPInteger.NA, 0, -454};
    final String[] nullCol = new String[] {null, null, null, null, null};
    final String[] nullCol02 = new String[] {"", "", "", "", ""};
    final String[] stringCol = new String[] {"", "", "cat", "dog", ""};
    final String[] stringCol02 = new String[] {"", "pig", "cat", "dog", ""};
    final double[] doubleCol02 = new double[] {1.1, 122.45564463, 0.0, 22.2, REXPDouble.NA};

    final RList a = new RList();
    // add each column separately
    a.put("doubleCol", new REXPDouble(doubleCol));
    a.put("integerCol", new REXPInteger(integerCol));
    a.put("nullCol", new REXPString(nullCol));
    a.put("nullCol02", new REXPString(nullCol02));
    a.put("stringCol", new REXPString(stringCol));
    a.put("stringCol02", new REXPString(stringCol02));
    a.put("doubleCol02", new REXPDouble(doubleCol02));

    return REXP.createDataFrame(a);
  }

  /*
   * FIXED: upgrade Rserve/RserveEnging to 1.8.x version
   *
   * Wrong attribute sizes for retrieved dataFrame with get() when compared to the original dataFrame
   *
   * Issue: 'attr2 size' of original dataframe for column names: 'nullCol02' and 'stringCol'
   * In original dataframe attr1 size is 5 and 5
   * In retrieved dataframe attr2 size is 9 and 9
   * all other columns have attribute sizes correct
   */
  @Test
  public void test() throws REXPMismatchException, REngineException, StatisticsException {

    final RConnection c = this.rConnectionRule.getConnection();

    // 1. generate test dataframe
    final REXP inData = this.dataGenerator();

    // 2. data for REXP population (original dataFrame)
    c.assign("dataF", inData);

    // Original dataFrame debug
    final RList inpDataRList = inData.asList();
    final String[] attrNames1 = inpDataRList.keys();
    Object attr1 = null;
    LOG.info("Original dataFrame: ");
    LOG.info("-----------------------");
    for (int i = 0; i < attrNames1.length; i++) {
      LOG.info("attrName1: " + attrNames1[i]);
      attr1 = inpDataRList.get(i);
      LOG.info("attr1 type: " + attr1.getClass().getName());
      LOG.info("attr1 size: " + ((REXP) attr1).asStrings().length + "\n");
    }

    // 3. get the assigned dataFrame (this should be the copy of the original)
    final REXP dataFrame = c.get("dataF", null, true);

    // Retrieved data frame with get
    final RList dataRList = dataFrame.asList();
    final String[] attrNames2 = dataRList.keys();
    Object attr = null;
    LOG.info("Retrieved dataFrame: ");
    LOG.info("-------------------------");
    for (int i = 0; i < attrNames2.length; i++) {
      LOG.info("attrName2: " + attrNames2[i]);
      attr = dataRList.get(i);
      LOG.info("attr2 type: " + attr.getClass().getName());
      LOG.info("attr2 size: " + ((REXP) attr).asStrings().length + "\n");
    }

    // print original dataframe and retrieved dataframe for another comparison
    LOG.info("Original dataFrame: ");
    LOG.info("-----------------------");
    LOG.info(inData.toDebugString());
    Rproperties.printDataFrame(inData);

    LOG.info("Retrieved dataFrame: ");
    LOG.info("-------------------------");
    LOG.info(dataFrame.toDebugString());
    Rproperties.printDataFrame(dataFrame);
  }
}
