package independentVariable;

import dependentVariable.DRTMeasure;
import logrecorder.DRTLog;
import mutantSet.BinSet;
import mutantSet.MutantSet;
import mutantSet.TestMethods;
import partition.Partition;
import testcases.Bean;
import testcases.GenerateTestcases;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author phantom
 */
public class DRT {
    private static final int SEEDS = 30 ;
    private static final int TESTTIMES = 30 ;
    private static final double DIVISOR = SEEDS * TESTTIMES;
    private static final String ORIGINAL_PACKAGE = "com.lwf.ustb.www.FEE.";
    private static final int NUMOFTESTCASES = 500000;


    Random r = new Random();
    private double[] pd;
    private double epsilon;
    /**
     * 获取第一个分区的ID
     * @return 返回第一个分区的ID
     */
    private int nextPartition(){
        int npi;
        double rd = r.nextDouble();
        double sum = 0.0;
        npi = -1;
        do {
            ++npi;
            sum += pd[npi];
        } while (rd >= sum && npi < pd.length);
        return npi;
    }

    /**
     * 调整概率分布
     * @param prePartitionIndex 当前分区的编号
     * @param isKillMutant 当前分区是否杀死了变异体
     */
    private void adjustPartition(int prePartitionIndex,boolean isKillMutant){
        if (isKillMutant) {// 上次测试用例杀死了变异体
            double sum = 0;
            for (int i = 0; i < pd.length; ++i) {// 减小其他分区被选中的概率
                if (i != prePartitionIndex) {
                    pd[i] -= epsilon / (pd.length - 1);
                    if (pd[i] < 0)
                        pd[i] = 0;
                    sum += pd[i];
                }
            }
            pd[prePartitionIndex] = 1 - sum;// 增大上次选中分区被选中的概率
        } else {// 上次测试用例未杀死变异体
            if (pd[prePartitionIndex] < epsilon) {
                for (int i = 0; i < pd.length; ++i) {
                    if (i != prePartitionIndex) {
                        pd[i] += pd[prePartitionIndex] / (pd.length - 1);
                    }
                }
                pd[prePartitionIndex] = 0;
            } else {
                for (int i = 0; i < pd.length; ++i) {
                    if (i != prePartitionIndex) {
                        pd[i] += epsilon / (pd.length - 1);
                    }
                }
                pd[prePartitionIndex] -= epsilon;
            }
        }
    }


    public void dynamicRandomTesting(){
        GenerateTestcases generateTestcases = new GenerateTestcases();
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        TestMethods testMethods = new TestMethods();
        List<String> methodsList = testMethods.getMethods();
//        int[] partitions = {106,69,26,25,9};//记录每一个分区之中变异体的数量
        int[] partitions = {4};//记录每一个分区之中变异体的数量
//        String[] distribution = {"M50-50","M60-40","M70-30","M80-20","M90-10"};
        String[] distribution = {"LowFailureRate"};
        int[] numOfPartitions = {18,3};
//        int[] numOfPartitions = {3};
//        int[] numOfPartitions = {18};
        Partition rptPartition = new Partition();
        double[] parameters = {0.00001,0.00005,0.0001,0.0005,0.001,0.005,0.01,0.05,0.1,0.2,0.3,0.4,0.5};
//        double[] parameters = {0.05,0.1,0.2};
        DRTLog drtLog = new DRTLog();
        for (int y = 0; y < distribution.length; y++) {//对不同的变异体集进行测试
            for (int i = 0; i < numOfPartitions.length; i++) {
                for (int x = 0; x < parameters.length; x++) {
                    epsilon = parameters[x];
                    DRTMeasure drtMeasure = new DRTMeasure();

                    List<Long> falltime = new ArrayList<>();
                    List<Long> f2alltime = new ArrayList<>();
                    List<Long> talltime = new ArrayList<>();

                    for (int j = 0; j < SEEDS; j++) {
                        for (int k = 0; k < TESTTIMES; k++) {
                            int counter = 0 ;
                            int fmeasure = 0 ;
                            List<Bean> beans = new ArrayList<Bean>();
                            beans.clear();
                            beans = generateTestcases.generateTestcases(j,NUMOFTESTCASES);//获得指定随机数种子情况下的测试用例集
                            List<String> killedMutants = new ArrayList<String>();//记录杀死的变异体
                            killedMutants.clear();
                            MutantSet ms = new MutantSet();
                            BinSet[] mutants = new BinSet[5];
                            for (int l = 0; l < mutants.length; l++) {
                                mutants[l] = new BinSet();
                            }
                            mutants = ms.getMutantsList();//获得5中变异体分布的变异体集
                            pd = new double[numOfPartitions[i]];
                            for (int l = 0; l < pd.length; l++) {
                                pd[l] = 1.0 / numOfPartitions[i];
                            }

                            long starttemp = System.currentTimeMillis();//开始测试的时间
                            long ftime = 0;

                            for (int l = 0; l < beans.size();) {//选取测试用例
                                int partition = nextPartition();
                                Bean bean;
                                //记录临时某一个测试用例杀死的变异体情况
                                List<String> templist = new ArrayList<String>();
                                templist.clear();
                                //获取属于partition中的tc
                                do {
                                    bean = beans.get(l++);
                                }while(!rptPartition.isBelongToOneOfPartition(bean,numOfPartitions[i],partition));
                                //将测试用例初始化
                                System.out.println("test begin:");
                                counter++;//测试用例的计数器

                                try{
                                    for (int m = 0; m < mutants[y].size(); m++) {
                                        //获取原始程序的实例
                                        Class originalClazz = Class.forName(ORIGINAL_PACKAGE+"ParkingFeeCalculator");
                                        Constructor constructor1 = originalClazz.getConstructor(null);
                                        Object originalInstance = constructor1.newInstance(null);
                                        //获取变异体程序的实例
                                        Class mutantClazz = Class.forName(mutants[y].getMutantName(m));
                                        Constructor constructor2 = mutantClazz.getConstructor(null);
                                        Object mutantInstance = constructor2.newInstance(null);
                                        //对一个变异体的所有方法进行遍历
                                        for (int n = 0; n < methodsList.size(); n++) {
                                            //获取源程序的方法
                                            Method originalMethod = originalClazz.getMethod(methodsList.get(n),int.class,int.class,int.class,double.class,boolean.class,String.class);
                                            Object originalResult =  originalMethod.invoke(originalInstance,bean.getTypeOfVehicle(),bean.getTypeOfCar(),bean.getDayOfWeek(),bean.getActualParkDuration(),bean.isDiscountCoupon(),bean.getEstimation());

                                            Method mutantMethod = mutantClazz.getMethod(methodsList.get(n),int.class,int.class,int.class,double.class,boolean.class,String.class);
                                            Object mutantResult = mutantMethod.invoke(mutantInstance,bean.getTypeOfVehicle(),bean.getTypeOfCar(),bean.getDayOfWeek(),bean.getActualParkDuration(),bean.isDiscountCoupon(),bean.getEstimation());
                                            if (!originalResult.equals(mutantResult)){
                                                String[] str = mutants[y].getMutantName(m).split("\\.");
                                                //删除杀死的变异体
                                                mutants[y].remove(m);
                                                m--;
                                                String temp = str[6];
                                                killedMutants.add(temp);
                                                templist.add(temp);
                                                if (killedMutants.size() == 1){
                                                    long ftimeTemp = System.currentTimeMillis();
                                                    ftime = ftimeTemp;
                                                    falltime.add(ftimeTemp - starttemp);
                                                    fmeasure = counter;
                                                    drtMeasure.addFmeasure(counter);
                                                }else if (killedMutants.size() == partitions[y]){
                                                    long ttimeTemp = System.currentTimeMillis();
                                                    talltime.add(ttimeTemp - starttemp);
                                                    drtMeasure.addTmeasure(counter);
                                                }else if (killedMutants.size() == 2){
                                                    long f2timeTemp = System.currentTimeMillis();
                                                    f2alltime.add(f2timeTemp - ftime);
                                                    drtMeasure.addNFmeasure(counter - fmeasure);
                                                }
                                                break;
                                            }
                                        }
                                    }
                                    //记录1个测试用例在所有得变异体上执行之后的结果
//                                    drtLog.recordProcessInfo("drt_log.txt",distribution[y],String.valueOf(j),
//                                            String.valueOf(partition),String.valueOf(bean.getId()),templist,
//                                            String.valueOf(partitions[y] - killedMutants.size()),
//                                            String.valueOf(parameters[x]));
                                    if (killedMutants.size() >= partitions[y]){
                                        break;
                                    }
                                } catch (InstantiationException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    long ftotaltime = 0;
                    for (int b = 0; b < falltime.size(); b++) {
                        ftotaltime += falltime.get(b);
                    }
                    double meanftime = Double.parseDouble(decimalFormat.format(ftotaltime / DIVISOR));

                    long f2totaltime = 0;
                    for (int b = 0; b < f2alltime.size(); b++) {
                        f2totaltime += f2alltime.get(b);
                    }
                    double meanf2time = Double.parseDouble(decimalFormat.format(f2totaltime / DIVISOR));

                    long ttotaltime = 0;
                    for (int b = 0; b < talltime.size(); b++) {
                        ttotaltime += talltime.get(b);
                    }
                    double meantime = Double.parseDouble(decimalFormat.format(ttotaltime / DIVISOR));
                    drtLog.recordResult("drtResult.xls",drtMeasure.getMeanFmeasure(),drtMeasure.getMeanNFmeasure(),
                            drtMeasure.getMeanTmeasure(),drtMeasure.getStandardDevOfFmeasure(),drtMeasure.getStandardDevOfNFmeasure(),
                            drtMeasure.getStandardDevOfTmeasure(),numOfPartitions[i],
                            parameters[x],distribution[y],meanftime,meanf2time,meantime);
                }
            }
        }
    }

    public static void main(String[] args) {
        DRT drt = new DRT();
        drt.dynamicRandomTesting();
    }


}
