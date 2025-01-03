package ai.zzt.okx.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouzhitong
 * @since 2024/4/24
 **/
@Slf4j
public class ThreadUtils {

    private static final ExecutorService calculateExecutor;

    private static final ExecutorService ioExecutor;

    private static final ExecutorService taskExecutor;

    // 定时任务线程池
    private static final ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(3);

    static {
        calculateExecutor = Executors.newFixedThreadPool(8);
        ioExecutor = Executors.newFixedThreadPool(5);
        taskExecutor = Executors.newFixedThreadPool(8);
    }

    public static void runCalculate(Runnable runnable) {
        calculateExecutor.submit(runnable);
    }

    public static void runIO(Runnable runnable) {
        ioExecutor.submit(runnable);
    }

    public static void runTask(Runnable task) {
        taskExecutor.submit(task);
    }

    public static <T> Future<T> callTask(Callable<T> callable) {
        return taskExecutor.submit(callable);
    }

    public static ScheduledFuture<?> submitScheduled(Runnable runnable, long initDelay, long delay, TimeUnit unit) {
        return scheduledExecutor.scheduleAtFixedRate(runnable, initDelay, delay, unit);
//        return scheduledExecutor.scheduleWithFixedDelay(runnable, initDelay, delay, unit);
    }

    public static void sleep(long seconds) {
        sleep(seconds, TimeUnit.SECONDS);
    }

    /**
     * 线程休眠
     *
     * @param time 休眠时间
     * @param unit 时间单位
     */
    public static void sleep(long time, TimeUnit unit) {
        try {
            Thread.sleep(unit.toMillis(time));
        } catch (InterruptedException e) {
            log.warn("Thread sleep interrupted.");
        }
    }

}
