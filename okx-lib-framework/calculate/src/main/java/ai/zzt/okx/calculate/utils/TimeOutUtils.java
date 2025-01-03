package ai.zzt.okx.calculate.utils;

import ai.zzt.okx.common.context.SystemContext;

/**
 * @author zhouzhitong
 * @since 2024/5/16
 **/
public class TimeOutUtils {

    private static final ThreadLocal<Long> threadLocalStartTime = new ThreadLocal<>();

    public static void start() {
        threadLocalStartTime.set(SystemContext.currentTimeMillis());
    }

    public static long getStartTime() {
        return threadLocalStartTime.get();
    }

    public static long cost() {
        return SystemContext.currentTimeMillis() - threadLocalStartTime.get();
    }

    public static void clear() {
        threadLocalStartTime.remove();
    }

}
