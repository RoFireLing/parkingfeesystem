package executor;

import independentVariable.DRT;
import independentVariable.RPT;
import independentVariable.RT;

/**
 * @author phantom
 */
public class Main {
    public static void main(String[] args) {
        RT rt = new RT();
        RPT rpt = new RPT();
        DRT drt = new DRT();
        rt.randomTesting();
        rpt.randomPartitionTesting();
        drt.dynamicRandomTesting();
    }

}
