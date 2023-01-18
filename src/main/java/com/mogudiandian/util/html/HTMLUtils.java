package com.mogudiandian.util.html;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * HTML工具类
 *
 * @author sunbo
 */
public final class HTMLUtils {

    /**
     * XML/HTML的标签正则 通配 <xxx> </xxx> <xxx />
     */
    private static final Pattern TAG_ALL = Pattern.compile("<(/?)(.*?)(/?)>");

    /**
     * 不需要闭合的标签 该死的HTML5/宽松模式
     */
    private static final List<String> NO_CLOSED_TAGS;

    static {
        Set<String> set = new HashSet<>();
        set.add("!");
        set.add("meta");
        set.add("link");
        set.add("input");
        set.add("img");
        set.add("hr");
        set.add("br");
        set.add("area");
        set.add("base");
        set.add("col");
        set.add("command");
        set.add("embed");
        set.add("keygen");
        set.add("param");
        set.add("source");
        set.add("track");
        set.add("wbr");

        NO_CLOSED_TAGS = set.stream()
                            .map(x -> "<" + x)
                            .collect(Collectors.toList());
    }

    /**
     * HTML通用标签正则
     */
    private static final Pattern HTML_TAG_REGEX = Pattern.compile("<.+?>", Pattern.MULTILINE);

    /**
     * HTML中script标签正则
     */
    private static final Pattern HTML_SCRIPT_REGEX = Pattern.compile("(<script.*?>)(.*?)(</script>)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    /**
     * HTML中style标签正则
     */
    private static final Pattern HTML_STYLE_REGEX = Pattern.compile("(<style.*?>)(.*?)(</style>)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    private HTMLUtils() {}

    /**
     * 去除标签
     * @param html HTML
     * @return 去除标签后的纯文本
     */
    public static String removeTags(String html) {
        // 参数校验
        if (html == null) {
            throw new IllegalArgumentException("HTML can not be null");
        }
        if (html.length() == 0) {
            return html;
        }
        String value = html;
        // 先去掉script和style
        for (Pattern pattern : Arrays.asList(HTML_SCRIPT_REGEX, HTML_STYLE_REGEX)) {
            value = pattern.matcher(value).replaceAll("$1$3");
        }
        // 再去掉所有标签
        return HTML_TAG_REGEX.matcher(value).replaceAll("");
    }

    /**
     * 摘要
     * <pre>
     * summarize('123<body>456</body>789', 8, false) => 12345678
     * summarize('123<body>456</body>789', 8, true) => 123<body>456</body>78
     * </pre>
     *
     * @param html HTML
     * @param length 摘要的长度
     * @param retainTags 是否需要包含标签
     * @return 指定长度的摘要
     */
    public static String summarize(String html, int length, boolean retainTags) {
        // 参数校验
        if (html == null) {
            throw new IllegalArgumentException("HTML can not be null");
        }
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive");
        }

        if (html.length() == 0) {
            return html;
        }

        // 开始标签栈 当需要标签时 将前面的标签入栈 最后出栈 保证闭合
        LinkedList<String> stack = retainTags ? new LinkedList<>() : null;

        // 最终要返回的结果
        StringBuilder stringBuilder = new StringBuilder(length);

        // index表示当前已到达的下标 count表示已摘要字数
        int index = 0, count = 0;

        for (Matcher matcher = TAG_ALL.matcher(html); matcher.find(); ) {
            // 如果匹配到了标签 则获取上一次位置到当前标签开始的子串
            String str = html.substring(index, matcher.start()).replace(" ", "");
            int available = str.length();

            // 如果有子串 说明标签和标签中间有文字
            if (available > 0) {
                // 当前需要的长度 = 参数需要的总长度 - 已摘要字数
                int need = length - count;
                // 需要的比本次的子串多 直接加入缓冲区
                if (need >= available) {
                    stringBuilder.append(str);
                    count += str.length();
                } else {
                    // 需要的没那么多 则从子串中要多少截多少
                    stringBuilder.append(str, 0, need);
                    count += need;
                    break;
                }
            }

            // 如果需要保留标签
            if (retainTags) {
                // 结束标识 </xxx>
                boolean isEnd = !matcher.group(1).isEmpty();
                // 自封闭标识 <xxx />
                boolean isSelfClosed = !matcher.group(3).isEmpty();
                // 当前的标签
                String tag = matcher.group();
                // 如果不是自封闭 看是否允许不封闭（宽松模式）
                if (!isSelfClosed) {
                    isSelfClosed = NO_CLOSED_TAGS.stream().anyMatch(tag::startsWith);
                }

                // 将标签加入到缓冲区
                stringBuilder.append(tag);

                // 如果是结束标签 则将原来放到栈中的开始标签取走
                if (isEnd) {
                    stack.removeFirst();
                } else if (!isSelfClosed) {
                    // 如果是开始标签并且不是自封闭标签 则入栈 因为自封闭的不需要最后闭合
                    stack.addFirst(tag);
                }
            }

            index = matcher.end();
        }

        // 匹配完以后可能还会剩余部分数据 根据长度进行读取
        for (int len = html.length(); index < len && count < length; index++) {
            char ch = html.charAt(index);
            if (ch != ' ') {
                stringBuilder.append(ch);
                count++;
            }
        }

        // 最后如果需要标签 并且栈中还有没闭合的标签 依次弹出并拼接闭合标签
        if (retainTags) {
            while (!stack.isEmpty()) {
                String tag = stack.removeFirst();
                String temp = tag.substring(1, tag.length() - 1);
                int i = temp.indexOf(' ');
                if (i >= 0) {
                    temp = temp.substring(0, i);
                }
                stringBuilder.append("</").append(temp).append(">");
            }
        }

        return stringBuilder.toString();
    }

}
