package com.mogudiandian.util.lang;

/**
 * 范围
 *
 * @author Joshua Sun
 * @since 1.0.17
 */
public class Range<T extends Comparable<T>> {

    /**
     * 开始
     */
    private final T start;

    /**
     * 是否包含开始
     */
    private final boolean startInclusive;

    /**
     * 结束
     */
    private final T end;

    /**
     * 是否包含结束
     */
    private final boolean endInclusive;

    /**
     * 构造函数 默认左开右闭区间
     * @param start 开始 如果为空表示负无穷
     * @param end 结束 如果为空表示正无穷
     */
    public Range(T start, T end) {
        this(start, true, end, false);
    }

    /**
     * 构造函数
     * @param start 开始 如果为空表示负无穷
     * @param startInclusive 是否包含开始
     * @param end 结束 如果为空表示正无穷
     * @param endInclusive 是否包含结束
     */
    public Range(T start, boolean startInclusive, T end, boolean endInclusive) {
        if (start != null && end != null) {
            int compareResult = start.compareTo(end);
            if (compareResult > 0) {
                throw new IllegalArgumentException("start must be less than or equal to end");
            } else if (compareResult == 0) {
                if (!startInclusive || !endInclusive) {
                    throw new IllegalArgumentException("start and end must be inclusive if they are equal");
                }
            }
        }
        this.start = start;
        this.startInclusive = startInclusive;
        this.end = end;
        this.endInclusive = endInclusive;
    }

    /**
     * 是否包含
     * @param t 元素
     * @return 是否包含
     */
    public boolean contains(T t) {
        return (start == null || (startInclusive ? t.compareTo(start) >= 0 : t.compareTo(start) > 0))
                && (end == null || (endInclusive ? t.compareTo(end) <= 0 : t.compareTo(end) < 0));
    }

    /**
     * 是否有交集
     * @param other 其他范围
     * @return 是否有交集 true 有交集 false 无交集
     */
    public boolean hasIntersection(Range<T> other) {
        if (start != null && other.end != null) {
            if (startInclusive && other.endInclusive) {
                if (start.compareTo(other.end) > 0) {
                    return false;
                }
            } else {
                if (start.compareTo(other.end) >= 0) {
                    return false;
                }
            }
        }
        if (end != null && other.start != null) {
            if (endInclusive && other.startInclusive) {
                if (end.compareTo(other.start) < 0) {
                    return false;
                }
            } else {
                if (end.compareTo(other.start) <= 0) {
                    return false;
                }
            }
        }
        return true;
    }

}
