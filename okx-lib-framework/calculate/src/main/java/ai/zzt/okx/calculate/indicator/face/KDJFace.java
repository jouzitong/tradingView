package ai.zzt.okx.calculate.indicator.face;

import ai.zzt.okx.calculate.indicator.KDJ;
import ai.zzt.okx.calculate.indicator.face.base.BaseIndicatorFace;
import ai.zzt.okx.calculate.indicator.val.KDJVal;
import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.common.enums.TrendType;
import ai.zzt.okx.common.utils.SlideList;
import ai.zzt.okx.settings.calculate.KDJSettings;
import ai.zzt.okx.settings.calculate.face.KDJSettingsFace;
import ai.zzt.okx.settings.calculate.strategy.kdj.KDJJStrategy;
import ai.zzt.okx.settings.calculate.strategy.kdj.KDJOverStrategy;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2024/9/4
 **/
@NoArgsConstructor
public class KDJFace extends BaseIndicatorFace<KDJSettingsFace> {

    public KDJFace(KDJSettingsFace settings) {
        super(settings);
    }

    @Override
    public IndicatorType type() {
        return IndicatorType.KDJ;
    }

    @Override
    public KDJ createIndex(Bar bar) {
        KDJSettings settings = settingsFace.getSettings(bar);
        return new KDJ(settings.getPeriod(), bar, settings.getAlpha());
    }

    @NotNull
    @Override
    public TrendType getTrend(Bar bar) {
        if (!settingsFace.isEnable()) {
            return TrendType.FLUCTUATE;
        }
        KDJSettings settings = getSettingsFace().getSettings(bar);
        if (settings == null || settings.getWeights() <= 0) {
            return TrendType.FLUCTUATE;
        }

        // 执行策略
//        TrendType overStrategyTrend = overStrategy(settings.getOverStrategy(), bar);
//        if (overStrategyTrend != TrendType.FLUCTUATE) {
//            return overStrategyTrend;
//        }

        TrendType jStrategyTrend = jStrategy(settings.getJLineStrategy(), bar);
        if (jStrategyTrend != TrendType.FLUCTUATE) {
            return jStrategyTrend;
        }
        return TrendType.FLUCTUATE;
    }

    private TrendType overStrategy(KDJOverStrategy overStrategy, Bar bar) {
        if (overStrategy == null) {
            return TrendType.FLUCTUATE;
        }
        KDJSettings settings = getSettingsFace().getSettings(bar);
        if (settings == null || settings.getWeights() <= 0) {
            return TrendType.FLUCTUATE;
        }
        KDJ kdj = get(bar);
        if (kdj == null) {
            return TrendType.FLUCTUATE;
        }
        SlideList<KDJVal> vals = kdj.getVals();
        if (vals.size() <= 2) {
            return TrendType.FLUCTUATE;
        }

        KDJVal current = kdj.get();
        KDJVal prev = kdj.getSecLast();

        if (current.k.compareTo(current.d) > 0 && prev.k.compareTo(prev.d) <= 0 && current.k.compareTo(BigDecimal.valueOf(20)) < 0) {
            return TrendType.UP; // 黄金交叉且在超卖区
        } else if (current.k.compareTo(current.d) < 0 && prev.k.compareTo(prev.d) >= 0 && current.k.compareTo(BigDecimal.valueOf(80)) > 0) {
            return TrendType.DOWN; // 死亡交叉且在超买区
        }
        return TrendType.FLUCTUATE;
    }

    private TrendType jStrategy(KDJJStrategy jStrategy, Bar bar) {
        if (jStrategy == null || !jStrategy.isEnable()) {
            return TrendType.FLUCTUATE;
        }
        KDJ kdj = get(bar);
        BigDecimal j = kdj.get().j;
        BigDecimal oversold = jStrategy.getOversold();
        BigDecimal overbought = jStrategy.getOverbought();
        if (j.compareTo(oversold) < 0) {
            return TrendType.UP;
        } else if (j.compareTo(overbought) > 0) {
            return TrendType.DOWN;
        }
        return TrendType.FLUCTUATE;
    }

}
