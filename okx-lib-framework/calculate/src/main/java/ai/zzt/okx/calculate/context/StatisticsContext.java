package ai.zzt.okx.calculate.context;

import ai.zzt.okx.calculate.vo.StatisticsSettingsVO;
import ai.zzt.okx.calculate.vo.StatisticsVO;
import ai.zzt.okx.common.enums.TrendType;
import jakarta.annotation.Nullable;
import lombok.Data;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 准确率统计.
 *
 * @author zhouzhitong
 * @since 2024/8/7
 **/
@Data
public class StatisticsContext implements Serializable {

    /**
     * 统计配置
     */
    private StatisticsSettingsVO settings = new StatisticsSettingsVO();

    private final Map<String, StatisticsVO> map = new ConcurrentHashMap<>();

    private final AtomicLong rightCount = new AtomicLong(0L);

    private final AtomicLong errorCount = new AtomicLong(0L);

    public void handle(MarkPrice mp, TrendType analyzeTrend) {
        String instId = mp.getInstId();
        if (!map.containsKey(instId) && analyzeTrend == TrendType.FLUCTUATE) {
            return;
        }
        StatisticsVO statistics = map.get(instId);

        if (statistics != null) {
            if (doAnalyze(mp, statistics)) {
                statistics.end = true;
            }

            TrendType lastTrend = statistics.trendType;
            // 如果上次分析的趋势和当前趋势不同, 且当前时间超过最大时间, 则开始新的分析
            if (analyzeTrend != TrendType.FLUCTUATE && lastTrend != analyzeTrend && isTimeOut(statistics, mp.getTs())) {
                StatisticsVO statisticsVO = new StatisticsVO();
                statisticsVO.setInstId(instId);
                statisticsVO.setTrendType(analyzeTrend);
                statisticsVO.setStartTime(mp.getTs());
                statisticsVO.setPrice(mp.getMarkPx());
                map.put(instId, statisticsVO);
            }
        } else {
            StatisticsVO statisticsVO = new StatisticsVO();
            statisticsVO.setInstId(instId);
            statisticsVO.setTrendType(analyzeTrend);
            statisticsVO.setStartTime(mp.getTs());
            statisticsVO.setPrice(mp.getMarkPx());
            map.put(instId, statisticsVO);
        }
    }

    // 开始分析
    protected boolean doAnalyze(MarkPrice mp, @Nullable StatisticsVO statistics) {
        if (statistics == null || statistics.isEnd() || statistics.startTime == mp.getTs()) {
            return false;
        }
        // 计算利润(%): (当前价 - 开仓价)/开仓价
        BigDecimal profit = mp.getMarkPx().subtract(statistics.price)
                .divide(statistics.price, 4, RoundingMode.HALF_UP);
        if (statistics.trendType == TrendType.DOWN) {
            profit = profit.abs();
        }
        statistics.maxProfit = profit.compareTo(statistics.maxProfit) > 0 ? profit : statistics.maxProfit;
        if (profit.compareTo(settings.profit) >= 0) {
            rightCount.incrementAndGet();
            return true;
        }
        if (isTimeOut(statistics, mp.getTs())) {
            errorCount.incrementAndGet();
            return true;
        }
        return false;
    }

    private boolean isTimeOut(StatisticsVO statistics, long ts) {
        return ts - statistics.startTime > settings.maxTime;
    }

}
