package ai.zzt.okx.common.bean;

import ai.zzt.okx.common.context.SystemContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 时间间隔管理器
 *
 * @author zhouzhitong
 * @since 2024/11/5
 **/
public class Throttler {

    private final Map<Object, Long> messageTimestamps = new ConcurrentHashMap<>();

    /**
     * 检查消息是否可以发送
     *
     * @param id 消息ID
     * @return true 可以发送
     */
    public boolean check(Object id, long interval) {
        try {
            long currentTime = SystemContext.currentTimeMillis();
            // 获取该消息上次发送的时间
            Long lastSentTime = messageTimestamps.get(id);
            if (lastSentTime == null) {
                // 如果该消息从未发送过, 则记录当前时间并允许发送
                return true;
            }
            // 如果当前时间与上次发送时间的差值大于间隔时间, 则可以发送
            return currentTime - lastSentTime > interval;
        } finally {
            // 记录当前时间
            messageTimestamps.put(id, SystemContext.currentTimeMillis());
        }
    }

}
