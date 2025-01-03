package ai.zzt.okx.calculate.indicator.face;

import ai.zzt.okx.calculate.indicator.RSI;
import ai.zzt.okx.calculate.indicator.face.base.BaseIndicatorFace;
import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.common.enums.TrendType;
import ai.zzt.okx.settings.calculate.RsiSettings;
import ai.zzt.okx.settings.calculate.face.RsiSettingsFace;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2024/7/17
 **/
@NoArgsConstructor
public final class RsiFace extends BaseIndicatorFace<RsiSettingsFace> {

    public RsiFace(RsiSettingsFace settings) {
        super(settings);
    }

    @NotNull
    @Override
    public TrendType getTrend(Bar bar) {
        if (!settingsFace.isEnable()) {
            return TrendType.FLUCTUATE;
        }
        RSI rsi = get(bar);
        if (rsi == null) {
            return TrendType.FLUCTUATE;
        }

        BigDecimal rsiIndex = rsi.get();
        if (rsiIndex == null) {
            return TrendType.FLUCTUATE;
        }
        RsiSettings settings = this.settingsFace.getSettings(bar);
        BigDecimal oversold = settings.getOversold();
        BigDecimal overbought = settings.getOverbought();
        if (rsiIndex.compareTo(overbought) >= 0) {
            return TrendType.DOWN;
        } else if (rsiIndex.compareTo(oversold) <= 0) {
            return TrendType.UP;
        }
        return TrendType.FLUCTUATE;
    }

    @Override
    public IndicatorType type() {
        return IndicatorType.RSI;
    }

    @Override
    public RSI createIndex(Bar bar) {
        RsiSettings settings = this.settingsFace.getSettings(bar);
        if (settings == null) {
            // FIXME 临时这么写的
            settings = new RsiSettings(settingsFace.getId(), null);
        }
        return new RSI(settings.getPeriod(), bar);
    }
}
