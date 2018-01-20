package dependentVariable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RTMeasure {
    private static final String PATH = "RTResult.txt";
    private List<Integer> Fmeasure;
    private List<Integer> Tmeasure;
    private List<Integer> NFmeasure;
    public RTMeasure(){
        Fmeasure = new ArrayList<Integer>();
        Tmeasure = new ArrayList<Integer>();
        NFmeasure = new ArrayList<Integer>();
    }
    public void addFmeasure(int item){
        Fmeasure.add(item);
    }

    public void addTmeasure(int item){
        Tmeasure.add(item);
    }

    public void addNFmeasure(int item) {NFmeasure.add(item);}

    public int sizeFmeasure(){
        return Fmeasure.size();
    }

    public int sizeTmeasure(){
        return Tmeasure.size();
    }

    public int sizeNFmeasure(){ return  NFmeasure.size();}

    public String getMeanFmeasure(){
        double sum = 0.0 ;
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        for (int i = 0; i < this.Fmeasure.size(); i++) {
            sum = sum + Fmeasure.get(i);
        }
        return decimalFormat.format(sum / Fmeasure.size());
    }

    public String getMeanNFmeasure(){
        double sum = 0.0 ;
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        for (int i = 0; i < this.NFmeasure.size(); i++) {
            sum = sum + NFmeasure.get(i);
        }
        return decimalFormat.format(sum / NFmeasure.size());
    }

    public String getMeanTmeasure(){
        double sum = 0.0 ;
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        for (int i = 0; i < this.Tmeasure.size(); i++) {
            sum = sum + Tmeasure.get(i);
        }
        return decimalFormat.format(sum / Tmeasure.size());
    }

    /**
     * 返回Fmeasure的标准差
     * @return 标准差
     */
    public String getStandardDevOfFmeasure(){
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return decimalFormat.format(Math.sqrt(varianceOfArray(0,this.Fmeasure)));
    }

    /**
     * 返回Fmeasure的标准差
     * @return 标准差
     */
    public String getStandardDevOfNFmeasure(){
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return decimalFormat.format(Math.sqrt(varianceOfArray(2,this.NFmeasure)));
    }

    /**
     * 返回Tmeasure的标准差
     * @return 标准差
     */
    public String getStandardDevOfTmeasure(){
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return decimalFormat.format(Math.sqrt(varianceOfArray(1,this.Tmeasure)));
    }
    private double varianceOfArray(int temp,List<Integer> dataArray) {
        double result = 0.0;
        double mean = 0.0;
        if (temp == 0)
            mean = Double.parseDouble(getMeanFmeasure());
        else if (temp == 1)
            mean = Double.parseDouble(getMeanTmeasure());
        else
            mean = Double.parseDouble(getMeanNFmeasure());

        for (int i = 0; i < dataArray.size(); i++) {
            result += Math.pow((dataArray.get(i) - mean),2);
        }
        return result / (dataArray.size() - 1);
    }

    @Override
    public String toString() {
        return "RTMeasure{" +
                "Fmeasure=" + Fmeasure +
                ", Tmeasure=" + Tmeasure +
                ", NFmeasure=" + NFmeasure +
                '}';
    }
}
