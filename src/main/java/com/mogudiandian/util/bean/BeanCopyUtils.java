package com.mogudiandian.util.bean;

import com.google.common.base.Objects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.cglib.beans.BeanCopier;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Bean属性拷贝的工具
 * @author Joshua Sun
 * @since 1.0.0
 */
public final class BeanCopyUtils {

    private static LoadingCache<ClassTuple, BeanCopier> cache;

    static {
        cache = CacheBuilder.newBuilder()
                            .maximumSize(1024)
                            .build(new CacheLoader<ClassTuple, BeanCopier>() {
                                @Override
                                public BeanCopier load(ClassTuple classTuple) {
                                    return BeanCopier.create(classTuple.sourceClass, classTuple.targetClass, false);
                                }
                            });
    }

    private BeanCopyUtils() {
        super();
    }

    /**
     * 拷贝属性
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyProperties(Object source, Object target) {
        java.util.Objects.requireNonNull(source);
        java.util.Objects.requireNonNull(target);
        try {
            BeanCopier beanCopier = cache.get(new ClassTuple(source.getClass(), target.getClass()));
            beanCopier.copy(source, target, null);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 拷贝属性并获取新对象
     * @param source 源对象
     * @param target 目标对象
     * @return A->B 返回B
     */
    public static <T> T copyPropertiesAndGet(Object source, T target) {
        copyProperties(source, target);
        return target;
    }

    /**
     * 拷贝属性并获取新对象
     * @param source 源对象
     * @param supplier 目标对象的获取方法
     * @return A->B 返回B
     */
    public static <T> T copyPropertiesAndGet(Object source, Supplier<T> supplier) {
        return copyPropertiesAndGet(source, supplier.get());
    }

    /**
     * 拷贝集合c1(x1, x2, ..., xn)到新集合c2(y1, y2, ..., yn)
     * @param source 原集合
     * @param collectionSupplier 新集合初始化方法
     * @param elementSupplier 新集合元素初始化方法
     * @return 新集合
     * @param <S> 原集合元素类型
     * @param <CS> 原集合类型
     * @param <T> 新集合元素类型
     * @param <CT> 新集合类型
     */
    public static <S, CS extends Collection<? extends S>, T, CT extends Collection<T>> CT copyCollection(CS source, Supplier<CT> collectionSupplier, Supplier<T> elementSupplier) {
        return source.stream()
                     .map(x -> copyPropertiesAndGet(x, elementSupplier))
                     .collect(Collectors.toCollection(collectionSupplier));
    }

    /**
     * 拷贝集合c1(x1, x2, ..., xn)到新List(y1, y2, ..., yn)
     * @param source 原集合
     * @param elementSupplier 新集合元素初始化方法
     * @return 新集合
     * @param <S> 原集合元素类型
     * @param <CS> 原集合类型
     * @param <T> 新集合元素类型
     */
    public static <S, CS extends Collection<? extends S>, T> List<T> copyList(CS source, Supplier<T> elementSupplier) {
        return copyCollection(source, ArrayList::new, elementSupplier);
    }

    /**
     * 拷贝集合c1(x1, x2, ..., xn)到新Set(y1, y2, ..., yn)
     * @param source 原集合
     * @param elementSupplier 新集合元素初始化方法
     * @return 新集合
     * @param <S> 原集合元素类型
     * @param <CS> 原集合类型
     * @param <T> 新集合元素类型
     */
    public static <S, CS extends Collection<? extends S>, T> Set<T> copySet(CS source, Supplier<T> elementSupplier) {
        return copyCollection(source, HashSet::new, elementSupplier);
    }

    /**
     * 两个类的元组
     * @author Joshua Sun
     */
    private static class ClassTuple {
        private Class<?> sourceClass;
        private Class<?> targetClass;

        public ClassTuple(Class<?> sourceClass, Class<?> targetClass) {
            this.sourceClass = sourceClass;
            this.targetClass = targetClass;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ClassTuple that = (ClassTuple) o;
            return Objects.equal(sourceClass, that.sourceClass) && Objects.equal(targetClass, that.targetClass);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(sourceClass, targetClass);
        }
    }

}
