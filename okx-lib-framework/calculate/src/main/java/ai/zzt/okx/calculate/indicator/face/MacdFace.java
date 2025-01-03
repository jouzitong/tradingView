package ai.zzt.okx.calculate.indicator.face;

import ai.zzt.okx.calculate.indicator.MACD;
import ai.zzt.okx.calculate.indicator.face.base.BaseIndicatorFace;
import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.common.enums.MacdTrendType;
import ai.zzt.okx.common.enums.TrendType;
import ai.zzt.okx.common.utils.SlideList;
import ai.zzt.okx.common.utils.TrendUtils;
import ai.zzt.okx.settings.calculate.MacdSettings;
import ai.zzt.okx.settings.calculate.face.MacdSettingsFace;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhouzhitong
 * @since 2024/7/17
 **/
@NoArgsConstructor
public final class MacdFace extends BaseIndicatorFace<MacdSettingsFace> {

    /**
     * 分析结果
     */
    private final Map<Bar, SlideList<MacdTrendType>> trendResultMap = new HashMap<>();

    public MacdFace(MacdSettingsFace settings) {
        super(settings);
    }

    @Override
    public void add(MarkPrice mp) {
        super.add(mp);
        Collection<Bar> bars = settingsFace.getBars();
        for (Bar bar : bars) {
            MACD macd = get(bar);
            SlideList<BigDecimal> macdLine = macd.get();
            SlideList<MacdTrendType> trend = TrendUtils.convertTrendType(macdLine);
            trendResultMap.put(bar, trend);
        }
    }

    @NotNull
    @Override
    public TrendType getTrend(Bar bar) {
        if (!settingsFace.isEnable()) {
            return TrendType.FLUCTUATE;
        }
        if (!trendResultMap.containsKey(bar)) {
            return TrendType.FLUCTUATE;
        }
        SlideList<MacdTrendType> macdTrendTypes = trendResultMap.get(bar);
        MacdSettings settings = settingsFace.getSettings(bar);
        MacdTrendType macdTrendType;
        if (settings.isGoldenCrossLine()) {
            macdTrendType = MacdTrendType.fromValue(macdTrendTypes);
        } else {
            int lastPeriod = settings.getLastPeriod();
            int minCount = settings.getMinContinuityCount();
            int maxCount = settings.getMaxContinuityCount();
            macdTrendType = MacdTrendType.checkContinuityAndGetLast(macdTrendTypes, minCount, maxCount, lastPeriod);
        }
        return settings.getMacdTrendTypeMap().get(macdTrendType);
    }

    @Override
    public IndicatorType type() {
        return IndicatorType.MACD;
    }

    @Override
    public MACD createIndex(Bar bar) {
        MacdSettings settings = settingsFace.getSettings(bar);
        return new MACD(settings.getShortPeriod(), settings.getLongPeriod(), settings.getSignalPeriod(), bar);
    }
}
