package ai.zzt.okx.calculate.indicator.face;

import ai.zzt.okx.calculate.indicator.KLine;
import ai.zzt.okx.calculate.indicator.face.base.BaseIndicatorFace;
import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.common.enums.TrendType;
import ai.zzt.okx.common.utils.K;
import ai.zzt.okx.common.utils.SlideList;
import ai.zzt.okx.settings.calculate.KSettings;
import ai.zzt.okx.settings.calculate.face.KSettingsFace;
import ai.zzt.okx.settings.calculate.strategy.k.KLineLossStrategy;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author zhouzhitong
 * @since 2024/7/17
 **/
@NoArgsConstructor
public final class KLineFace extends BaseIndicatorFace<KSettingsFace> {

    public KLineFace(KSettingsFace settings) {
        super(settings);
    }

    @NotNull
    @Override
    public TrendType getTrend(Bar bar) {
        if (!this.settingsFace.isEnable()) {
            return TrendType.FLUCTUATE;
        }
        KSettings settings = this.settingsFace.getSettings(bar);
        KLine kline = get(bar);
        if (kline == null) {
            return TrendType.FLUCTUATE;
        }
        SlideList<K> ks = kline.get();
        if (ks == null || ks.isEmpty()) {
            return TrendType.FLUCTUATE;
        }

        KLineLossStrategy lossStrategy = settings.getLossStrategy();
        if (lossStrategy != null) {
            K k = ks.getLast();
            BigDecimal h;
            if (ks.size() >= 2) {
                K secK = ks.getSecLast();
                h = secK.getH();
            } else {
                h = k.getH();
            }

            BigDecimal l = k.getL();
            BigDecimal o = k.getO();
            BigDecimal c = k.getC();

            // TODO 涨跌都算进去了, 不应该算进去的.

            // 最大波动率 = (最高价 - 当前价)/ 开盘价
            BigDecimal maxDiff = h.subtract(c).divide(o, 4, RoundingMode.HALF_DOWN);
            // 回撤率 = (当前价 - 最低价)/ 开盘价
            BigDecimal retraceRate = c.subtract(l).divide(o, 4, RoundingMode.HALF_DOWN);

            if (lossStrategy.getMinDiffRate().compareTo(maxDiff) < 0
                // FIXME 这个回撤需要更加细粒度的控制,
//                && lossStrategy.getReboundRate().compareTo(retraceRate) < 0
            ) {
                return TrendType.UP;
            }
        }


        return TrendType.FLUCTUATE;
    }

    @Override
    public IndicatorType type() {
        return IndicatorType.K_LINE;
    }

    @Override
    public KLine createIndex(Bar bar) {
        KSettings settings = settingsFace.getSettings(bar);
        if (settings == null) {
            // FIXME 临时这么写的
            settings = new KSettings(settingsFace.getId(), null);
        }
        return new KLine(settings.getPeriod(), bar);
    }

}
