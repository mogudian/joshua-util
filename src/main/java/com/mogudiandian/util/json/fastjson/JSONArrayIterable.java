package com.mogudiandian.util.json.fastjson;

import com.alibaba.fastjson.JSONArray;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * JSONArray的迭代器
 * @author sunbo
 */
public final class JSONArrayIterable<T> implements Iterable<T> {

    /**
     * 内部维护着一个list 使用委托
     */
    private List<T> items;

    public JSONArrayIterable(JSONArray jsonArray, Class<T> itemType) {
        if (jsonArray == null) {
            throw new RuntimeException("json array can not be null");
        }
        if (itemType == null) {
            throw new RuntimeException("item type can not be null");
        }
        items = IntStream.range(0, jsonArray.size())
                         .mapToObj(x -> jsonArray.getObject(x, itemType))
                         .collect(Collectors.toList());
    }

    @Override
    public Iterator<T> iterator() {
        return items.iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return items.spliterator();
    }

    public static <T> JSONArrayIterable<T> iterator(JSONArray jsonArray, Class<T> itemType) {
        return new JSONArrayIterable<>(jsonArray, itemType);
    }
}
