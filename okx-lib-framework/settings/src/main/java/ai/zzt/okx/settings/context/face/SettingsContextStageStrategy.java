package ai.zzt.okx.settings.context.face;

import ai.zzt.okx.common.context.SystemContext;
import ai.zzt.okx.common.utils.vo.DateTimeInterval;
import ai.zzt.okx.common.utils.Pair;
import ai.zzt.okx.settings.context.SettingsContext;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 配置上下文 --- 阶段性策略
 * <p>
 * TODO 目前 end时间字段没有用到, 后续需要考虑是否需要删除
 *
 * @author zhouzhitong
 * @since 2024-08-19
 **/
public class SettingsContextStageStrategy {

    private final PriorityQueue<Pair<DateTimeInterval, SettingsContext>> settingsQueue =
            new PriorityQueue<>(Comparator.comparingLong((a) -> a.getFirst().getStart()));

    public void addSettings(DateTimeInterval interval, SettingsContext settings) {
        settingsQueue.add(Pair.of(interval, settings));
    }

    public SettingsContext get() {
        return get(SystemContext.currentTimeMillis());
    }

    public SettingsContext get(long ts) {
        if (settingsQueue.isEmpty()) {
            return null;
        }
        if (settingsQueue.size() == 1) {
            return settingsQueue.peek().getSecond();
        }
        Pair<DateTimeInterval, SettingsContext> cur = settingsQueue.peek();
        while (settingsQueue.peek().getFirst().getStart() <= ts) {
            if (settingsQueue.size() == 1) {
                return settingsQueue.peek().getSecond();
            }
            if (settingsQueue.peek().getFirst().getStart() == ts) {
                return settingsQueue.peek().getSecond();
            }
            cur = settingsQueue.poll();
        }
        return cur.getSecond();
    }


}
