package dependentVariable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author phantom
 */
public class DRTMeasure extends RPTMeasure {
    private List<Double> fmeasure ;
    private List<Double> tmeasure ;
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.00");
    public DRTMeasure() {
        super();
        fmeasure = new ArrayList<Double>();
        tmeasure = new ArrayList<Double>();
    }
}
