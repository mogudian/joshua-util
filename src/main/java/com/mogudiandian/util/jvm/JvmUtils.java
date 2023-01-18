package com.mogudiandian.util.jvm;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

/**
 * JVM工具类
 * @author sunbo
 */
public final class JvmUtils {

    private JvmUtils() {}

    /**
     * 主动垃圾回收
     */
    @Deprecated
    public static void gc() {
        Object obj = new Object();
        WeakReference<Object> ref = new WeakReference<>(obj);
        obj = null;
        while (ref.get() != null) {
            Runtime.getRuntime().gc();
            Runtime.getRuntime().runFinalization();
            if (ref.get() != null) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

}
