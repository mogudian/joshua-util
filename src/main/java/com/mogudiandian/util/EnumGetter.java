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
 * @author sunbo
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

}
