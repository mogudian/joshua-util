package com.mogudiandian.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * 枚举工具类
 * @author Joshua Sun
 */
public final class EnumGetter {

    private static Table<Class<?>, Function<?, ?>, Map<?, Enum<?>>> cache;

    static {
        cache = HashBasedTable.create();
    }

    private EnumGetter() {
        super();
    }

    /**
     * 根据枚举类和属性获取枚举
     * @param enumClass 枚举类
     * @param getterMethodReference 属性Getter的Method-Reference
     * @param value 属性值
     * @param <E> 枚举类型
     * @param <P> 属性类型
     * @return 枚举
     */
    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>, P> E get(Class<E> enumClass, Function<E, P> getterMethodReference, P value) {
        Map<P, E> enumMap = (Map<P, E>) cache.get(enumClass, getterMethodReference);
        if (enumMap == null) {
            enumMap = EnumSet.allOf(enumClass).stream().collect(HashMap::new, (m, e) -> m.put(getterMethodReference.apply(e), e), Map::putAll);
            cache.put(enumClass, getterMethodReference, (Map<?, Enum<?>>) enumMap);
        }
        return enumMap.get(value);
    }

    /**
     * 根据枚举类和属性获取枚举
     * @param enumClass 枚举类
     * @param getterMethodReference 属性Getter的Method-Reference
     * @param value 属性值
     * @param defaultValue 默认值
     * @param <E> 枚举类型
     * @param <P> 属性类型
     * @return 枚举
     */
    public static <E extends Enum<E>, P> E getOrDefault(Class<E> enumClass, Function<E, P> getterMethodReference, P value, E defaultValue) {
        return Optional.ofNullable(get(enumClass, getterMethodReference, value)).orElse(defaultValue);
    }

    /**
     * 根据枚举的某一个属性(输入属性)和值，获取另一个属性(输出属性)的值
     * @param enumClass 枚举类
     * @param keyMethodReference 输入属性的Method-Reference
     * @param key 输入属性值
     * @param valueMethodReference 输出属性的Method-Reference
     * @return 输出属性的值 默认为空
     * @param <E> 枚举类型
     * @param <P> 输入属性类型
     * @param <R> 输出属性类型
     */
    public static <E extends Enum<E>, P, R> R getEnumPropertyValue(Class<E> enumClass, Function<E, P> keyMethodReference, P key, Function<E, R> valueMethodReference) {
        return getEnumPropertyValue(enumClass, keyMethodReference, key, valueMethodReference, null);
    }

    /**
     * 根据枚举的某一个属性(输入属性)和值，获取另一个属性(输出属性)的值
     * @param enumClass 枚举类
     * @param keyMethodReference 输入属性的Method-Reference
     * @param key 输入属性值
     * @param valueMethodReference 输出属性的Method-Reference
     * @param defaultValue 默认输出属性值
     * @return 输出属性的值
     * @param <E> 枚举类型
     * @param <P> 输入属性类型
     * @param <R> 输出属性类型
     */
    public static <E extends Enum<E>, P, R> R getEnumPropertyValue(Class<E> enumClass, Function<E, P> keyMethodReference, P key, Function<E, R> valueMethodReference, R defaultValue) {
        return Optional.ofNullable(key)
                       .map(x -> EnumGetter.get(enumClass, keyMethodReference, x))
                       .map(valueMethodReference)
                       .orElse(defaultValue);
    }

}
