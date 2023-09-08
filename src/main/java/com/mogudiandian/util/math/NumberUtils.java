package com.mogudiandian.util.math;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 数值工具类
 *
 * @author Joshua Sun
 * @since 1.0.1
 */
public final class NumberUtils {

    private NumberUtils() {}

    /**
     * 解析字符串为数值 兼容前后有空格的情况
     * @param str 字符串
     * @param validateFn 校验是否可解析的函数
     * @param parseFn 解析为具体类型的函数
     * @return 具体类型的对象
     * @param <T> 返回数值的类型
     */
    private static <T> Optional<T> parseNumber(String str, Predicate<String> validateFn, Function<String, T> parseFn) {
        // 校验参数
        Objects.requireNonNull(validateFn);
        Objects.requireNonNull(parseFn);

        // 参数非法返回empty
        if (StringUtils.isEmpty(str)) {
            return Optional.empty();
        }

        // trim后校验是否为空串
        str = str.trim();
        if (str.length() == 0) {
            return Optional.empty();
        }

        // 校验失败返回empty
        if (!validateFn.test(str)) {
            return Optional.empty();
        }

        // 校验成功正常返回
        return Optional.of(parseFn.apply(str));
    }

    /**
     * 解析字符串为Long
     * @param value 字符串
     * @return 数字，可能会失败，失败不会抛异常，除非越界
     */
    public static Optional<Long> parseLong(String value) {
        return parseNumber(value, NumberUtils::isInteger, Long::parseLong);
    }

    /**
     * 解析字符串为Integer
     * @param value 字符串
     * @return 数字，可能会失败，失败不会抛异常，除非越界
     */
    public static Optional<Integer> parseInteger(String value) {
        return parseLong(value).map(Math::toIntExact);
    }

    /**
     * 解析字符串为BigInteger
     * @param value 字符串
     * @return 数字，可能会失败，失败不会抛异常，除非越界
     */
    public static Optional<BigInteger> parseBigInteger(String value) {
        return parseNumber(value, NumberUtils::isInteger, BigInteger::new);
    }

    /**
     * 解析字符串为Double
     * @param value 字符串
     * @return 数字，可能会失败，失败不会抛异常，除非越界
     */
    public static Optional<Double> parseDouble(String value) {
        return parseNumber(value, NumberUtils::isDecimal, Double::parseDouble);
    }

    /**
     * 解析字符串为Float
     * @param value 字符串
     * @return 数字，可能会失败，失败不会抛异常，除非越界
     */
    public static Optional<Float> parseFloat(String value) {
        return parseNumber(value, NumberUtils::isDecimal, Float::parseFloat);
    }

    /**
     * 解析字符串为BigDecimal
     * @param value 字符串
     * @return 数字，可能会失败，失败不会抛异常
     */
    public static Optional<BigDecimal> parseBigDecimal(String value) {
        return parseNumber(value, NumberUtils::isDecimal, BigDecimal::new);
    }

    /**
     * 是否为整数
     * @param str 字符串
     * @return 是否为整数
     */
    private static boolean isInteger(String str) {
        for (int i = 0, len = str.length(); i < len; i++) {
            char ch = str.charAt(i);
            // 数字或者第一位是负号
            if (Character.isDigit(ch) || (i == 0 && ch == '-')) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * 是否为小数或整数
     * @param str 字符串
     * @return 是否为小数或整数
     */
    private static boolean isDecimal(String str) {
        for (int i = 0, len = str.length(), point = 0; i < len; i++) {
            char ch = str.charAt(i);
            // 数字或者第一位是负号
            if (Character.isDigit(ch) || (i == 0 && ch == '-')) {
                continue;
            }
            // 非第一位是点号
            if (i > 0 && ch == '.') {
                // 前面没出现过点号
                if (point == 0) {
                    // 标记已出现过点号
                    point = 1;
                    continue;
                }
            }
            return false;
        }
        return true;
    }

}
