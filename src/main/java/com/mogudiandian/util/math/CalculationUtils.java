package com.mogudiandian.util.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

/**
 * 计算工具类
 *
 * @author Joshua Sun
 */
public final class CalculationUtils {

    private CalculationUtils() {}

    /**
     * 计算 其中null元素不参与计算
     * @param stream BigDecimal组成的流
     * @param reduceFn 归纳函数
     * @return 计算后的值 默认为0
     */
    private static BigDecimal calculate(Stream<BigDecimal> stream, BinaryOperator<BigDecimal> reduceFn) {
        return stream.filter(Objects::nonNull)
                     .reduce(reduceFn)
                     .orElse(BigDecimal.ZERO);
    }

    /**
     * 可变数组转换成流
     * @param nx 可变数组
     * @return 流
     */
    private static Stream<BigDecimal> variableArrayToStream(BigDecimal... nx) {
        return Optional.ofNullable(nx).map(Arrays::stream).orElse(Stream.empty());
    }

    /**
     * 获取流
     * @param n1 操作数1
     * @param n2 操作数2
     * @param nx 更多操作数
     * @return 流包含 n1, n2, ..., nx
     */
    private static Stream<BigDecimal> getStream(BigDecimal n1, BigDecimal n2, BigDecimal... nx) {
        return Stream.concat(Stream.of(n1, n2), variableArrayToStream(nx));
    }

    /**
     * 列表转换成流
     * @param nx 可变数组
     * @return 流
     */
    private static Stream<BigDecimal> listToStream(List<BigDecimal> nx) {
        return Optional.ofNullable(nx).map(Collection::stream).orElse(Stream.empty());
    }

    /**
     * 加法
     * @param n1 操作数1
     * @param n2 操作数2
     * @param nx 更多操作数
     * @return 和值 默认为0
     */
    public static BigDecimal add(BigDecimal n1, BigDecimal n2, BigDecimal... nx) {
        return calculate(getStream(n1, n2, nx), BigDecimal::add);
    }

    /**
     * 加法
     * @param ns 操作数列表
     * @return 和值 默认为0
     */
    public static BigDecimal add(BigDecimal... ns) {
        return calculate(variableArrayToStream(ns), BigDecimal::add);
    }

    /**
     * 加法
     * @param ns 操作数列表
     * @return 和值 默认为0
     */
    public static BigDecimal add(List<BigDecimal> ns) {
        return calculate(listToStream(ns), BigDecimal::add);
    }

    /**
     * 减法
     * @param n1 操作数1
     * @param n2 操作数2
     * @param nx 更多操作数
     * @return 差值 默认为0
     */
    public static BigDecimal subtract(BigDecimal n1, BigDecimal n2, BigDecimal... nx) {
        return calculate(getStream(n1, n2, nx), BigDecimal::subtract);
    }

    /**
     * 减法
     * @param ns 操作数列表
     * @return 差值 默认为0
     */
    public static BigDecimal subtract(BigDecimal... ns) {
        return calculate(variableArrayToStream(ns), BigDecimal::subtract);
    }

    /**
     * 减法
     * @param ns 操作数列表
     * @return 差值 默认为0
     */
    public static BigDecimal subtract(List<BigDecimal> ns) {
        return calculate(listToStream(ns), BigDecimal::subtract);
    }

    /**
     * 乘法
     * @param n1 操作数1
     * @param n2 操作数2
     * @param nx 更多操作数
     * @return 乘积 默认为0
     */
    public static BigDecimal multiple(BigDecimal n1, BigDecimal n2, BigDecimal... nx) {
        return calculate(getStream(n1, n2, nx), BigDecimal::multiply);
    }

    /**
     * 乘法
     * @param ns 操作数列表
     * @return 乘积 默认为0
     */
    public static BigDecimal multiple(BigDecimal... ns) {
        return calculate(variableArrayToStream(ns), BigDecimal::multiply);
    }

    /**
     * 乘法
     * @param ns 操作数列表
     * @return 乘积 默认为0
     */
    public static BigDecimal multiple(List<BigDecimal> ns) {
        return calculate(listToStream(ns), BigDecimal::multiply);
    }

    /**
     * 安全除法
     * @param scale 保留几位小数
     * @param roundingMode 进位模式
     * @return 商值 如果除数为0则值为0
     */
    private static BinaryOperator<BigDecimal> safeDivideFn(int scale, RoundingMode roundingMode) {
        return (x, y) -> y.compareTo(BigDecimal.ZERO) != 0 ? x.divide(y, scale, roundingMode) : BigDecimal.ZERO;
    }

    /**
     * 除法
     * @param scale 保留几位小数
     * @param roundingMode 进位模式
     * @param n1 操作数1
     * @param n2 操作数2
     * @param nx 更多操作数
     * @return 商值 默认为0 如果除数为0则值为0
     */
    public static BigDecimal divide(int scale, RoundingMode roundingMode, BigDecimal n1, BigDecimal n2, BigDecimal... nx) {
        return calculate(getStream(n1, n2, nx), safeDivideFn(scale, roundingMode));
    }

    /**
     * 除法
     * @param scale 保留几位小数
     * @param roundingMode 进位模式
     * @param ns 操作数列表
     * @return 商值 默认为0
     */
    public static BigDecimal divide(int scale, RoundingMode roundingMode, BigDecimal... ns) {
        return calculate(variableArrayToStream(ns), safeDivideFn(scale, roundingMode));
    }

    /**
     * 除法
     * @param scale 保留几位小数
     * @param roundingMode 进位模式
     * @param ns 操作数列表
     * @return 商值 默认为0
     */
    public static BigDecimal divide(int scale, RoundingMode roundingMode, List<BigDecimal> ns) {
        return calculate(listToStream(ns), safeDivideFn(scale, roundingMode));
    }

    /**
     * 除法 默认四舍五入保留2位小数
     * @param n1 操作数1
     * @param n2 操作数2
     * @param nx 更多操作数
     * @return 商值 默认为0 如果除数为0则值为0
     */
    public static BigDecimal divide(BigDecimal n1, BigDecimal n2, BigDecimal... nx) {
        return divide(2, RoundingMode.HALF_UP, n1, n2, nx);
    }

    /**
     * 除法 默认四舍五入保留2位小数
     * @param ns 操作数列表
     * @return 商值 默认为0
     */
    public static BigDecimal divide(BigDecimal... ns) {
        return divide(2, RoundingMode.HALF_UP, ns);
    }

    /**
     * 除法 默认四舍五入保留2位小数
     * @param ns 操作数列表
     * @return 商值 默认为0
     */
    public static BigDecimal divide(List<BigDecimal> ns) {
        return divide(2, RoundingMode.HALF_UP, ns);
    }

}
