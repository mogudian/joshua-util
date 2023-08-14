package com.mogudiandian.util.lang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import sun.misc.Unsafe;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.BiFunction;

/**
 * 增强型的比较相同工具类
 *
 * @author Joshua Sun
 * @since 2023/8/3
 */
public final class EnhancedEqualsUtils {

    private EnhancedEqualsUtils() {}

    /**
     * Compares two objects for equality.
     *
     * @param o1 The first object to compare.
     * @param o2 The second object to compare.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    public static boolean equals(Object o1, Object o2) {
        return objectEquals(o1, o2);
    }

    /**
     * Compares two objects for equality.
     *
     * @param o1 The first object to compare.
     * @param o2 The second object to compare.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    private static boolean objectEquals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }

        if (o1 instanceof Number && o2 instanceof Number) {
            return numberEquals((Number) o1, (Number) o2);
        }

        if (o1 instanceof CharSequence && o2 instanceof CharSequence) {
            return charSequenceEquals((CharSequence) o1, (CharSequence) o2);
        }

        if (isDateType(o1) && isDateType(o2)) {
            return getTimeInMillis(o1) == getTimeInMillis(o2);
        }

        if (o1 instanceof Collection && o2 instanceof Collection) {
            return collectionEquals((Collection) o1, (Collection) o2);
        }

        if (o1.getClass().isArray() && o2.getClass().isArray()) {
            return arrayEquals(o1, o2);
        }

        if (o1 instanceof Map.Entry && o2 instanceof Map.Entry) {
            return entryEquals((Map.Entry) o1, (Map.Entry) o2);
        }

        if (o1 instanceof Map && o2 instanceof Map) {
            return mapEquals((Map) o1, (Map) o2);
        }

        if (o1.equals(o2)) {
            return true;
        }

        if (beanEquals(o1, o2)) {
            return true;
        }

        return Objects.equals(o1.toString(), o2.toString());
    }

    /**
     * Compares two numbers for equality.
     *
     * @param n1 The first number to compare.
     * @param n2 The second number to compare.
     * @return {@code true} if the numbers are equal, {@code false} otherwise.
     */
    public static boolean numberEquals(Number n1, Number n2) {
        if (n1 == n2) {
            return true;
        }
        if (n1 == null || n2 == null) {
            return false;
        }
        if (n1.equals(n2)) {
            return true;
        }

        BigDecimal d1 = null, d2 = null;
        if (n1 instanceof BigDecimal) {
            d1 = (BigDecimal) n1;
        } else if (n1 instanceof Double || n1 instanceof Float) {
            d1 = BigDecimal.valueOf(n1.doubleValue());
        }
        if (n2 instanceof BigDecimal) {
            d2 = (BigDecimal) n2;
        } else if (n2 instanceof Double || n2 instanceof Float) {
            d2 = BigDecimal.valueOf(n2.doubleValue());
        }

        if (d1 != null && d2 != null) {
            return d1.compareTo(d2) == 0;
        }
        if (d1 != null) {
            return d1.compareTo(new BigDecimal(n2.toString())) == 0;
        }
        if (d2 != null) {
            return d2.compareTo(new BigDecimal(n1.toString())) == 0;
        }

        BigInteger i1 = null, i2 = null;
        if (n1 instanceof BigInteger) {
            i1 = (BigInteger) n1;
        } else if (n1 instanceof Byte || n1 instanceof Short || n1 instanceof Integer || n1 instanceof Long) {
            i1 = BigInteger.valueOf(n1.longValue());
        }
        if (n2 instanceof BigInteger) {
            i2 = (BigInteger) n2;
        } else if (n2 instanceof Byte || n2 instanceof Short || n2 instanceof Integer || n2 instanceof Long) {
            i2 = BigInteger.valueOf(n2.longValue());
        }

        if (i1 != null && i2 != null) {
            return i1.compareTo(i2) == 0;
        }
        if (i1 != null) {
            return i1.toString().equals(n2.toString());
        }
        if (i2 != null) {
            return i2.toString().equals(n1.toString());
        }

        if (n1.doubleValue() == n2.doubleValue()) {
            return true;
        }

        return new BigDecimal(n1.toString()).compareTo(new BigDecimal(n2.toString())) == 0;
    }

    /**
     * Compares two CharSequence objects for equality.
     *
     * @param s1 The first CharSequence object to compare.
     * @param s2 The second CharSequence object to compare.
     * @return {@code true} if the CharSequences are equal, {@code false} otherwise.
     */
    public static boolean charSequenceEquals(CharSequence s1, CharSequence s2) {
        if (s1 == s2) {
            return true;
        }
        if (s1 == null || s2 == null) {
            return false;
        }

        int len = s1.length();

        if (len != s2.length()) {
            return false;
        }

        boolean[] found = new boolean[len];

        for (int i = 0; i < len; i++) {
            boolean match = false;
            for (int j = 0; j < len; j++) {
                if (!found[j] && s1.charAt(i) == s2.charAt(j)) {
                    found[j] = true;
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the given object is of a date type.
     *
     * @param o The object to check.
     * @return {@code true} if the object is of a date type, {@code false} otherwise.
     */
    private static boolean isDateType(Object o) {
        return o instanceof Date
                || o instanceof Calendar
                || o instanceof Instant
                || o instanceof LocalDateTime
                || o instanceof LocalDate;
    }

    /**
     * Returns the time in milliseconds for the given date object.
     *
     * @param o The date object. This can be one of the following types:
     *          - java.util.Date
     *          - java.util.Calendar
     *          - java.time.Instant
     *          - java.time.LocalDateTime
     *          - java.time.LocalDate
     * @return The time in milliseconds represented by the given date object.
     * @throws IllegalArgumentException if the given object is not of a date type.
     */
    private static long getTimeInMillis(Object o) {
        if (o instanceof Date) {
            return ((Date) o).getTime();
        }
        if (o instanceof Calendar) {
            return ((Calendar) o).getTimeInMillis();
        }
        if (o instanceof Instant) {
            return ((Instant) o).toEpochMilli();
        }
        if (o instanceof LocalDateTime) {
            return ((LocalDateTime) o).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        if (o instanceof LocalDate) {
            return ((LocalDate) o).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        throw new IllegalArgumentException("Not support for type " + o.getClass().getName());
    }

    /**
     * Compares two collections for equality, with no need for the same order.
     *
     * @param c1 The first collection to compare.
     * @param c2 The second collection to compare.
     * @param <E> The type of elements in the collection.
     * @return {@code true} if the collections are equal, {@code false} otherwise.
     */
    public static <E> boolean collectionEquals(Collection<? extends E> c1, Collection<? extends E> c2) {
        if (c1 == c2) {
            return true;
        }
        if (c1 == null || c2 == null) {
            return false;
        }

        int len = c1.size();

        if (len != c2.size()) {
            return false;
        }

        boolean[] found = new boolean[len];

        for (Iterator<? extends E> iterator1 = c1.iterator(); iterator1.hasNext(); ) {
            E c1i = iterator1.next();
            boolean match = false;
            int j = 0;
            for (Iterator<? extends E> iterator2 = c2.iterator(); iterator2.hasNext(); j++) {
                E c2j = iterator2.next();
                if (!found[j] && objectEquals(c1i, c2j)) {
                    found[j] = true;
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }

        return true;
    }

    /**
     * Compares two arrays for equality, supporting different types of arrays, with no need for the same order.
     *
     * @param o1 The first array to compare.
     * @param o2 The second array to compare.
     * @return {@code true} if the arrays are equal, {@code false} otherwise.
     */
    public static boolean arrayEquals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }

        if (o1 instanceof Object[] && o2 instanceof Object[]) {
            return arrayEquals ((Object[]) o1, (Object[]) o2);
        }

        if (o1 instanceof long[] && o2 instanceof long[]) {
            return arrayEquals((long[]) o1, (long[]) o2);
        }

        if (o1 instanceof int[] && o2 instanceof int[]) {
            return arrayEquals((int[]) o1, (int[]) o2);
        }

        if (o1 instanceof short[] && o2 instanceof short[]) {
            return arrayEquals((short[]) o1, (short[]) o2);
        }

        if (o1 instanceof byte[] && o2 instanceof byte[]) {
            return arrayEquals((byte[]) o1, (byte[]) o2);
        }

        if (o1 instanceof double[] && o2 instanceof double[]) {
            return arrayEquals((double[]) o1, (double[]) o2);
        }

        if (o1 instanceof float[] && o2 instanceof float[]) {
            return arrayEquals((float[]) o1, (float[]) o2);
        }

        if (o1 instanceof char[] && o2 instanceof char[]) {
            return arrayEquals((char[]) o1, (char[]) o2);
        }

        if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
            return arrayEquals((boolean[]) o1, (boolean[]) o2);
        }

        return arrayEqualsByUnsafe(o1, o2);
    }

    /**
     * Compares two object arrays for equality, supporting different types of arrays, with no need for the same order.
     *
     * @param a1 The first array to compare.
     * @param a2 The second array to compare.
     * @return {@code true} if the arrays are equal, {@code false} otherwise.
     */
    public static boolean arrayEquals(Object[] a1, Object[] a2) {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        int len = a1.length;

        if (len != a2.length) {
            return false;
        }

        boolean[] found = new boolean[len];

        for (int i = 0; i < len; i++) {
            boolean match = false;
            for (int j = 0; j < len; j++) {
                if (!found[j] && objectEquals(a1[i], a2[j])) {
                    found[j] = true;
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }

        return true;
    }

    /**
     * Compares two object arrays for equality, supporting different types of arrays, with no need for the same order.
     *
     * @param a1 The first array to compare.
     * @param a2 The second array to compare.
     * @return {@code true} if the arrays are equal, {@code false} otherwise.
     */
    public static boolean arrayEquals(long[] a1, long[] a2) {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        int len = a1.length;

        if (len != a2.length) {
            return false;
        }

        boolean[] found = new boolean[len];

        for (int i = 0; i < len; i++) {
            boolean match = false;
            for (int j = 0; j < len; j++) {
                if (!found[j] && a1[i] == a2[j]) {
                    found[j] = true;
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }

        return true;
    }

    /**
     * Compares two long arrays for equality, supporting different types of arrays, with no need for the same order.
     *
     * @param a1 The first long array to compare.
     * @param a2 The second long array to compare.
     * @return {@code true} if the arrays are equal, {@code false} otherwise.
     */
    public static boolean arrayEquals(int[] a1, int[] a2) {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        int len = a1.length;

        if (len != a2.length) {
            return false;
        }

        boolean[] found = new boolean[len];

        for (int i = 0; i < len; i++) {
            boolean match = false;
            for (int j = 0; j < len; j++) {
                if (!found[j] && a1[i] == a2[j]) {
                    found[j] = true;
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }

        return true;
    }

    /**
     * Compares two short arrays for equality, supporting different types of arrays, with no need for the same order.
     *
     * @param a1 The first short array to compare.
     * @param a2 The second short array to compare.
     * @return {@code true} if the arrays are equal, {@code false} otherwise.
     */
    public static boolean arrayEquals(short[] a1, short[] a2) {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        int len = a1.length;

        if (len != a2.length) {
            return false;
        }

        boolean[] found = new boolean[len];

        for (int i = 0; i < len; i++) {
            boolean match = false;
            for (int j = 0; j < len; j++) {
                if (!found[j] && a1[i] == a2[j]) {
                    found[j] = true;
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }

        return true;
    }

    /**
     * Compares two byte arrays for equality, supporting different types of arrays, with no need for the same order.
     *
     * @param a1 The first byte array to compare.
     * @param a2 The second byte array to compare.
     * @return {@code true} if the arrays are equal, {@code false} otherwise.
     */
    public static boolean arrayEquals(byte[] a1, byte[] a2) {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        int len = a1.length;

        if (len != a2.length) {
            return false;
        }

        boolean[] found = new boolean[len];

        for (int i = 0; i < len; i++) {
            boolean match = false;
            for (int j = 0; j < len; j++) {
                if (!found[j] && a1[i] == a2[j]) {
                    found[j] = true;
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }

        return true;
    }

    /**
     * Compares two double arrays for equality, supporting different types of arrays, with no need for the same order.
     *
     * @param a1 The first double array to compare.
     * @param a2 The second double array to compare.
     * @return {@code true} if the arrays are equal, {@code false} otherwise.
     */
    public static boolean arrayEquals(double[] a1, double[] a2) {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        int len = a1.length;

        if (len != a2.length) {
            return false;
        }

        boolean[] found = new boolean[len];

        for (int i = 0; i < len; i++) {
            boolean match = false;
            for (int j = 0; j < len; j++) {
                if (!found[j] && a1[i] == a2[j]) {
                    found[j] = true;
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }

        return true;
    }

    /**
     * Compares two float arrays for equality, supporting different types of arrays, with no need for the same order.
     *
     * @param a1 The first float array to compare.
     * @param a2 The second float array to compare.
     * @return {@code true} if the arrays are equal, {@code false} otherwise.
     */
    public static boolean arrayEquals(float[] a1, float[] a2) {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        int len = a1.length;

        if (len != a2.length) {
            return false;
        }

        boolean[] found = new boolean[len];

        for (int i = 0; i < len; i++) {
            boolean match = false;
            for (int j = 0; j < len; j++) {
                if (!found[j] && a1[i] == a2[j]) {
                    found[j] = true;
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }

        return true;
    }

    /**
     * Compares two char arrays for equality, supporting different types of arrays, with no need for the same order.
     *
     * @param a1 The first char array to compare.
     * @param a2 The second char array to compare.
     * @return {@code true} if the arrays are equal, {@code false} otherwise.
     */
    public static boolean arrayEquals(char[] a1, char[] a2) {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        int len = a1.length;

        if (len != a2.length) {
            return false;
        }

        boolean[] found = new boolean[len];

        for (int i = 0; i < len; i++) {
            boolean match = false;
            for (int j = 0; j < len; j++) {
                if (!found[j] && a1[i] == a2[j]) {
                    found[j] = true;
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }

        return true;
    }

    /**
     * Compares two boolean arrays for equality, supporting different types of arrays, with no need for the same order.
     *
     * @param a1 The first boolean array to compare.
     * @param a2 The second boolean array to compare.
     * @return {@code true} if the arrays are equal, {@code false} otherwise.
     */
    public static boolean arrayEquals(boolean[] a1, boolean[] a2) {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        int len = a1.length;

        if (len != a2.length) {
            return false;
        }

        boolean[] found = new boolean[len];

        for (int i = 0; i < len; i++) {
            boolean match = false;
            for (int j = 0; j < len; j++) {
                if (!found[j] && a1[i] == a2[j]) {
                    found[j] = true;
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }

        return true;
    }

    /**
     * Compares two arrays of objects for equality, supporting different types of arrays, with no need for the same order.
     *
     * @param o1 The first array to compare.
     * @param o2 The second array to compare.
     * @return {@code true} if the arrays are equal, {@code false} otherwise.
     */
    private static boolean arrayEqualsByUnsafe(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }

        int len = Array.getLength(o1);

        if (len != Array.getLength(o2)) {
            return false;
        }

        Unsafe unsafe = UnsafeHolder.UNSAFE;

        BiFunction<Object, Long, Number> accessor1 = getPrimitiveArrayElementAccessor(unsafe, o1);
        if (accessor1 == null) {
            return arrayEqualsByReflection(o1, o2);
        }

        BiFunction<Object, Long, Number> accessor2 = getPrimitiveArrayElementAccessor(unsafe, o2);
        if (accessor2 == null) {
            return arrayEqualsByReflection(o1, o2);
        }

        Class<?> o1Class = o1.getClass(), o2Class = o2.getClass();

        int offset1 = unsafe.arrayBaseOffset(o1Class), elementSize1 = unsafe.arrayIndexScale(o1Class), shift1 = 31 - Integer.numberOfLeadingZeros(elementSize1);
        int offset2 = unsafe.arrayBaseOffset(o2Class), elementSize2 = unsafe.arrayIndexScale(o2Class), shift2 = 31 - Integer.numberOfLeadingZeros(elementSize2);

        boolean[] found = new boolean[len];

        for (int i = 0; i < len; i++) {
            double o1i = accessor1.apply(o1, offset1 + ((long) i << shift1)).doubleValue();
            boolean match = false;
            for (int j = 0; j < len; j++) {
                if (!found[j] && o1i == accessor2.apply(o2, offset2 + ((long) j << shift2)).doubleValue()) {
                    found[j] = true;
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }

        return true;
    }

    /**
     * 获取基本类型数组的元素访问器
     * 访问器为双参函数 参数1为数组对象 参数2为offset(不是index)
     * @param unsafe Unsafe对象
     * @param object 数组
     * @return 访问器的函数 如果返回null表示不是基本类型数组
     */
    private static BiFunction<Object, Long, Number> getPrimitiveArrayElementAccessor(Unsafe unsafe, Object object) {
        if (object instanceof long[]) {
            return unsafe::getLong;
        }
        if (object instanceof int[]) {
            return unsafe::getInt;
        }
        if (object instanceof short[]) {
            return unsafe::getShort;
        }
        if (object instanceof byte[]) {
            return unsafe::getByte;
        }
        if (object instanceof double[]) {
            return unsafe::getDouble;
        }
        if (object instanceof float[]) {
            return unsafe::getFloat;
        }
        if (object instanceof char[]) {
            return (arr, offset) -> (int) unsafe.getChar(arr, offset);
        }
        if (object instanceof boolean[]) {
            return (arr, offset) -> unsafe.getBoolean(arr, offset) ? 1 : 0;
        }
        return null;
    }

    /**
     * 使用反射比较两个数组是否相等
     * @param o1 第一个数组
     * @param o2 第二个数组
     * @return 如果两个数组相等返回true，否则返回false
     */
    private static boolean arrayEqualsByReflection(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }

        int len = Array.getLength(o1);

        if (len != Array.getLength(o2)) {
            return false;
        }

        boolean[] found = new boolean[len];

        for (int i = 0; i < len; i++) {
            Object o1i = Array.get(o1, i);
            boolean match = false;
            for (int j = 0; j < len; j++) {
                if (!found[j] && objectEquals(o1i, Array.get(o2, j))) {
                    found[j] = true;
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }

        return true;
    }

    /**
     * 比较两个Map.Entry对象是否相等
     *
     * @param e1 第一个Map.Entry对象
     * @param e2 第二个Map.Entry对象
     * @param <K> Map.Entry键的类型
     * @param <V> Map.Entry值的类型
     * @return 如果两个Map.Entry对象相等返回true，否则返回false
     */
    public static <K, V> boolean entryEquals(Map.Entry<? extends K, ? extends V> e1, Map.Entry<? extends K, ? extends V> e2) {
        if (e1 == e2) {
            return true;
        }
        if (e1 == null || e2 == null) {
            return false;
        }

        return objectEquals(e1.getKey(), e2.getKey()) && objectEquals(e1.getValue(), e2.getValue());
    }

    /**
     * 比较两个Map对象是否相等
     *
     * @param m1 第一个Map对象
     * @param m2 第二个Map对象
     * @param <K> Map键的类型
     * @param <V> Map值的类型
     * @return 如果两个Map对象相等返回true，否则返回false
     */
    public static <K, V> boolean mapEquals(Map<? extends K, ? extends V> m1, Map<? extends K, ? extends V> m2) {
        if (m1 == m2) {
            return true;
        }
        if (m1 == null || m2 == null) {
            return false;
        }

        int len = m1.size();

        if (len != m2.size()) {
            return false;
        }

        return collectionEquals(m1.entrySet(), m2.entrySet());
    }

    /**
     * 比较两个Bean对象是否相等
     *
     * @param o1 第一个Bean对象
     * @param o2 第二个Bean对象
     * @return 如果两个Bean对象相等返回true，否则返回false
     */
    private static boolean beanEquals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }

        Object j1 = JSON.toJSON(o1), j2 = JSON.toJSON(o2);

        if (j1 instanceof JSONArray && j2 instanceof JSONArray) {
            return collectionEquals((JSONArray) j1, (JSONArray) j2);
        }

        if (j1 instanceof JSONObject && j2 instanceof JSONObject) {
            JSONObject jo1 = (JSONObject) j1, jo2 = (JSONObject) j2;
            removeNullValuesForMap(jo1);
            removeNullValuesForMap(jo2);
            return mapEquals(jo1, jo2);
        }

        return false;
    }

    /**
     * 移除Map中值为null的元素
     *
     * @param map 要操作的Map对象
     */
    private static void removeNullValuesForMap(Map<?, ?> map) {
        map.entrySet().removeIf(entry -> entry.getValue() == null);
    }

    /**
     * This class provides a means to obtain the singleton instance of the {@code Unsafe} class, which allows access to low-level operations in Java.
     * <p>
     * The singleton instance of {@code Unsafe} is obtained using reflection to access the private field "theUnsafe" in the {@code Unsafe} class.
     * </p>
     * <p>
     * <strong>Note:</strong> The {@code Unsafe} class is intended for advanced developers and should be used with caution as it allows direct memory access and other unsafe operations.
     * </p>
     */
    private static class UnsafeHolder {
        private static final Unsafe UNSAFE;

        static {
            try {
                Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafeField.setAccessible(true);
                UNSAFE = (Unsafe) theUnsafeField.get(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
