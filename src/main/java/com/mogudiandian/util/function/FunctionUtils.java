package com.mogudiandian.util.function;

import java.util.function.Function;

/**
 * 函数的工具类
 *
 * @author Joshua Sun
 * @since 1.0.5
 */
public final class FunctionUtils {

    private FunctionUtils() {}

    /**
     * 组合两个函数 y=f(u) u=g(x) 为复合函数 y=f'(x)
     * a和b组合 会先调用b 再拿b的结果调用a
     * @param fnY 函数1 也就是y的函数
     * @param fnU 函数2 也就是u的函数
     * @return 函数1和函数2组成的复合函数
     * @param <X> 函数2的自变量类型
     * @param <U> 函数2的值类型 也是函数1的自变量类型
     * @param <Y> 函数1的值类型 也是最终复合函数的值类型
     */
    public static <X, U, Y> Function<X, Y> compose(Function<U, Y> fnY, Function<X, U> fnU) {
        return fnY.compose(fnU);
    }

    /**
     * 组合三个函数 y=f(v) v=g(u) u=h(x) 为复合函数 y=f'(x)
     * @param fnY 函数1 也就是y的函数
     * @param fnV 函数2 也就是v的函数
     * @param fnU 函数3 也就是u的函数
     * @return 三个函数组成的复合函数
     * @param <X> 函数3的自变量类型
     * @param <U> 函数3的值类型 也是函数2的自变量类型
     * @param <V> 函数2的值类型 也是函数1的自变量类型
     * @param <Y> 函数1的值类型 也是最终复合函数的值类型
     */
    public static <X, U, V, Y> Function<X, Y> compose(Function<V, Y> fnY, Function<U, V> fnV, Function<X, U> fnU) {
        return fnY.compose(fnV).compose(fnU);
    }

}
