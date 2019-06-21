package independentVariable;

import dependentVariable.DRTMeasure;
import logrecorder.ATLog;
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
 * describe:
 * this class implement the algorithem of AT that is proposed in the
 * paper "Optimal and adaptive testing for software reliability assessment"
 * @author phantom
 * @date 2019/06/18
 */
public class AT {
    private int[] ksi;

    //record the number of selected test cases in partition s_i
    private int[] eta;

    //record the detected failures by partition s_i
    private int[] Y;

    // 最初需要从每个分区中选择一个测试用例并执行，初始值为2
    private int d;

    // 每个分区对应的被选择的概率
    private double[] p;

    /**
     * initialize the above parameters
     * @param numberOfPartitions the number of partitions
     */
    private void initializationParameters(int numberOfPartitions){
        int tempNumber = numberOfPartitions * 2 + 1;
        ksi = new int[tempNumber];
        ksi[0] = NUMOFTESTCASES;
        eta = new int[numberOfPartitions];
        Y = new int[numberOfPartitions];
        d = 2;
        p = new double[numberOfPartitions];
        for (int i = 0; i < numberOfPartitions; i++) {
            p[i] = 1.0 / numberOfPartitions ;
        }
    }

    public void adaptiveTesting(){
        GenerateTestcases generateTestcases = new GenerateTestcases();
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        TestMethods testMethods = new TestMethods();
        List<String> methodsList = testMethods.getMethods();
        int[] partitions = {11};
        String[] distribution = {"LowFailureRate"};

        int[] numOfpartition = {18};
        Partition rptPartition = new Partition();
        for (int y = 0; y < distribution.length; y++) {//对不同的变异体集进行测试
            for (int i = 0; i < numOfpartition.length; i++) {
                ATLog atLog = new ATLog("AT_log.txt");
                List<Long> falltime = new ArrayList<Long>();
                List<Long> f2alltime = new ArrayList<Long>();
                List<Long> talltime = new ArrayList<Long>();
                DRTMeasure drtMeasure = new DRTMeasure();
                conPartition = numOfpartition[i];
                for (int k = 0; k < SEEDS; k++) {
                    for (int l = 0; l < TESTTIMES; l++) {
                        System.out.println("test begin: 随机数种子为" +
                                String.valueOf(k) + ";重复的实验次数为：" + String.valueOf(l));
                        int counter = 0 ;
                        int fmeasure = 0 ;
                        List<Bean> beans = new ArrayList<Bean>();
                        beans.clear();
                        beans = generateTestcases.generateTestcases(k,NUMOFTESTCASES);//获得指定随机数种子情况下的测试用例集
                        List<String> killedMutants = new ArrayList<String>();//记录杀死的变异体
                        killedMutants.clear();
                        MutantSet ms = new MutantSet();
                        BinSet[] mutants = new BinSet[5];
                        for (int a = 0; a < mutants.length; a++) {
                            mutants[a] = new BinSet();
                        }
                        mutants = ms.getMutantsList();//获得5中变异体分布的变异体集
                        long starttemp = System.currentTimeMillis();
                        long ftime = 0;
                        //初始化参数
                        initializationParameters(numOfpartition[i]);

                        for (int m = 0; m < beans.size();) {//选取测试用例
                            Bean bean = null;
                            int partitionIndex = 0;
                            //记录临时某一个测试用例杀死的变异体情况
                            List<String> templist = new ArrayList<String>();
                            if (d > 0) {
                                for (int j = 0; j < numOfpartition[i]; j++) {
                                    partitionIndex = j;
                                    templist.clear();
                                    //获取属于partition中的tc
                                    do {
                                        bean = beans.get(m++);
                                    } while (!rptPartition.isBelongToOneOfPartition(bean, numOfpartition[i], j));
                                    //将测试用例初始
                                    counter++;//测试的测试用例数目自增
                                    try{
                                        for (int n = 0; n < mutants[y].size(); n++) {
                                            //获取原始程序的实例
                                            Class originalClazz = Class.forName(ORIGINAL_PACKAGE+"ParkingFeeCalculator");
                                            Constructor constructor1 = originalClazz.getConstructor(null);
                                            Object originalInstance = constructor1.newInstance(null);
                                            //获取变异体程序的实例
                                            Class mutantClazz = Class.forName(mutants[y].getMutantName(n));
                                            Constructor constructor2 = mutantClazz.getConstructor(null);
                                            Object mutantInstance = constructor2.newInstance(null);
                                            //对一个变异体的所有方法进行遍历
                                            for (int o = 0; o < methodsList.size(); o++) {
                                                //获取源程序的方法
                                                Method originalMethod = originalClazz.getMethod(methodsList.get(o),int.class,int.class,int.class,double.class,boolean.class,String.class);
                                                Object originalResult =  originalMethod.invoke(originalInstance,bean.getTypeOfVehicle(),bean.getTypeOfCar(),bean.getDayOfWeek(),bean.getActualParkDuration(),bean.isDiscountCoupon(),bean.getEstimation());

                                                Method mutantMethod = mutantClazz.getMethod(methodsList.get(o),int.class,int.class,int.class,double.class,boolean.class,String.class);
                                                Object mutantResult = mutantMethod.invoke(mutantInstance,bean.getTypeOfVehicle(),bean.getTypeOfCar(),bean.getDayOfWeek(),bean.getActualParkDuration(),bean.isDiscountCoupon(),bean.getEstimation());

                                                if (!originalResult.equals(mutantResult)){
                                                    String[] str = mutants[y].getMutantName(n).split("\\.");
                                                    //删除杀死的变异体
                                                    mutants[y].remove(n);
                                                    n--;
                                                    String temp = str[6];
                                                    killedMutants.add(temp);
                                                    templist.add(temp);
                                                    if (killedMutants.size() == 1){
                                                        long ftimeTemp = System.currentTimeMillis();
                                                        ftime = ftimeTemp;
                                                        falltime.add(ftimeTemp - starttemp);
                                                        fmeasure = counter;
                                                        drtMeasure.addFmeasure(counter);
                                                    }else if (killedMutants.size() == NUMOFMUTANTS){
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
                                        if (templist.size() != 0){//表示改测试用例揭示软件中的故障
                                            ksi[0] -= 1;
                                            ksi[j * 2 + 1] += 1;
                                            ksi[j * 2 + 2] += 1;
                                            eta[j] += 1;
                                            Y[j] += 1;
                                        }else {//表示改测试用例没有揭示软件中的故障调整概率分布
                                            ksi[0] -= 1;
                                            ksi[j * 2 + 1] += 1;
                                            ksi[j * 2 + 2] += 0;
                                            eta[j] += 1;
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
                                    atLog.recordProcessInfo(String.valueOf(k), String.valueOf(bean.getId()), String.valueOf(partitionIndex),
                                            templist, String.valueOf(NUMOFMUTANTS - killedMutants.size()));
                                }
                                d--;
                            }else {
                                valueFunction(the, ksi);
                                int tempvalue = index;
                                do {
                                    bean = beans.get(m++);
                                }while(!rptPartition.isBelongToOneOfPartition(bean,numOfpartition[i],index));
                                templist.clear();
                                //将测试用例初始化
                                counter++;//测试的测试用例数目自增
                                try{
                                    for (int n = 0; n < mutants[y].size(); n++) {
                                        //获取原始程序的实例
                                        Class originalClazz = Class.forName(ORIGINAL_PACKAGE+"ParkingFeeCalculator");
                                        Constructor constructor1 = originalClazz.getConstructor(null);
                                        Object originalInstance = constructor1.newInstance(null);
                                        //获取变异体程序的实例
                                        Class mutantClazz = Class.forName(mutants[y].getMutantName(n));
                                        Constructor constructor2 = mutantClazz.getConstructor(null);
                                        Object mutantInstance = constructor2.newInstance(null);
                                        //对一个变异体的所有方法进行遍历
                                        for (int o = 0; o < methodsList.size(); o++) {
                                            //获取源程序的方法
                                            Method originalMethod = originalClazz.getMethod(methodsList.get(o),int.class,int.class,int.class,double.class,boolean.class,String.class);
                                            Object originalResult =  originalMethod.invoke(originalInstance,bean.getTypeOfVehicle(),bean.getTypeOfCar(),bean.getDayOfWeek(),bean.getActualParkDuration(),bean.isDiscountCoupon(),bean.getEstimation());

                                            Method mutantMethod = mutantClazz.getMethod(methodsList.get(o),int.class,int.class,int.class,double.class,boolean.class,String.class);
                                            Object mutantResult = mutantMethod.invoke(mutantInstance,bean.getTypeOfVehicle(),bean.getTypeOfCar(),bean.getDayOfWeek(),bean.getActualParkDuration(),bean.isDiscountCoupon(),bean.getEstimation());
                                            if (!originalResult.equals(mutantResult)){
                                                String[] str = mutants[y].getMutantName(n).split("\\.");
                                                //删除杀死的变异体
                                                mutants[y].remove(n);
                                                n--;
                                                String temp = str[6];
                                                killedMutants.add(temp);
                                                templist.add(temp);
                                                if (killedMutants.size() == 1){
                                                    long ftimeTemp = System.currentTimeMillis();
                                                    ftime = ftimeTemp;
                                                    falltime.add(ftimeTemp - starttemp);
                                                    fmeasure = counter;
                                                    drtMeasure.addFmeasure(counter);
                                                }else if (killedMutants.size() == NUMOFMUTANTS){
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
                                    ksi[index * 2 + 1] += 1;
                                    eta[index] += 1;
                                    if (templist.size() != 0){//表示改测试用例揭示软件中的故障
                                        ksi[index * 2 + 2] += 1;
                                        Y[index] += 1;
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
                                atLog.recordProcessInfo(String.valueOf(k), String.valueOf(bean.getId()), String.valueOf(index),
                                        templist, String.valueOf(NUMOFMUTANTS - killedMutants.size()));
                            }
                            if (killedMutants.size() >= NUMOFMUTANTS){
                                break;
                            }
                        }
                    }
                }
                //计算平均时间
                long ftotaltime = 0;
                for (int k = 0; k < falltime.size(); k++) {
                    ftotaltime += falltime.get(k);
                }
                double meanfTime = Double.parseDouble(decimalFormat.format(ftotaltime / DIVID)) ;

                long f2totaltime = 0;
                for (int k = 0; k < f2alltime.size(); k++) {
                    f2totaltime += f2alltime.get(k);
                }
                double meanf2Time = Double.parseDouble(decimalFormat.format(f2totaltime / DIVID)) ;

                long ttotaltime = 0;

                for (int k = 0; k < talltime.size(); k++) {
                    ttotaltime += talltime.get(k);
                }
                double meantime = Double.parseDouble(decimalFormat.format(ttotaltime / DIVID)) ;
                String filename = "ATResult_"+ String.valueOf(numOfpartition[i]) + ".txt";
                atLog.recordResult(filename,String.valueOf(drtMeasure.getMeanFmeasure()),String.valueOf(drtMeasure.getMeanNFmeasure()),
                        String.valueOf(drtMeasure.getMeanTmeasure()),String.valueOf(drtMeasure.getStandardDevOfFmeasure()),String.valueOf(drtMeasure.getStandardDevOfNFmeasure()),
                        String.valueOf(drtMeasure.getStandardDevOfTmeasure()),numOfpartition[i], meanfTime,meanf2Time,meantime);
            }
        }
    }

    /**
     * 根据当前的eta计算价值函数的值
     * @return 当前最优的活动
     */
    public double valueFunction(int threshhold, int[] ksi1){
        double result = 0;


        double[] theta = new double[p.length];
        for (int i = 0; i < theta.length; i++) {
            theta[i] = (double)ksi1[i * 2 + 2] / ksi1[i * 2 + 1];
        }
        if (threshhold == 0){
            double sum = 0;
            for (int i = 0; i < p.length; i++) {
                double numerator = (double)((p[i] * p[i] * ksi1[i * 2 + 2] *
                        (ksi1[i * 2 + 1] - ksi1[i * 2 + 2])));
                double denominator = (double) (((ksi1[i * 2 + 1] - 1) *
                        ksi1[i * 2 + 1] * ksi1[i * 2 + 1]));
                sum += numerator / denominator;
            }
            result = sum;
        }else{
            //存储计算结果
            double[] values = new double[p.length * 2];

            for (int j = 0; j < p.length * 2; j++) {
                if (j == 0){
                    int[] tempEta = ksi1;
                    tempEta[j + 1] += 1;
                    values[j] = valueFunction(threshhold - 1, tempEta);
                    ksi1[j + 1] -= 1;
                }else if (j == 1){
                    int[] tempEta = ksi1;
                    tempEta[j] += 1;
                    tempEta[j + 1] += 1;
                    values[j] = valueFunction(threshhold - 1, tempEta);
                    ksi1[j] -= 1;
                    ksi1[j + 1] -= 1;
                }else if (j == 2){
                    int[] tempEta = ksi1;
                    tempEta[j + 1] += 1;
                    values[j] = valueFunction(threshhold - 1, tempEta);
                    ksi1[j + 1] -= 1;
                }else if (j == 3){
                    int[] tempEta = ksi1;
                    tempEta[j] += 1;
                    tempEta[j + 1] += 1;
                    values[j] = valueFunction(threshhold - 1, tempEta);
                    ksi1[j] -= 1;
                    ksi1[j + 1] -= 1;
                }else {
                    if (j % 2 == 0){
                        int[] tempEta = ksi1;
                        tempEta[j + 1] += 1;
                        values[j] = valueFunction(threshhold - 1, tempEta);
                        ksi1[j + 1] -= 1;
                    }else {
                        int[] tempEta = ksi1;
                        tempEta[j] += 1;
                        tempEta[j + 1] += 1;
                        values[j] = valueFunction(threshhold - 1, tempEta);
                        ksi1[j] -= 1;
                        ksi1[j + 1] -= 1;
                    }
                }
            }
            double[] partitionValues = new double[p.length];
            for (int i = 0; i < partitionValues.length; i++) {
                partitionValues[i] = (1-theta[i]) * values[i * 2] +
                                        theta[i] * values[i * 2 + 1];
            }
            result = partitionValues[0];
            for (int i = 1; i < partitionValues.length; i++) {
                if (partitionValues[i] < result){
                    result = partitionValues[i];
                    if (threshhold == the){
                        index = new Random().nextInt(conPartition);
                    }
                }
            }
        }
        return result;
    }

    private static final int TESTTIMES = 1 ;
    private static final int SEEDS = 20 ;
    private static final double DIVID = TESTTIMES * SEEDS ;
    private static final int NUMOFTESTCASES = 900000;
    private static final String ORIGINAL_PACKAGE = "com.lwf.ustb.www.FEE.";
    private static final int NUMOFMUTANTS = 4 ;
    private static int the = 3;
    private static int index = 0;
    private int conPartition = 0;

    public static void main(String[] args) {
        AT at = new AT();
        at.adaptiveTesting();
    }

}
