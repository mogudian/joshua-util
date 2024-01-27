package com.mogudiandian.util.lang;

/**
 * 布尔值工具类，扩展了 {@link org.apache.commons.lang3.BooleanUtils} 的功能
 * @see org.apache.commons.lang3.BooleanUtils
 *
 * @author Joshua Sun
 * @since 2023/11/29
 */
public final class BooleanUtils extends org.apache.commons.lang3.BooleanUtils {

    private BooleanUtils() {}

    /**
     * Checks if the given Boolean value is true or null.
     *
     * @param bool the Boolean value to be checked
     * @return true if the Boolean value is true or null, false otherwise
     */
    public static boolean isTrueOrNull(Boolean bool) {
        return bool == null || bool;
    }

    /**
     * Checks if the given Boolean value is false or null.
     *
     * @param bool the Boolean value to be checked
     * @return true if the Boolean value is false or null, false otherwise
     */
    public static boolean isFalseOrNull(Boolean bool) {
        return bool == null || !bool;
    }

}
