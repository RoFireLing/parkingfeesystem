package mutantSet;

import java.io.*;
import java.util.List;

import static java.io.File.separator;

/**
 * @author phantom
 */
public class MutantSet {
    private static final String MUTANT_PACKAGE = "com.lwf.ustb.www.FEE.mutant";
    private static final String MUTANT_PATH = "com"+separator+"lwf"+separator+"ustb"+separator+"www"+separator+"FEE"+
            separator+"mutant";
    private static final String CLASS_NAME = "ParkingFeeCalculator";
    private BinSet[] mutantsList;
    public MutantSet() {
        mutantsList = new BinSet[5];
        for (int i = 0; i < mutantsList.length; i++) {
            mutantsList[i] = new BinSet();
        }
        //向list中加入变异体
        String[] txt = {"M50-50.txt","M60-40.txt","M70-30.txt","M80-20.txt","M90-10.txt"};
        StringBuffer stringBuffer = new StringBuffer(0);
        for (int i = 0; i < txt.length; i++) {
            String path = System.getProperty("user.dir") + separator + "src" + separator + "mutantSet" + separator + txt[i];
            File file = new File(path);
            try{
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String str = "";
                while((str = bufferedReader.readLine()) != null){
                    stringBuffer.delete(0,stringBuffer.length());
                    stringBuffer.append(MUTANT_PACKAGE + ".");
                    stringBuffer.append(str + ".");
                    stringBuffer.append(CLASS_NAME);
                    mutantsList[i].add(new Mutant(stringBuffer.toString()));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 返回指定变异体分布的变异体集的个数
     * @param index 指定的变异体分布0-4
     * @return 返回变异体的个数
     */
    public int size(int index){return mutantsList[index].size();}

    public BinSet[] getMutantsList() {
        return mutantsList;
    }
}
