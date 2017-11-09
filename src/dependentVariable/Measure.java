package dependentVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author phantom
 */
public interface Measure {
     void addFmeasure(int item);
     void addTmeasure(int item);
     int sizeFmeasure();
     int sizeTmeasure();
     String getMeanFmeasure();
     String getMeanTmeasure();
     String getstandardDevofFmeasure();
    String getstandardDevofTmeasure();
    void toprint();
}
