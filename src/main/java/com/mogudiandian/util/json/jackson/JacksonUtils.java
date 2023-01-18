package com.mogudiandian.util.json.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.SneakyThrows;

import java.util.List;
import java.util.TimeZone;

/**
 * Jackson工具类
 *
 * @author sunbo
 */
public final class JacksonUtils {

    private JacksonUtils() {}

    /**
     * 将JSON解析为对象
     * @param jsonString JSON字符串
     * @param clazz 对象类型
     * @return 对象
     * @param <T> 对象的类型
     */
    @SneakyThrows
    public static <T> T parseObject(String jsonString, Class<T> clazz) {
        ObjectMapper objectMapper = getDeserializeObjectMapper();

        return objectMapper.readValue(jsonString, clazz);
    }

    /**
     * 将JSON解析为List
     * @param jsonString JSON字符串
     * @param clazz 对象类型
     * @return 对象集合
     * @param <T> 集合中对象的类型
     */
    @SneakyThrows
    public static <T> List<T> parseArray(String jsonString, Class<T> clazz) {
        ObjectMapper objectMapper = getDeserializeObjectMapper();

        CollectionType listType = objectMapper.getTypeFactory()
                                              .constructCollectionType(List.class, clazz);

        return objectMapper.readValue(jsonString, listType);
    }

    private static ObjectMapper getDeserializeObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 允许出现特殊字符和转义符
        objectMapper.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());
        // 遇到未知属性不要抛出异常
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 遇到忽略属性不要抛出异常
        objectMapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        // 时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        return objectMapper;
    }

    /**
     * 将对象序列化为字符串
     * @see JacksonUtils#toJSONString(Object, boolean)
     * @param obj 对象
     * @return 字符串
     */
    @SneakyThrows
    public static String toJSONString(Object obj) {
        return toJSONString(obj, false);
    }

    /**
     * 将对象序列化为字符串
     * @param obj 对象
     * @param prettyFormat 是否格式化
     * @return 字符串
     */
    @SneakyThrows
    public static String toJSONString(Object obj, boolean prettyFormat) {
        ObjectMapper objectMapper = new ObjectMapper();

        // 时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        if (prettyFormat) {
            // 格式化
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }

        // 枚举输出成字符串
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        // 属性为null时不序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return objectMapper.writeValueAsString(obj);
    }

}
