package com.mogudiandian.util.regex;

/**
 * 正则工具类
 * @author Joshua Sun
 * @since 1.0.0
 */
public final class RegexUtils {

    private RegexUtils() {}

    /**
     * 对文本中的正则符号进行转义
     * @param text 文本
     * @return 转义后的文本
     */
    public static String escape(String text) {
        return text.replace("\\", "\\\\")
                   .replace("*", "\\*")
                   .replace("+", "\\+")
                   .replace("{", "\\{")
                   .replace("}", "\\}")
                   .replace("(", "\\(")
                   .replace(")", "\\)")
                   .replace("^", "\\^")
                   .replace("$", "\\$")
                   .replace("[", "\\[")
                   .replace("]", "\\]")
                   .replace("?", "\\?")
                   .replace(",", "\\,")
                   .replace(".", "\\.")
                   .replace("&", "\\&")
                   .replace("|", "\\|");
    }

}
