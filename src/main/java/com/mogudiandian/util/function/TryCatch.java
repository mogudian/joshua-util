package com.mogudiandian.util.function;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * try-catch 执行的工具类
 * 用于可忽略的异常
 *
 * @author Joshua Sun
 * @since 1.0.13
 */
public final class TryCatch {

    private TryCatch() {}

    /**
     * try-catch 执行并返回
     * @param tryGetter try执行的函数
     * @return try执行成功，则返回try执行的结果，否则返回null
     * @param <T> 返回值类型
     */
    public static <T> T tryGet(Supplier<T> tryGetter) {
        return tryGet(tryGetter, null);
    }

    /**
     * try-catch 执行并返回
     * @param tryGetter try执行的函数
     * @param defaultValue 如果try执行失败，则返回该值
     * @return try执行成功，则返回try执行的结果，否则返回defaultValue
     * @param <T> 返回值类型
     */
    public static <T> T tryGet(Supplier<T> tryGetter, T defaultValue) {
        return tryGet(tryGetter, null, defaultValue);
    }

    /**
     * try-catch 执行并返回
     * @param tryGetter try执行的函数
     * @param catcher catch执行的函数
     * @param defaultValue 如果try执行失败，则返回该值
     * @return try执行成功，则返回try执行的结果，否则返回defaultValue
     * @param <T> 返回值类型
     */
    public static <T> T tryGet(Supplier<T> tryGetter, Consumer<Exception> catcher, T defaultValue) {
        try {
            return tryGetter.get();
        } catch (Exception e) {
            if (catcher != null) {
                catcher.accept(e);
            }
            return defaultValue;
        }
    }

    /**
     * try-catch 执行并返回
     * @param tryGetter try执行的函数
     * @param catchGetter catch执行的函数
     * @return try执行成功，则返回try执行的结果，否则返回catch执行的结果
     * @param <T> 返回值类型
     */
    public static <T> T tryCatchGet(Supplier<T> tryGetter, Supplier<T> catchGetter) {
        try {
            return tryGetter.get();
        } catch (Exception e) {
            return catchGetter.get();
        }
    }

    /**
     * try-catch 执行并返回
     * @param tryGetter try执行的函数
     * @param catchGetter catch执行的函数
     * @return try执行成功，则返回try执行的结果，否则返回catch执行的结果
     * @param <T> 返回值类型
     */
    public static <T> T tryCatchGet(Supplier<T> tryGetter, Function<Exception, T> catchGetter) {
        try {
            return tryGetter.get();
        } catch (Exception e) {
            return catchGetter.apply(e);
        }
    }

}
