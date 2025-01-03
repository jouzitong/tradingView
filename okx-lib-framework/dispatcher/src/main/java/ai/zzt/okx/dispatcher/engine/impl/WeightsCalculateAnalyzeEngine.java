package ai.zzt.okx.dispatcher.engine.impl;

import ai.zzt.okx.calculate.context.AnalyzeContext;
import ai.zzt.okx.calculate.indicator.face.base.BaseIndicatorFace;
import ai.zzt.okx.common.enums.TrendType;
import ai.zzt.okx.okx_client.context.PositionContext;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权重实现策略
 * <p>
 * 止盈性平仓
 *
 * @author zhouzhitong
 * @since 2024/6/4
 **/
@Slf4j
@Service
//@ConditionalOnMissingBean(ICalculateAnalyzeEngine.class)
public class WeightsCalculateAnalyzeEngine extends BaseCalculateAnalyzeEngine {

    public WeightsCalculateAnalyzeEngine() {
        log.info("初始化, 默认计算分析器: {}", this.getClass().getSimpleName());
    }

    /**
     * 判断是否可以平仓
     *
     * @param analyzeContext  分析上下文
     * @param settingsContext 配置上下文
     * @param positionContext 仓位上下文
     */
    @Override
    protected void doAnalyzeClose(AnalyzeContext analyzeContext, SettingsContext settingsContext, PositionContext positionContext) {
        String instId = analyzeContext.getInstId();
        if (positionContext.getFace(instId).isEmpty()) {
            return;
        }
        List<BaseIndicatorFace> indicatorFaces = analyzeContext.getIndicatorFaces();
        Bar bar = settingsContext.getLocalBar();
        if (bar == null) {
            return;
        }

        TrendType[] trendTypes = new TrendType[indicatorFaces.size()];
        for (int i = 0; i < indicatorFaces.size(); i++) {
            trendTypes[i] = indicatorFaces.get(i).getLocalTrend(bar);
        }
        TrendType localTrend = TrendType.add(trendTypes);
        analyzeContext.setLocalTrend(localTrend);
    }

    /**
     * 分析趋势. 通过MACD和RSI分析结果, 判断当前趋势
     *
     * @param analyzeContext  分析上下文
     * @param settingsContext 配置上下文
     */
    @Override
    protected void doAnalyze(AnalyzeContext analyzeContext, SettingsContext settingsContext) {

        List<BaseIndicatorFace> indicatorFaces = analyzeContext.getIndicatorFaces();

        List<Bar> bars = settingsContext.getBars();
        int w = 0;
        for (Bar bar : bars) {
            for (BaseIndicatorFace indicatorFace : indicatorFaces) {
                TrendType trend;
                try {
                    trend = indicatorFace.getTrend(bar);
                } catch (Exception e) {
                    log.warn("分析趋势异常, bar: {}, indicatorFace: {}", bar, indicatorFace);
                    log.error("", e);
                    trend = TrendType.FLUCTUATE;
                }
                if (trend == TrendType.UP) {
                    w += indicatorFace.getSettingsFace().getSettings(bar).getWeights();
                } else if (trend == TrendType.DOWN) {
                    w -= indicatorFace.getSettingsFace().getSettings(bar).getWeights();
                }
            }
        }
        if (w >= settingsContext.getTargetWeight()) {
            analyzeContext.setAnalyzeTrend(TrendType.UP);
        } else if (-w >= settingsContext.getTargetWeight()) {
            analyzeContext.setAnalyzeTrend(TrendType.DOWN);
        }
    }

}
