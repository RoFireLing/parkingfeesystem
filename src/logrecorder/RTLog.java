package logrecorder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.io.File.separator;

/**
 * @author phantom
 */
public class RTLog {
    public RTLog() {}

    /**
     * 记录每一个测试用例的执行结果
     * @param filename log文件的名字
     * @param mutantDistribution 变异体的分布情况
     * @param seed seed
     * @param TCID 测试用例ID
     * @param killedmutants 杀死的变异体
     * @param numOfUnkillledMutants 剩下的变异体的数量
     */
    public void recordProcessInfo(String filename, String mutantDistribution, String seed,
                                  String TCID, List<String> killedmutants,String numOfUnkillledMutants){
        String path = System.getProperty("user.dir") + separator + "logs" + separator + filename;
        File file = new File(path);
        if (!file.exists())
            createFile(path);
        //获取杀死的变异体
        String mutantskilled = "";
        for (int i = 0; i < killedmutants.size(); i++) {
            mutantskilled += killedmutants.get(i) + ",";
        }
        String content = "\n"+"\t"+mutantDistribution + "\t\t\t\t\t\t" + seed + "\t\t\t\t" + TCID + "\t\t\t\t\t"+
                numOfUnkillledMutants + "\t\t\t\t\t\t\t" + mutantskilled;
        try{//将content中得内容写入文件中
            FileWriter fileWriter = new FileWriter(path,true);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 向文件中记录结果
     * @param filename 文件得名字
     * @param mutantDistribution 变异体分布
     * @param fmeasure fmeausre得值
     * @param tmeasure tmeasure的值
     * @param sdrfmwasure 方差
     * @param sdrtmeasure 方差
     * @param time 花费的时间
     */
    public void recordResult(String filename, String mutantDistribution,String fmeasure,String tmeasure,
                             String sdrfmwasure,String sdrtmeasure,double time){
        String path = System.getProperty("user.dir") + separator + "result" + separator + filename;
        File file = new File(path);
        try{
            if (!file.exists()){
                file.createNewFile();
            }
            //向文件中记录结果
            FileWriter fileWriter = new FileWriter(file,true);
            String content = "\n" + "变异体分布：" + mutantDistribution + "\n" + "Fmeasure =" + fmeasure + "\n" + "Tmeasure =" +
                    tmeasure + "\n" + "sdr_Fmeasure =" + sdrfmwasure + "\n" + "sdr_Tmeasure =" + sdrtmeasure + "\n" + "平均时间为："
                    + String.valueOf(time);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 创建文件并且向创建得文件中添加表头
     * @param path
     */
    private void createFile(String path){
        File file = new File(path);
        try{
            file.createNewFile();//创建新的文件
            //向文件中写入抬头
            String content =  "mutants distribution" + "\t\t\t" + "seed" + "\t\t\t" + "TC_ID" + "\t\t\t"+
                    "numOfUnKilledMutants" + "\t\t\t" + "killedMutants";
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RTLog rtLog = new RTLog();
        List<String> lists = new ArrayList<String>();
        lists.add("roa1");
        rtLog.recordProcessInfo("test","M50-50","1","1",lists,"10");
    }

}
