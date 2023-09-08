package com.mogudiandian.util.json.fastjson;

import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * FastJSON省略长字符串的过滤器
 * 用法：JSON.toJSONString(jsonObject, new LongStringOmitFilter(x))
 * @author Joshua Sun
 * @since 1.0.0
 */
public final class LongStringOmitFilter implements ValueFilter {

    /**
     * 默认保留长度为16
     */
    private static final int DEFAULT_RETAIN_LENGTH = 16;

    /**
     * 省略号(英文三个句点)
     */
    private static final String OMITTED_POSTFIX = "...";

    /**
     * 保留长度
     */
    private final int retainLength;

    /**
     * 使用保留长度进行构造
     * @param retainLength 保留长度
     */
    public LongStringOmitFilter(int retainLength) {
        this.retainLength = retainLength;
    }

    /**
     * 使用默认保留长度进行构造
     */
    public LongStringOmitFilter() {
        this(DEFAULT_RETAIN_LENGTH);
    }

    @Override
    public Object process(Object object, String name, Object value) {
        if (object == null) {
            return null;
        }
        if (value == null) {
            return null;
        }
        // 只处理CharSequence
        if (CharSequence.class.isAssignableFrom(value.getClass())) {
            return getOmittedCharSequence((CharSequence) value);
        }
        return value;
    }

    /**
     * 获取省略后的字符串
     * @param charSequence 原始串
     * @return 原始串或省略后的串
     */
    private CharSequence getOmittedCharSequence(CharSequence charSequence) {
        // 如果长度没超 则用原始值
        if (charSequence.length() <= retainLength) {
            return charSequence;
        }
        // 如果长度超了 则取指定保留长度的前面加上省略号
        CharSequence newSequence = charSequence.subSequence(0, retainLength);
        return newSequence + OMITTED_POSTFIX;
    }

}
