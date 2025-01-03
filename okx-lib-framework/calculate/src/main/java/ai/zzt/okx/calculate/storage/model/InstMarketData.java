package ai.zzt.okx.calculate.storage.model;

import ai.zzt.okx.common.utils.K;
import ai.zzt.okx.common.utils.SlideList;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 产品市场数据
 *
 * @author zhouzhitong
 * @since 2024/12/1
 **/
@Data
@Slf4j
public class InstMarketData implements Serializable {

    private final String instId;

    private final int period;

    private final Map<Bar, KLine> kLineMap = new ConcurrentHashMap<>();

    private Bar minBar;

    public InstMarketData(String instId, int period) {
        this.instId = instId;
        this.period = period;
    }

    public void add(Bar bar, BigDecimal mp, long ts) {
        if (minBar == null) {
            minBar = bar;
        } else if (minBar.getScale() > bar.getScale()) {
            minBar = bar;
        }
        KLine kLine = kLineMap.get(bar);
        if (kLine == null) {
            kLine = new KLine(period, bar);
        }
        kLine.add(mp, ts);
    }

    public BigDecimal getLastMp() {
        if (kLineMap.isEmpty()) {
            log.warn("【未知异常】");
            return BigDecimal.ZERO;
        }
        return kLineMap.get(minBar).getLast();
    }

    public long getLastTs() {
        return kLineMap.get(minBar).getTs();
    }

    @Getter
    public static class KLine {

        private final int period;

        private final SlideList<K> kLines;

        private final Bar bar;

        public KLine(int period, Bar bar) {
            this.period = period;
            this.kLines = new SlideList<>(period);
            this.bar = bar;
        }

        public void add(BigDecimal mp, long ts) {
            if (kLines.isEmpty()) {
                kLines.add(new K(mp, bar, ts));
            } else if (!kLines.getLast().update(mp, ts)) {
                kLines.add(new K(mp, bar, ts));
            }
        }

        public BigDecimal getLast() {
            return kLines.getLast().get();
        }

        public long getTs() {
            return kLines.getLast().getCurTs();
        }

    }

}
