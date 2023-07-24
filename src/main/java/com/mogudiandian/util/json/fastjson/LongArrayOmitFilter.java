package com.mogudiandian.util.json.fastjson;

import com.alibaba.fastjson.serializer.ValueFilter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

/**
 * FastJSON省略长数组的过滤器
 * 用法：JSON.toJSONString(jsonObject, new LongArrayOmitFilter(x))
 * @author Joshua Sun
 */
public final class LongArrayOmitFilter implements ValueFilter {

    /**
     * 默认保留长度为16
     */
    private static final int DEFAULT_RETAIN_LENGTH = 16;

    /**
     * 省略号(英文三个句点)
     */
    private static final String OMITTED_POSTFIX = "length=%d";

    /**
     * 保留长度
     */
    private final int retainLength;

    /**
     * 使用保留长度进行构造
     * @param retainLength 保留长度
     */
    public LongArrayOmitFilter(int retainLength) {
        this.retainLength = retainLength;
    }

    /**
     * 使用默认保留长度进行构造
     */
    public LongArrayOmitFilter() {
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
        // 只处理集合或数组
        if (Collection.class.isAssignableFrom(value.getClass())) {
            return getOmittedCollection((Collection<?>) value);
        } else if (value.getClass().isArray()) {
            return getOmittedArray(value);
        }
        return value;
    }

    /**
     * 获取省略后的集合
     * @param collection 原始集合
     * @return 原始集合或省略后的集合
     */
    private Collection<?> getOmittedCollection(Collection<?> collection) {
        int size = collection.size();

        // 如果长度没超 则用原始值
        if (size <= retainLength) {
            return collection;
        }

        List<Object> result = new ArrayList<>(retainLength + 1);

        // 分两种情况遍历集合
        if (collection instanceof List && collection instanceof RandomAccess) {
            List<?> list = (List<?>) collection;
            for (int i = 0, len = Math.min(retainLength, list.size()); i < len; i++) {
                result.add(list.get(i));
            }
        } else {
            for (Object object : collection) {
                if (result.size() >= retainLength) {
                    break;
                }
                result.add(object);
            }
        }

        result.add(String.format(OMITTED_POSTFIX, size));

        return result;
    }

    /**
     * 获取省略后的数组
     * @param array 原始数组
     * @return 原始数组或省略后的数组
     */
    private Object getOmittedArray(Object array) {
        int length = Array.getLength(array);

        // 如果长度没超 则用原始值
        if (length <= retainLength) {
            return array;
        }

        List<Object> result = new ArrayList<>(retainLength + 1);

        // 遍历数组
        for (int i = 0, len = Math.min(retainLength, length); i < len; i++) {
            result.add(Array.get(array, i));
        }

        result.add(String.format(OMITTED_POSTFIX, length));

        return result;
    }

}
