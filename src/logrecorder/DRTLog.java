package logrecorder;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.io.File.separator;

/**
 * @author phantom
 */
public class DRTLog {
    public DRTLog() {}

    /**
     * 记录每一个测试用例的执行结果
     * @param filename log文件的名字
     * @param mutantDistribution 变异体的分布情况
     * @param seed seed
     * @param TCID 测试用例ID
     * @param killedmutants 杀死的变异体
     * @param numOfUnkillledMutants 剩下的变异体的数量
     */
    public void recordProcessInfo(String filename, String mutantDistribution, String seed, String partition,
                                  String TCID, List<String> killedmutants, String numOfUnkillledMutants,String parameters){
        String path = System.getProperty("user.dir") + separator + "logs" + separator + filename;
        File file = new File(path);
        if (!file.exists())
            createFile(path);
        //获取杀死的变异体
        String mutantskilled = "";
        for (int i = 0; i < killedmutants.size(); i++) {
            mutantskilled += killedmutants.get(i) + ",";
        }
        String content = "\n"+"\t"+mutantDistribution + "\t\t\t\t\t\t" +parameters+"\t\t\t\t"+ seed + "\t\t\t\t" + TCID + "\t\t\t\t"+partition
                +"\t\t\t\t\t\t"+numOfUnkillledMutants + "\t\t\t\t\t\t\t" + mutantskilled;
        try{//将content中得内容写入文件中
            FileWriter fileWriter = new FileWriter(path,true);
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
            String content =  "mutants distribution" + "\t\t\t" +"parameters"+"\t\t\t"+"seed" + "\t\t\t" + "TC_ID" + "\t\t\t"+
                    "partition"+"\t\t\t"+"numOfUnKilledMutants" + "\t\t\t" + "killedMutants";
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void recordResult(String filename,String fmesure,String tmeasure,String sdrfmeasure,String sdrtmeasure,
                             int numOfpartitions,double parametors,double time,String mutantDistribution){
        String path = System.getProperty("user.dir") + separator + "result" + separator + mutantDistribution+filename;

        try{
            File file = new File(path);
            WritableFont font = new WritableFont(WritableFont.ARIAL,14);//设置字体的样式以及大小
            WritableCellFormat wcf = new WritableCellFormat(font);//设置单元格的字体
            wcf.setBorder(Border.ALL, BorderLineStyle.THIN);//设置单元格的线条
            wcf.setVerticalAlignment(VerticalAlignment.CENTRE);//设置对齐格式
            wcf.setAlignment(Alignment.CENTRE);//设置对其格式
            wcf.setWrap(false);//设置文字是否换行
            //如果文件不存在则创建新的文件，新的文件中自动包含对应的表头
            if (!file.exists()){
                try{
                    file.createNewFile();
                    //向新建的文件中添加表头
                    WritableWorkbook writableWorkbook1 = Workbook.createWorkbook(file);
                    WritableSheet sheet = writableWorkbook1.createSheet("sheet",0);
                    String[] elements = {"MuDistribution","partitions","parameters","Fmeasure","sdr_Fmeasure","Tmeasure","sdr_Tmeasure","time"};
                    for (int i = 0; i < elements.length; i++) {
                        sheet.addCell(new Label(i,0,elements[i],wcf));
                        sheet.setColumnView(i,22);
                    }
                    writableWorkbook1.write();
                    writableWorkbook1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (RowsExceededException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
            //向文件中写入内容
            Workbook originalWorkbook = Workbook.getWorkbook(file);
            //在原来的基础上写入内容
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(file,originalWorkbook);
            //获得原始表格中的sheet
            WritableSheet sheet = writableWorkbook.getSheet(0);
            //获得之前sheet中写入的行数
            int temp = sheet.getRows();
            String[] elements = {mutantDistribution,String.valueOf(numOfpartitions),String.valueOf(parametors),fmesure,
            sdrfmeasure,tmeasure,sdrtmeasure,String.valueOf(time)};
            for (int i = 0; i < elements.length; i++) {
                sheet.addCell(new Label(i,temp,elements[i],wcf));
            }
            //写入内容之后关闭工作簿
            originalWorkbook.close();
            writableWorkbook.write();
            writableWorkbook.close();
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

    private void createXLS(String path,WritableCellFormat wcf){
        File file = new File(path);
        try{
            file.createNewFile();
            //向新建的文件中添加表头
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(file);
            WritableSheet sheet = writableWorkbook.createSheet("sheet",0);
            String[] elements = {"MuDistribution","partitions","parameters","Fmeasure","sdr_Fmeasure","Tmeasure","sdr_Tmeasure","time"};
            for (int i = 0; i < elements.length; i++) {
                sheet.addCell(new Label(i,0,elements[i],wcf));
                sheet.setColumnView(i,22);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        DRTLog drtLog = new DRTLog();
        List<String> lists = new ArrayList<String>();
        lists.add("roa1");
        drtLog.recordProcessInfo("test","M50-50","1","1","1",lists,"10","0.001");
    }

}
