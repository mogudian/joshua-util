package lang;

import com.mogudiandian.util.string.SplitUtils;

/**
 * 测试字符串分割工具类
 *
 * @author sunbo
 * @since 2023/9/4
 */
public class TestSplitUtils {

    public static void main(String[] args) {
        String str = "test1234,567.345-9842.5326-.345.5.-42-555.--6";
        System.out.println(SplitUtils.splitToIntegerList(str));
        System.out.println(SplitUtils.splitToLongList(str));
        System.out.println(SplitUtils.splitToDoubleList(str));
    }

}

