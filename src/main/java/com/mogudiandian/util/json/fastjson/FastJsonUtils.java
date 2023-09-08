package com.mogudiandian.util.json.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Collection;
import java.util.Date;

/**
 * FastJSON的工具类
 * @author Joshua Sun
 * @since 1.0.0
 */
public final class FastJsonUtils {

    private FastJsonUtils() {
        super();
    }

    /**
     * 判断一个类型是否为JSON的简单类型(非对象和集合)
     * @param clazz 类型
     * @return 返回true的场景为Boolean/Character/8种基本类型/枚举/字符串/数值/日期
     */
    private static boolean isJSONSimpleType(Class<?> clazz) {
        if (clazz == Boolean.class
                || clazz == Character.class
                || clazz.isPrimitive()
                || clazz.isEnum()
                || CharSequence.class.isAssignableFrom(clazz)
                || Number.class.isAssignableFrom(clazz)
                || Date.class.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    /**
     * 将一个Java对象转换为JSON对象
     * @param object Java对象
     * @return JSONObject
     */
    public static JSONObject toJSONObject(Object object) {
        if (object == null) {
            return null;
        }
        Class<?> clazz = object.getClass();
        // 无法处理简单类型
        if (isJSONSimpleType(clazz)) {
            throw new IllegalArgumentException("not support for type " + clazz);
        }
        // 无法处理数组
        if (clazz.isArray() || Collection.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("please call toJSONArray method for type " + clazz);
        }
        return (JSONObject) JSON.toJSON(object);
    }

    /**
     * 将一个Java对象转换为JSON数组
     * @param object Java对象
     * @return JSONArray
     */
    public static JSONArray toJSONArray(Object object) {
        if (object == null) {
            return null;
        }
        Class<?> clazz = object.getClass();
        // 无法处理简单类型
        if (isJSONSimpleType(clazz)) {
            throw new IllegalArgumentException("not support for type " + clazz);
        }
        // 只能处理数组或集合
        if (clazz.isArray() || Collection.class.isAssignableFrom(clazz)) {
            return (JSONArray) JSON.toJSON(object);
        }
        // 无法处理其它类型
        throw new IllegalArgumentException("please call toJSONObject method for type " + clazz);
    }
}
