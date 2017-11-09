package mutantSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author phantom
 */
public class TestMethods {
    private List<String> methods;
    public TestMethods() {
        methods = new ArrayList<String>();
        methods.add("parkingFee");
    }

    /**
     * 返回要测试的方法列表
     * @return 待测的方法列表
     */
    public List<String> getMethods() { return methods; }
}
