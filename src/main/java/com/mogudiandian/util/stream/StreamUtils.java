package com.mogudiandian.util.stream;

import java.util.Arrays;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 流的工具类
 * @author Joshua Sun
 */
public final class StreamUtils {

    private StreamUtils() {}

    /**
     * 将多个流进行笛卡尔积
     * @param aggregator 乘积函数
     * @param streams 多个流
     * @param <T> 流的元素类型
     * @return 计算后的流
     */
    public static <T> Stream<T> cartesianProduct(BinaryOperator<T> aggregator, Supplier<Stream<T>>... streams) {
        return Arrays.stream(streams)
                     .reduce((s1, s2) -> () -> s1.get().flatMap(t1 -> s2.get().map(t2 -> aggregator.apply(t1, t2))))
                     .orElse(Stream::empty)
                     .get();
    }

}
