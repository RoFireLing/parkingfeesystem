package testcases;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author phantom
 */
public class GenerateTestcases {

    /**
     * 产生测试用例集的主要方法
     * @param seed 产生测试用例的随机数
     * @param numOfTestcases
     * @return
     */
    public List<Bean> generateTestcases(int seed,int numOfTestcases){
        Random rand = new Random(seed);
        List<Bean> beans = new ArrayList<Bean>();//存放测试用例的列表
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        for (int i = 0; i < numOfTestcases; i++) {
            int tov = rand.nextInt(2);
            int toc = rand.nextInt(2);
            int dow = rand.nextInt(2);
            double apd = Double.parseDouble(decimalFormat.format(rand.nextDouble() * 24.0));
            boolean dc = rand.nextBoolean();
            String[] ests = {"(0, 2]", "(2, 4]", "(4, 24]"};
            String est = null;
            int x = rand.nextInt(4);
            if (!dc) {
                if (x == 3)
                    est = null ;
                else
                    est = ests[x];
            }
            beans.add(new Bean(i,tov,toc,dow,apd,dc,est));
        }
        return beans;
    }

}
