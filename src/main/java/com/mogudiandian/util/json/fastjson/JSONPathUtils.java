package com.mogudiandian.util.json.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * JSONPath的工具类(基于fastjson)
 * @author sunbo
 */
public final class JSONPathUtils {

    private JSONPathUtils() {
        super();
    }

    /**
     * 解析JSONPath
     * @param json JSON
     * @param path 路径
     * @param clazz 需要解析成的类型
     * @param <T> 类型
     * @return 解析后的对象
     */
    public static <T> T parse(JSON json, String path, Class<T> clazz) {
        Object obj = JSONPath.eval(json, path);
        if (obj == null) {
            return null;
        }
        if (clazz == null) {
            return (T) obj;
        }
        return TypeUtils.cast(obj, clazz, null);
    }

}
