package logrecorder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static java.io.File.separator;

/**
 * describe:
 *
 * @author phantom
 * @date 2019/06/19
 */
public class ATLog {

    public ATLog(String logName) {
        createFile(logName);
    }

    /**
     * 记录AT在测试过程中的信息
     * @param seed 种子
     * @param TCId 测试用例id
     * @param killedmutants 杀死的变异体的名字
     * @param numOfUnkilledMutants 剩下变异体的名字
     */
    public void recordProcessInfo(String seed, String TCId, String partition,
                                  List<String> killedmutants, String numOfUnkilledMutants){
        String path = System.getProperty("user.dir") + separator +
                "logs" + separator + "AT_log.txt";
        File file = new File(path);
        String mutantskilled = "";
        for (int i = 0; i < killedmutants.size(); i++) {
            mutantskilled += killedmutants.get(i);
        }
        String cotent = seed + "\t\t\t\t" + TCId + "\t\t\t\t" + partition +
                "\t\t\t\t\t\t"+mutantskilled + "\t\t\t\t\t\t\t\t" + numOfUnkilledMutants;
        try{
            FileWriter fileWriter = new FileWriter(path,true);
            fileWriter.write("\n"+cotent);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recordResult(String filename,String fmeasure,String nfmeasure,
                             String tmeasure,String sdrfmeasure, String sdrnfmeasure,
                             String sdrtmeasure,int numOfpartitions,
                             double Ftime,double F2time,double Ttime){
        String path = System.getProperty("user.dir")+ separator
                + "result" + separator + filename;
        File file = new File(path);
        if (!file.exists()){
            try{
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //向文件中记录结果
        try{
            FileWriter fileWriter = new FileWriter(file,true);
            String cotent = "\n"+"分区："+String.valueOf(numOfpartitions) + "\n"+"Fmeasure = " + fmeasure + "\n" +
                    "NFmeasure = " + nfmeasure + "\n" +
                    "Tmeasure = "+ tmeasure + "\n"+
                    "sdr_Fmeasure = " + sdrfmeasure + "\n" +
                    "sdr_NFmeasure = " + sdrnfmeasure + "\n" +
                    "adr_Tmaesure = " + sdrtmeasure + "\n"+
                    "Ftime："+String.valueOf(Ftime) + "\n"+
                    "F2time："+String.valueOf(F2time) + "\n"+
                    "Ttime："+String.valueOf(Ttime);
            fileWriter.write(cotent);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建文件并且初始话抬头
     * @param fileName 文件的名字
     */
    public void createFile(String fileName){
        String path = System.getProperty("user.dir")+ separator + "logs"+ separator + fileName;
        File file = new File(path);
        if (file.exists()){
            try{
                file.delete();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try{
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //向文件中写入抬头
        try{
            FileWriter fileWriter = new FileWriter(file);
            String cotent = "seed" + "\t\t\t" + "TC_ID" + "\t\t\t" + "partirion" +
                    "\t\t\t"+"killedMutants" + "\t\t\t" + "numOfUnkilledMutants";
            fileWriter.write(cotent);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
