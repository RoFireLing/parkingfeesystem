package failureRate.partition;

import java.util.ArrayList;
import java.util.List;

public class RecordCount {
    private static List<Integer> list ;

    public RecordCount() {
        list = new ArrayList<>();
    }

    /**
     * 返回列表中的数据的个数
     * @return
     */
    public int size(){return list.size();}

    /**
     * 删除列表中的数据
     * @param index
     */
    public void remove(int index){ list.remove(index);}

    /**
     * 向列表中添加元素
     * @param item
     */
    public void add(int item){list.add(item);}


    public void removeAll(){list.clear();}

    public double getMean(){
        double result = 0.0 ;
        for (int i = 0; i < list.size(); i++) {
            result += list.get(i);
        }
        return result / list.size();
    }



    public static List<Integer> getList() {
        return list;
    }

    public static void setList(List<Integer> list) {
        RecordCount.list = list;
    }
}
