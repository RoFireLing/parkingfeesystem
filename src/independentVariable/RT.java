package independentVariable;

import dependentVariable.RTMeasure;
import logrecorder.RTLog;
import mutantSet.BinSet;
import mutantSet.MutantSet;
import mutantSet.TestMethods;
import testcases.Bean;
import testcases.GenerateTestcases;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author phantom
 */
public class RT {
    private static final int SEEDS = 30 ;
    private static final int TESTTIMES = 30 ;
    private static final double DIVID = SEEDS * TESTTIMES ;
    private static final int NUMOFTESTCASES = 500000;
    private static final String ORIGINAL_PACKAGE = "com.lwf.ustb.www.FEE.";
    public void randomTesting(){
        GenerateTestcases generateTestcases = new GenerateTestcases();

        TestMethods testMethods = new TestMethods();
        List<String> methodsList = testMethods.getMethods();
        int[] partitions = {106,69,26,25,9};//记录每一个分区之中变异体的数量
//        int[] partitions = {9};
        String[] distribution = {"M50-50","M60-40","M70-30","M80-20","M90-10"};
//        String[] distribution = {"M90-10"};
        for (int y = 0; y < distribution.length; y++) {
            //记录每一个测试序列的测试结果
            RTMeasure rtMeasure = new RTMeasure();
            RTLog rtLog = new RTLog();
            long totaltime = 0;
            long start = System.currentTimeMillis();//开始测试的时间
            for (int i = 0; i < SEEDS; i++) {
                for (int r = 0; r < TESTTIMES; r++) {
                    //获得变异体集
                    MutantSet ms = new MutantSet();
                    BinSet[] mutants = new BinSet[5];
                    for (int j = 0; j < mutants.length; j++) {
                        mutants[j] = new BinSet();
                    }
                    mutants = ms.getMutantsList();
                    List<Bean> beans = new ArrayList<Bean>();
                    //产生测试用例
                    beans.clear();
                    beans = generateTestcases.generateTestcases(i,NUMOFTESTCASES);
                    //记录被杀死的变异体
                    List<String> killedMutants = new ArrayList<String>();
                    killedMutants.clear();
                    int counter = 0 ;

                    for (int j = 0; j < beans.size(); j++) {//每一个测试用例要在所有的变异体上执行
                        System.out.println("test begin:");
                        Bean bean = beans.get(j);//当前测试用例
                        counter++;
                        //记录临时某一个测试用例杀死的变异体情况
                        List<String> templist = new ArrayList<String>();
                        templist.clear();
                        try{
                            //开始逐个遍历变异体
                            for (int k = 0; k < mutants[y].size(); k++) {
                                //获取原始程序的实例
                                Class originalClazz = Class.forName(ORIGINAL_PACKAGE+"ParkingFeeCalculator");
                                Constructor constructor1 = originalClazz.getConstructor(null);
                                Object originalInstance = constructor1.newInstance(null);
                                //获取变异体程序的实例
                                Class mutantClazz = Class.forName(mutants[y].getMutantName(k));
                                Constructor constructor2 = mutantClazz.getConstructor(null);
                                Object mutantInstance = constructor2.newInstance(null);
                                //对一个变异体的所有方法进行遍历
                                for (int l = 0; l < methodsList.size(); l++) {
                                    //获取源程序的方法
                                    Method originalMethod = originalClazz.getMethod(methodsList.get(l),int.class,int.class,int.class,double.class,boolean.class,String.class);
                                    Object originalResult =  originalMethod.invoke(originalInstance,bean.getTypeOfVehicle(),bean.getTypeOfCar(),bean.getDayOfWeek(),bean.getActualParkDuration(),bean.isDiscountCoupon(),bean.getEstimation());

                                    Method mutantMethod = mutantClazz.getMethod(methodsList.get(l),int.class,int.class,int.class,double.class,boolean.class,String.class);
                                    Object mutantResult = mutantMethod.invoke(mutantInstance,bean.getTypeOfVehicle(),bean.getTypeOfCar(),bean.getDayOfWeek(),bean.getActualParkDuration(),bean.isDiscountCoupon(),bean.getEstimation());
                                    if (!originalResult.equals(mutantResult)){//揭示故障
//                                    System.out.println("original"+originalResult + "mutant"+mutantResult);
                                        String[] str = mutants[y].getMutantName(k).split("\\.");
                                        //删除杀死的变异体
                                        mutants[y].remove(k);
                                        k--;
                                        String temp = str[6];
                                        killedMutants.add(temp);
                                        templist.add(temp);
                                        if (killedMutants.size() == 1){
                                            rtMeasure.addFmeasure(counter);
                                        }else if (killedMutants.size() == partitions[y]){

                                            rtMeasure.addTmeasure(counter);
                                        }
                                        break;
                                    }
                                }
                            }
                            //记录1个测试用例在所有得变异体上执行之后的结果
//                            rtLog.recordProcessInfo("RT_log.txt",distribution[y],String.valueOf(i),String.valueOf(bean.getId()),
//                                    templist,String.valueOf(partitions[y] - killedMutants.size()));
                            if (killedMutants.size() >= partitions[y]){
                                break;
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            long end = System.currentTimeMillis();
            totaltime += (end - start);
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            double meanTime = Double.parseDouble(decimalFormat.format(totaltime / DIVID)) ;
            rtLog.recordResult("RTResult.txt",distribution[y],rtMeasure.getMeanFmeasure(),rtMeasure.getMeanTmeasure(),
                    rtMeasure.getStandardDevOfFmeasure(),rtMeasure.getStandardDevOfTmeasure(),meanTime);
        }

    }
    public static void main(String[] args) {
        RT rt = new RT();
        rt.randomTesting();
    }

}
