package partition;

import testcases.Bean;

import java.util.Random;

/**
 * @author phantom
 */
public class Partition {

    public int nextPartition(int numOfpartitions){
        double[] pd = new double[numOfpartitions];
        for (int i = 0; i < pd.length; i++) {
            pd[i] = 1.0 / numOfpartitions;
        }
        Random random = new Random();
        int npi = -1 ;
        double rd = random.nextDouble();
        double sum = 0.0 ;
        do {
            ++npi;
            sum += pd[npi];
        }while(rd >= sum && npi < pd.length - 1);
        return npi ;
    }

    public boolean isBelongToOneOfPartition(Bean bean,int numOfPartition,int partition){
        int tov = bean.getTypeOfVehicle();
        int toc = bean.getTypeOfCar();
        int dow = bean.getDayOfWeek();
        double apd = bean.getActualParkDuration();
        boolean dc = bean.isDiscountCoupon();
        String est = bean.getEstimation();
        boolean flag = false;
        if (numOfPartition == 18){
            int temp = 0 ;
            if (!dc){
                if (!(est == null)){
                    temp = 2;
                }
            }else {
                temp = 1 ;
            }
            String[] par = {"002","001","000","012","011","010","1002","1001","1000","1012","1011","1010","1102","1101","1100","1112","1111","1110"};
            if(tov == 0){
                String str = String.valueOf(tov) + String.valueOf(dow) + String.valueOf(temp);
                if (str.equals(par[partition]))
                    flag = true;
            }else {
                String str = String.valueOf(tov) + String.valueOf(toc) + String.valueOf(dow) + String.valueOf(temp);
                if (str.equals(par[partition]))
                    flag = true;
            }
        }else {
            if (tov == 0){
                if (partition == 0)
                    flag = true;
            }else if (tov == 1){
                if (toc == 0){
                    if (partition == 1)
                        flag = true;
                }else {
                    if (partition == 2)
                        flag = true;
                }
            }
        }
        return flag;
    }

    public static void main(String[] args) {
        Partition p = new Partition();
        Bean bean1 = new Bean(0,0,1,0,2.3,false,"a");

        System.out.println(p.isBelongToOneOfPartition(bean1,3,0));

    }



}
