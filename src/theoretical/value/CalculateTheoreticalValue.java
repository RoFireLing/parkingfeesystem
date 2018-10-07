package theoretical.value;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 计算理论区间，
 * 并返回区间1/4,2/4，3/4的值
 */

public class CalculateTheoreticalValue {

    /**区间的下届*/
    private double min ;



    /**
     * 输入最小的失效率得到区间的上届和下届
     * @param thetaMin 最小的失效率
     * @param numberOfPartitions 分区的数目
     */
    public void calculateInterval(double thetaMin, int numberOfPartitions){
        min = (2 * numberOfPartitions * thetaMin * thetaMin) / (1 - 2 * thetaMin) ;


        /**将计算结果写入文件中*/
        writeResultsToFile(String.valueOf(numberOfPartitions));
    }

    /**
     * 将3个值写入到result文件下的theoreticalValue.txt中
     */
    private void writeResultsToFile(String numberOfPartitions){
        String path = System.getProperty("user.dir") + File.separator + "result" +
                File.separator + "theoreticalValue" + numberOfPartitions + ".txt";
        File file = new File(path);

        if (file.exists())
            file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuffer sb = new StringBuffer(10);
        sb.append("quaterValue: " + String.valueOf(min) + "\n");


        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(file));
            printWriter.write(sb.toString());
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sb.delete(0,sb.length());
    }

    public static void main(String[] args) {
        CalculateTheoreticalValue ct = new CalculateTheoreticalValue();
        ct.calculateInterval(0.001492,3);
    }



}
