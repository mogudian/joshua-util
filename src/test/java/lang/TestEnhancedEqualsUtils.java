package lang;

import com.mogudiandian.util.lang.EnhancedEqualsUtils;
import com.mogudiandian.util.map.HashMapBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 测试增强型Equals工具类
 *
 * @author Joshua Sun
 * @since 2023/8/4
 */
public class TestEnhancedEqualsUtils {

    public static void main(String[] args) {
        // testNumber();
        // testString();
        // testDate();
        // testCollection();
        // testArray();
        testMap();
    }

    private static void testMap() {
        Map<String, Object> m1 = HashMapBuilder.build("1", 1, "2", 2, "3", "3");
        Map<Integer, Object> m2 = HashMapBuilder.build(1, "1", 2, "2", 3, "3");
        System.out.println(EnhancedEqualsUtils.equals(m1, m2));
    }

    private static void testArray() {
        int[] a1 = {1, 2, 3, 4, 5, 6, 7, 8};
        long[] a2 = {8L, 7L, 6L, 5L, 4L, 3L, 2L, 1L};
        String[] a3 = {"5", "6", "7", "8", "1", "2", "3", "4"};
        System.out.println(EnhancedEqualsUtils.equals(a1, a2));
        System.out.println(EnhancedEqualsUtils.equals(a1, a3));
        System.out.println(EnhancedEqualsUtils.equals(a2, a3));
    }

    private static void testCollection() {
        List<Integer> list = Stream.of(1, 2, 3, 4, 5, 6, 7, 8).collect(Collectors.toList());
        Set<Long> set = Stream.of(5L, 6L, 7L, 8L, 1L, 2L, 3L, 4L).collect(Collectors.toSet());
        System.out.println(EnhancedEqualsUtils.equals(list, set));
    }

    private static void testDate() {
        long timestamp = System.currentTimeMillis();
        Date d1 = new Date(timestamp);
        Calendar d2 = Calendar.getInstance();
        d2.setTimeInMillis(timestamp);
        Instant d3 = Instant.ofEpochMilli(timestamp);
        LocalDateTime d4 = d3.atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDate d5 = d4.toLocalDate();
        System.out.println(EnhancedEqualsUtils.equals(d1, d2));
        System.out.println(EnhancedEqualsUtils.equals(d1, d3));
        System.out.println(EnhancedEqualsUtils.equals(d1, d4));
        System.out.println(EnhancedEqualsUtils.equals(d1, d5));
        d5 = LocalDate.now();
        d4 = d5.atStartOfDay();
        System.out.println(EnhancedEqualsUtils.equals(d4, d5));
    }

    private static void testString() {
        String s1 = "abcdefg";
        StringBuilder s2 = new StringBuilder(s1);
        String s3 = "hijklmn";
        System.out.println(EnhancedEqualsUtils.equals(s1, s2));
        System.out.println(EnhancedEqualsUtils.equals(s1, s3));
        System.out.println(EnhancedEqualsUtils.equals(s2, s3));
    }

    public static void testNumber() {
        int n1 = 15;
        float n2 = 15.0f;
        long n3 = 16;
        BigDecimal n4 = new BigDecimal("16");
        System.out.println(EnhancedEqualsUtils.equals(n1, n2));
        System.out.println(EnhancedEqualsUtils.equals(n1, n3));
        System.out.println(EnhancedEqualsUtils.equals(n2, n3));
        System.out.println(EnhancedEqualsUtils.equals(n3, n4));
    }

}
