package testcases;

/**
 * @author phantom
 */
public class Bean {
    private int id;
    private int typeOfVehicle;// 车辆类型
    private int typeOfCar;// 小汽车种类
    private int dayOfWeek;// 日期类型（“工作日”或“周末”）
    private double actualParkDuration;// 实际停车时长
    private boolean discountCoupon;// 是否有打折券
    private String estimation;// 估计的停车时间
    public Bean(int i, int tov, int toc, int dow, double apd, boolean dc, String est){
        this.id = i;
        this.setTypeOfVehicle(tov);
        this.setTypeOfCar(toc);
        this.setDayOfWeek(dow);
        this.setActualParkDuration(apd);
        this.setDiscountCoupon(dc);
        this.setEstimation(est);
    }

    /**
     * 打印具体的测试用例
     * @return
     */
    public String toString() {
        StringBuffer rsb = new StringBuffer("第");
        rsb.append(id);
        rsb.append("个测试用例：车辆类型--");
        if (typeOfVehicle == com.lwf.ustb.www.FEE.util.Vehicle.CAR) {
            if (typeOfCar == com.lwf.ustb.www.FEE.util.Car.TWO_DOOR_COUPE) {
                rsb.append("两门跑车");
            } else if (typeOfCar == com.lwf.ustb.www.FEE.util.Car.OTHERS) {
                rsb.append("小轿车");
            } else {
                rsb.append(typeOfCar);
            }
        } else if (com.lwf.ustb.www.FEE.util.Vehicle.MOTORCYCLE == typeOfVehicle){
            rsb.append("摩托车");
        } else {
            rsb.append(typeOfVehicle);
        }
        rsb.append("；实际停车时间--");
        if (dayOfWeek == com.lwf.ustb.www.FEE.util.Day.WEEKDAY) {
            rsb.append("工作日");
        } else if (com.lwf.ustb.www.FEE.util.Day.WEEKEND == dayOfWeek){
            rsb.append("周末");
        } else {
            rsb.append(dayOfWeek);
        }
        rsb.append(actualParkDuration);
        rsb.append("小时。");
        if (discountCoupon) {
            rsb.append("有");
        } else {
            rsb.append("无");
        }
        rsb.append("打折券。");
        if (estimation != null && estimation.length() > 0) {
            rsb.append("预计停车时间 ");
            String[] hs = estimation.split(",");
            hs[0] = hs[0].trim();
            hs[0] = hs[0].substring(1);
            hs[1] = hs[1].trim();
            hs[1] = hs[1].substring(0, hs[1].length() - 1);
            rsb.append(hs[0]);
            rsb.append("小时到");
            rsb.append(hs[1]);
            rsb.append("小时。");
        }
        return rsb.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeOfVehicle() {
        return typeOfVehicle;
    }

    public void setTypeOfVehicle(int typeOfVehicle) {
        this.typeOfVehicle = typeOfVehicle;
    }

    public int getTypeOfCar() {
        return typeOfCar;
    }

    public void setTypeOfCar(int typeOfCar) {
        this.typeOfCar = typeOfCar;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public double getActualParkDuration() {
        return actualParkDuration;
    }

    public void setActualParkDuration(double actualParkDuration) {
        this.actualParkDuration = actualParkDuration;
    }

    public boolean isDiscountCoupon() {
        return discountCoupon;
    }

    public void setDiscountCoupon(boolean discountCoupon) {
        this.discountCoupon = discountCoupon;
    }

    public String getEstimation() {
        return estimation;
    }

    public void setEstimation(String estimation) {
        this.estimation = estimation;
    }
}
