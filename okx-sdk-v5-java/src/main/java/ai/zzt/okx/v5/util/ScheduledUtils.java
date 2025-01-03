package ai.zzt.okx.v5.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Jou Ziton
 * @since 2024/5/21
 **/
public class ScheduledUtils implements AutoCloseable {

    public static final String HEART_KEY = "heartBeat";

    public static final String RECONNECT_KEY = "reconnect";

    private static final ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(1);

    private static final Map<String, ScheduledFuture<?>> scheduledFutureMap = new ConcurrentHashMap<>();

    public static void schedule(String key, Runnable runnable, long delay, TimeUnit timeUnit) {
        ScheduledFuture<?> schedule = scheduledExecutor.schedule(runnable, delay, timeUnit);
        scheduledFutureMap.put(key, schedule);
    }

    public static void schedule(String key, Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        ScheduledFuture<?> scheduledFuture = scheduledExecutor.scheduleAtFixedRate(runnable, delay, period, timeUnit);
        scheduledFutureMap.put(key, scheduledFuture);
    }

    public static boolean closeTask(String key) {
        if (scheduledFutureMap.containsKey(key)) {
            return scheduledFutureMap.get(key).cancel(false);
        }
        return false;
    }

    public static void shutdown() {
        scheduledExecutor.shutdown();
    }

    @Override
    public void close() throws Exception {
        shutdown();
    }
}
