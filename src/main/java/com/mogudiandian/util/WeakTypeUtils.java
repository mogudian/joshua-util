package com.mogudiandian.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 模拟弱类型的工具类
 *
 * @author Joshua Sun
 * @since 2023/6/15
 */
public final class WeakTypeUtils {

    private WeakTypeUtils() {}

    /**
     * 将对象转换为boolean
     * 仿照JS的规则
     * null -> false
     * false -> false
     * true -> true
     * 0 -> false
     * !0 -> true
     * "" -> false
     * !"" -> true
     * object -> true
     * array -> true
     * @param obj 对象
     * @return 布尔
     */
    public static boolean toBoolean(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue() != 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() > 0;
        }
        return true;
    }

    /**
     * 或 仿照JS 返回第一个为真的对象
     * @param obj1 对象1
     * @param obj2 对象2
     * @param objs 对象3...n
     * @return 第一个为真的对象 如果都为假则返回最后一个对象
     * @param <T> 对象类型
     */
    public static <T> T or(T obj1, T obj2, T... objs) {
        Stream<T> stream = Stream.of(obj1, obj2);
        if (objs != null && objs.length > 0) {
            stream = Stream.concat(stream, Arrays.stream(objs));
        }
        return or(stream);
    }

    /**
     * 或 仿照JS 返回第一个为真的对象
     * @param supplier1 对象提供者1
     * @param supplier2 对象提供者2
     * @param suppliers 对象提供者3...n
     * @return 第一个为真的对象 如果都为假则返回最后一个对象
     * @param <T> 对象类型
     */
    public static <T> T or(Supplier<T> supplier1, Supplier<T> supplier2, Supplier<T>... suppliers) {
        Stream<Supplier<T>> stream = Stream.of(supplier1, supplier2);
        if (suppliers != null && suppliers.length > 0) {
            stream = Stream.concat(stream, Arrays.stream(suppliers));
        }
        return or(stream.map(Supplier::get));
    }

    /**
     * 或 仿照JS 返回第一个为真的对象
     * @param stream 流
     * @return 第一个为真的对象 如果都为假则返回最后一个对象
     * @param <T> 对象类型
     */
    private static <T> T or(Stream<T> stream) {
        T last = null;
        for (Iterator<T> iterator = stream.iterator(); iterator.hasNext(); ) {
            last = iterator.next();
            // 只要有true就返回
            if (toBoolean(last)) {
                break;
            }
        }
        return last;
    }

    /**
     * 且 仿照JS 返回第一个为假的对象
     * @param obj1 对象1
     * @param obj2 对象2
     * @param objs 对象3...n
     * @return 第一个为假的对象 如果都为真则返回最后一个对象
     * @param <T> 对象类型
     */
    public static <T> T and(T obj1, T obj2, T... objs) {
        Stream<T> stream = Stream.of(obj1, obj2);
        if (objs != null && objs.length > 0) {
            stream = Stream.concat(stream, Arrays.stream(objs));
        }
        return and(stream);
    }

    /**
     * 且 仿照JS 返回第一个为假的对象
     * @param supplier1 对象提供者1
     * @param supplier2 对象提供者2
     * @param suppliers 对象提供者3...n
     * @return 第一个为假的对象 如果都为真则返回最后一个对象
     * @param <T> 对象类型
     */
    public static <T> T and(Supplier<T> supplier1, Supplier<T> supplier2, Supplier<T>... suppliers) {
        Stream<Supplier<T>> stream = Stream.of(supplier1, supplier2);
        if (suppliers != null && suppliers.length > 0) {
            stream = Stream.concat(stream, Arrays.stream(suppliers));
        }
        return and(stream.map(Supplier::get));
    }

    /**
     * 且 仿照JS 返回第一个为假的对象
     * @param stream 流
     * @return 第一个为假的对象 如果都为真则返回最后一个对象
     * @param <T> 对象类型
     */
    private static <T> T and(Stream<T> stream) {
        T last = null;
        for (Iterator<T> iterator = stream.iterator(); iterator.hasNext(); ) {
            last = iterator.next();
            // 只要有false就返回
            if (!toBoolean(last)) {
                break;
            }
        }
        return last;
    }

    /**
     * 如果条件为真则执行
     * @param supplier 要判断的对象提供者
     * @param predicate 判断条件
     * @param consumer 为真的消费者
     * @param <T> 提供对象的类型
     */
    public static <T> void ifThen(Supplier<T> supplier, Predicate<T> predicate, Consumer<T> consumer) {
        T t = supplier.get();
        if (predicate.test(t)) {
            consumer.accept(t);
        }
    }

    /**
     * 如果条件为真则执行
     * @param t 要判断的对象
     * @param predicate 判断条件
     * @param runnable 为真执行的动作
     * @param <T> 对象的类型
     */
    public static <T> void ifThen(T t, Predicate<T> predicate, Runnable runnable) {
        ifThen(predicate.test(t), runnable);
    }

    /**
     * 如果条件为真则执行
     * @param supplier 要判断的对象提供者
     * @param runnable 为真执行的动作
     */
    public static void ifThen(Supplier<Boolean> supplier, Runnable runnable) {
        ifThen(supplier.get(), runnable);
    }

    /**
     * 如果条件为真则执行
     * @param object 要判断的对象
     * @param runnable 为真执行的动作
     */
    public static <T> void ifThen(Object object, Runnable runnable) {
        ifThen(toBoolean(object), runnable);
    }

    /**
     * 如果条件为真则执行
     * @param b 是否为真
     * @param runnable 为真执行的动作
     */
    public static <T> void ifThen(boolean b, Runnable runnable) {
        if (b) {
            runnable.run();
        }
    }

    /**
     * 如果条件为真则执行
     * @param supplier 要判断的对象提供者
     * @param predicate 判断条件
     * @param tConsumer 为真的消费者
     * @param fConsumer 为假的消费者
     * @param <T> 对象的类型
     */
    public static <T> void ifElse(Supplier<T> supplier, Predicate<T> predicate, Consumer<T> tConsumer, Consumer<T> fConsumer) {
        T t = supplier.get();
        if (predicate.test(t)) {
            tConsumer.accept(t);
        } else {
            fConsumer.accept(t);
        }
    }

    /**
     * 如果条件为真则执行
     * @param t 要判断的对象
     * @param predicate 判断条件
     * @param tRunnable 为真执行的动作
     * @param fRunnable 为假执行的动作
     * @param <T> 对象的类型
     */
    public static <T> void ifElse(T t, Predicate<T> predicate, Runnable tRunnable, Runnable fRunnable) {
        ifElse(predicate.test(t), tRunnable, fRunnable);
    }

    /**
     * 如果条件为真则执行
     * @param supplier 要判断的对象提供者
     * @param tRunnable 为真执行的动作
     * @param fRunnable 为假执行的动作
     */
    public static void ifElse(Supplier<Boolean> supplier, Runnable tRunnable, Runnable fRunnable) {
        ifElse(supplier.get(), tRunnable, fRunnable);
    }

    /**
     * 如果条件为真则执行
     * @param object 要判断的对象
     * @param tRunnable 为真执行的动作
     * @param fRunnable 为假执行的动作
     */
    public static <T> void ifElse(Object object, Runnable tRunnable, Runnable fRunnable) {
        ifElse(toBoolean(object), tRunnable, fRunnable);
    }

    /**
     * 如果条件为真则执行
     * @param b 是否为真
     * @param tRunnable 为真执行的动作
     * @param fRunnable 为假执行的动作
     */
    public static <T> void ifElse(boolean b, Runnable tRunnable, Runnable fRunnable) {
        if (b) {
            tRunnable.run();
        } else {
            fRunnable.run();
        }
    }

}
