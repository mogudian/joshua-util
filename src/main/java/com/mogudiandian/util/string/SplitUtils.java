package com.mogudiandian.util.string;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.*;

/**
 * 字符串分割工具类
 *
 * @author Joshua Sun
 * @since 1.0.9
 */
public final class SplitUtils {

    private SplitUtils() {}

    /**
     * Splits a given string into an array of integers.
     *
     * @param string the string to be split
     * @return an array of integers
     */
    public static int[] splitToInts(String string) {
        return splitToIntStream(string).toArray();
    }

    /**
     * Splits a given string into an array of integers.
     *
     * @param string the string to be split
     * @return an array of integers
     */
    public static Integer[] splitToIntegerArray(String string) {
        return splitToIntStream(string).boxed().toArray(Integer[]::new);
    }

    /**
     * Splits a given string into a list of integers.
     *
     * @param string the string to be split
     * @return a list of integers
     */
    public static List<Integer> splitToIntegerList(String string) {
        return splitToIntStream(string).boxed().collect(Collectors.toList());
    }

    /**
     * Splits a given string into an array of longs.
     *
     * @param string the string to be split
     * @return an array of longs
     */
    public static long[] splitToLongs(String string) {
        return splitToLongStream(string).toArray();
    }

    /**
     * Splits a given string into an array of Longs.
     *
     * @param string the string to be split
     * @return an array of Longs
     */
    public static Long[] splitToLongArray(String string) {
        return splitToLongStream(string).boxed().toArray(Long[]::new);
    }

    /**
     * Splits a given string into a List of Longs.
     *
     * @param string the string to be split
     * @return a List of Longs
     */
    public static List<Long> splitToLongList(String string) {
        return splitToLongStream(string).boxed().collect(Collectors.toList());
    }

    /**
     * Splits a given string into an array of doubles.
     *
     * @param string the string to be split
     * @return an array of doubles
     */
    public static double[] splitToDoubles(String string) {
        return splitToDoubleStream(string).toArray();
    }

    /**
     * Splits a given string into an array of Double objects.
     *
     * @param string the string to be split
     * @return an array of Double objects
     */
    public static Double[] splitToDoubleArray(String string) {
        return splitToDoubleStream(string).boxed().toArray(Double[]::new);
    }

    /**
     * Splits a given string into a List of Double objects.
     *
     * @param string the string to be split
     * @return a List of Double objects
     */
    public static List<Double> splitToDoubleList(String string) {
        return splitToDoubleStream(string).boxed().collect(Collectors.toList());
    }

    /**
     * Splits a given string into an array of BigDecimal objects.
     *
     * @param string the string to be split
     * @return an array of BigDecimal objects
     */
    public static BigDecimal[] splitToBigDecimalArray(String string) {
        return splitToBigDecimalStream(string).toArray(BigDecimal[]::new);
    }

    /**
     * Splits a given string into a List of BigDecimal objects.
     *
     * @param string the string to be split
     * @return a List of BigDecimal objects
     */
    public static List<BigDecimal> splitToBigDecimalList(String string) {
        return splitToBigDecimalStream(string).collect(Collectors.toList());
    }

    /**
     * Splits the given string into an IntStream of integers.
     *
     * @param string the string to be split
     * @return an IntStream of integers obtained from the string
     */
    private static IntStream splitToIntStream(String string) {
        IntStream.Builder builder = IntStream.builder();
        split(string,
                (ch, segment) -> Character.isDigit(ch)
                        || (ch == '-' && segment.length() == 0),
                segment -> {
                    if (segment.length() > 1 || segment.charAt(0) != '-') {
                        builder.add(Integer.parseInt(segment));
                    }
                });
        return builder.build();
    }

    /**
     * Splits the given string into a LongStream of longs.
     *
     * @param string the string to be split
     * @return a LongStream of longs obtained from the string
     */
    private static LongStream splitToLongStream(String string) {
        LongStream.Builder builder = LongStream.builder();
        split(string,
                (ch, segment) -> Character.isDigit(ch)
                        || (ch == '-' && segment.length() == 0),
                segment -> {
                    if (segment.length() > 1 || segment.charAt(0) != '-') {
                        builder.add(Long.parseLong(segment));
                    }
                });
        return builder.build();
    }

    /**
     * Splits the given string into a DoubleStream of doubles.
     *
     * @param string the string to be split
     * @return a DoubleStream of doubles obtained from the string
     */
    private static DoubleStream splitToDoubleStream(String string) {
        DoubleStream.Builder builder = DoubleStream.builder();
        split(string,
                (ch, segment) -> Character.isDigit(ch)
                        || (ch == '-' && segment.length() == 0)
                        || (ch == '.' && segment.chars().noneMatch(x -> x == '.')),
                segment -> {
                    if (segment.length() > 1 || segment.charAt(0) != '-' || segment.charAt(0) != '.') {
                        builder.add(Double.parseDouble(segment));
                    }
                });
        return builder.build();
    }

    /**
     * Splits the given string into a Stream of BigDecimals.
     *
     * @param string the string to be split
     * @return a Stream of BigDecimals obtained from the string
     */
    private static Stream<BigDecimal> splitToBigDecimalStream(String string) {
        Stream.Builder<BigDecimal> builder = Stream.builder();
        split(string,
                (ch, segment) -> Character.isDigit(ch)
                        || (ch == '-' && segment.length() == 0)
                        || (ch == '.' && segment.chars().noneMatch(x -> x == '.')),
                segment -> {
                    if (segment.length() > 1 || segment.charAt(0) != '-' || segment.charAt(0) != '.') {
                        builder.add(new BigDecimal(segment));
                    }
                });
        return builder.build();
    }

    /**
     * Splits a string based on a condition and outputs each segment using a consumer.
     *
     * @param string            the string to split
     * @param retainedCondition the condition to determine if a character should be retained in a segment
     * @param segmentConsumer   the consumer to accept each segment
     */
    private static void split(String string, BiPredicate<Character, StringBuilder> retainedCondition, Consumer<String> segmentConsumer) {
        StringBuilder segment = new StringBuilder();
        for (int i = 0, len = string.length(), last = len - 1; i < len; i++) {
            char ch = string.charAt(i);
            boolean end = true;
            if (retainedCondition.test(ch, segment)) {
                segment.append(ch);
                end = false;
            }
            if ((end || i == last) && segment.length() > 0) {
                segmentConsumer.accept(segment.toString());
                segment.setLength(0);
            }
        }
    }

}
