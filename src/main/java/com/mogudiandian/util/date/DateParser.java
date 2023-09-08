package com.mogudiandian.util.date;

import com.mogudiandian.util.regex.RegexUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间字符串解析器
 * 1.初始化 将格式yyyy-MM-dd替换为正则(\d{1,4}-\d{1,2}-\d{1,2})并标识1对应年2对应月3对应日
 * 2.解析 如果正则能匹配 则将相应的group提取出来并将对应的field设置成提取出来的数字
 * 这个轮子的好处是线程安全 并且不会抛ParseException 无法解析时返回null
 *          不足在于只要形似时间即可 无法对真实性进行判断
 * @author Joshua Sun
 * @since 1.0.0
 */
public final class DateParser {

    /**
     * 正则的缓存 格式串 -> 正则+位置 例如 yyyyMMdd -> [(\d{1,4})(\d{1,2})(\d{1,2}), {1: year, 2: month, 3:date}]
     */
    private static final Map<String, Map.Entry<Pattern, Map<Integer, Integer>>> patternAndIndexMapCache = new ConcurrentHashMap<>();

    /**
     * 标识符对应的字段
     */
    private static final Map<String, Integer> fieldMap = new LinkedHashMap<>();

    static {
        fieldMap.put("yyyy", Calendar.YEAR);
        fieldMap.put("MM", Calendar.MONTH);
        fieldMap.put("dd", Calendar.DATE);
        fieldMap.put("HH", Calendar.HOUR_OF_DAY);
        fieldMap.put("a", Calendar.AM_PM);
        fieldMap.put("hh", Calendar.HOUR);
        fieldMap.put("mm", Calendar.MINUTE);
        fieldMap.put("ss", Calendar.SECOND);
        fieldMap.put("SSS", Calendar.MILLISECOND);
    }

    /**
     * format的长度 如果传入的字符串长度大于当前可识别长度 则认为无法解析
     */
    private final int length;

    /**
     * 格式
     */
    private final String format;

    /**
     * 转换后的正则对象
     */
    private final Pattern pattern;

    /**
     * 上下午
     */
    private Boolean amPm;

    /**
     * 记录每一个占位符对应的字段 用于匹配后替换
     */
    private final Map<Integer, Integer> indexMap;

    /**
     * 使用指定格式构造
     *
     * @param format 格式
     * @see java.text.SimpleDateFormat#SimpleDateFormat(String)
     */
    public DateParser(String format) {
        if (StringUtils.isBlank(format)) {
            throw new IllegalArgumentException();
        }
        this.format = format;
        this.length = format.length();
        Map.Entry<Pattern, Map<Integer, Integer>> patternAndIndexMap = getPatternAndIndexMap(format);
        this.pattern = patternAndIndexMap.getKey();
        this.indexMap = patternAndIndexMap.getValue();
    }

    public DateParser(String format, boolean amPm) {
        this(format);
        this.amPm = amPm;
    }

    /**
     * 获取格式串对应的正则和位置
     * @param format 格式串
     * @return 正则
     */
    private Map.Entry<Pattern, Map<Integer, Integer>> getPatternAndIndexMap(String format) {
        Map.Entry<Pattern, Map<Integer, Integer>> patternAndIndexMap = patternAndIndexMapCache.get(format);
        if (patternAndIndexMap == null) {
            patternAndIndexMap = patternAndIndexMapCache.computeIfAbsent(format, this::generatePatternAndIndexMap);
        }
        return patternAndIndexMap;
    }

    /**
     * 格式串转换为正则和位置
     *
     * @param format 格式串
     * @return 正则
     */
    private Map.Entry<Pattern, Map<Integer, Integer>> generatePatternAndIndexMap(String format) {
        // 先转义
        format = RegexUtils.escape(format);

        // 正则的group是从1开始
        int groupIndex = 1;
        // 记录分组位置对应Calendar字段
        Map<Integer, Integer> indexMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : fieldMap.entrySet()) {
            String field = entry.getKey();
            if (format.contains(field)) {
                // yyyy替换为(\d{1,4}) MM替换为(\d{1,2})
                format = format.replace(field, "(" + "\\d{1," + field.length() + "})");
                // 记录分组位置对应的是哪个字段 比如group1对应的是YEAR
                indexMap.put(groupIndex++, entry.getValue());
            }
        }
        return new AbstractMap.SimpleImmutableEntry<>(Pattern.compile(format), indexMap);
    }

    /**
     * 解析
     *
     * @param str 字符串
     * @return 返回非空表示可以解析 空表示无法解析
     */
    public Date parse(String str) {
        if (str == null) {
            return null;
        }

        if (str.length() > length) {
            return null;
        }

        Matcher matcher = pattern.matcher(str);
        if (!matcher.matches()) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        if (amPm != null) {
            calendar.set(Calendar.AM_PM, amPm ? Calendar.AM : Calendar.PM);
        }

        for (Map.Entry<Integer, Integer> entry : indexMap.entrySet()) {
            String value = matcher.group(entry.getKey());
            // 将记录的位置上的字段替换为提取出的值
            int number = Integer.parseInt(value);
            // CalendarAPI的month是从0开始的
            if (entry.getValue() == Calendar.MONTH) {
                number--;
            }
            calendar.set(entry.getValue(), number);
        }

        return calendar.getTime();
    }

    /**
     * 格式化
     * @param date 时间
     * @return 格式化后的字符串
     */
    public String format(Date date) {
        return format(date, true);
    }

    /**
     * 格式化
     * @param date 时间
     * @param zeroPadding 是否补零
     * @return 格式化后的字符串
     */
    String format(Date date, boolean zeroPadding) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        StringBuilder stringBuilder = new StringBuilder(this.format);
        for (Map.Entry<String, Integer> entry : fieldMap.entrySet()) {
            String format = entry.getKey();
            int value = entry.getValue();
            for (int index = 0; (index = stringBuilder.indexOf(format, index)) >= 0; ) {
                int number = calendar.get(value);
                if (value == Calendar.MONTH) {
                    number++;
                }
                String str = zeroPadding ? String.format("%0" + format.length() + "d", number) : Integer.toString(number);
                stringBuilder.replace(index, index + format.length(), str);
                index += format.length();
            }
        }
        return stringBuilder.toString();
    }

    public Boolean getAmPm() {
        return amPm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateParser that = (DateParser) o;
        return format.equals(that.format);
    }

    @Override
    public int hashCode() {
        return Objects.hash(format);
    }
}
