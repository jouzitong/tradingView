package ai.zzt.okx.common.utils;

import ai.zzt.okx.common.event.EventType;
import ai.zzt.okx.common.base.monitor.Monitor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


/**
 * 处理效率监控
 *
 * @author zhouzhitong
 * @since 2024/5/4
 **/
@Slf4j
public class EfficiencyMonitor implements Monitor {

    private static final SlideList<MonitorInfo> averageTime = new SlideList<>(10 * 10000);

    private static final ThreadLocal<MonitorInfo> startTime = new ThreadLocal<>();

    public static void start(Object data, EventType eventType) {
        MonitorInfo monitorInfo = new MonitorInfo(data, eventType, System.currentTimeMillis());
        startTime.set(monitorInfo);
    }

    /**
     * 结束监控
     *
     * @return 耗时. 单位: 毫秒
     */
    public static long end() {
        try {
            MonitorInfo monitorInfo = startTime.get();
            if (monitorInfo != null) {
                monitorInfo.setEndTime(System.currentTimeMillis());
                averageTime.add(monitorInfo);
                long costTime = monitorInfo.getCostTime();
//                log.trace("eventType: {}, costTime: {}ms.", monitorInfo.getEventType(), costTime);
                return monitorInfo.getCostTime();
            }
            return 0;
        } finally {
            startTime.remove();
        }
    }

    @Getter
    public static class MonitorInfo {

        private final Object data;

        private final EventType eventType;

        private final long startTime;

        @Setter
        private long endTime;


        public MonitorInfo(Object data, EventType eventType, long startTime) {
            this.data = data;
            this.eventType = eventType;
            this.startTime = startTime;
        }

        /**
         * 获取耗时
         *
         * @return 耗时
         */
        public long getCostTime() {
            return endTime - startTime;
        }

        @Override
        public String toString() {
            return "耗时: " + getCostTime() + "ms, 类型: " + eventType + ", 数据: " + data;
        }

    }

}
