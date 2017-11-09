package dependentVariable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RPTMeasure implements Measure {
    private List<Double> fmeasure ;
    private List<Double> tmeasure ;
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.00");
    public RPTMeasure() {
        fmeasure = new ArrayList<Double>();
        tmeasure = new ArrayList<Double>();
    }

    /**
     * 向fmeasure列表中添加元素
     * @param item 要添加的元素
     */
    @Override
    public void addFmeasure(int item) {

    }

    /**
     * 向tmeasure列表中添加元素
     * @param item 要添加的元素
     */
    @Override
    public void addTmeasure(int item) {

    }
    public void addFmeasure(double item){
        fmeasure.add(item);
    }
    public void addTmeasure(double item){
        tmeasure.add(item);
    }


    @Override
    public int sizeFmeasure() {
        return fmeasure.size();
    }

    @Override
    public int sizeTmeasure() {
        return tmeasure.size();
    }

    @Override
    public String getMeanFmeasure() {
        double sum = 0.0;
        for (int i = 0; i < fmeasure.size(); i++) {
            sum += fmeasure.get(i);
        }
        return decimalFormat.format(sum / fmeasure.size());
    }

    @Override
    public String getMeanTmeasure() {
        double sum = 0.0;
        for (int i = 0; i < tmeasure.size(); i++) {
            sum += tmeasure.get(i);
        }
        return decimalFormat.format(sum / tmeasure.size());
    }

    @Override
    public String getstandardDevofFmeasure() { return decimalFormat.format(Math.sqrt(varianceofArray(0,this.fmeasure))); }


    @Override
    public String getstandardDevofTmeasure() { return decimalFormat.format(Math.sqrt(varianceofArray(1,this.tmeasure))); }


    public double varianceofArray(int temp, List<Double> array) {
        double result = 0.0;
        double mean = 0.0;
        if (temp == 0)
            mean = Double.parseDouble(getMeanFmeasure());
        else
            mean = Double.parseDouble(getMeanTmeasure());
        for (int i = 0; i < array.size(); i++) {
            result = Math.pow(array.get(i)-mean,2);
        }
        return result / (array.size() - 1);
    }

    @Override
    public void toprint() {
        System.out.println("Fmeasure:");
        String f = "";
        for (int i = 0; i < fmeasure.size(); i++) {
            f += String.valueOf(fmeasure.get(i)) + ",";
        }
        System.out.println(f);
        System.out.println("Tmeasure:");
        String t = "";
        for (int i = 0; i < tmeasure.size(); i++) {
            t += String.valueOf(tmeasure.get(i)) + ",";
        }
        System.out.println(t);
    }
}
