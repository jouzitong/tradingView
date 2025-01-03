package ai.zzt.okx.calculate.indicator.face;

import ai.zzt.okx.calculate.indicator.BOLL;
import ai.zzt.okx.calculate.indicator.face.base.BaseIndicatorFace;
import ai.zzt.okx.calculate.indicator.vo.Mp;
import ai.zzt.okx.calculate.vo.BOLLVO;
import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.common.enums.TrendType;
import ai.zzt.okx.settings.calculate.BollSettings;
import ai.zzt.okx.settings.calculate.face.BollSettingsFace;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author zhouzhitong
 * @since 2024/9/4
 **/
@NoArgsConstructor
public class BollFace extends BaseIndicatorFace<BollSettingsFace> {

    public BollFace(BollSettingsFace settings) {
        super(settings);
    }

    @Override
    public IndicatorType type() {
        return IndicatorType.BOLL;
    }

    @Override
    public BOLL createIndex(Bar bar) {
        BollSettings settings = settingsFace.getSettings(bar);
        return new BOLL(settings.getPeriod(), bar);
    }

    @NotNull
    @Override
    public TrendType getTrend(Bar bar) {
        if (!settingsFace.isEnable()) {
            return TrendType.FLUCTUATE;
        }
        BOLL boll = get(bar);
        if (boll == null) {
            return TrendType.FLUCTUATE;
        }
        BollSettings settings = settingsFace.getSettings(bar);
        BOLLVO bollVal = boll.get();
        BigDecimal curPrice = boll.getPrice();
        // 获取最值之间的差值
        BigDecimal diffVal = bollVal.getDiff();
        // 计算差值率: 差值/当前值
        BigDecimal bestDiffRate = diffVal.divide(curPrice, 4, RoundingMode.HALF_UP);

        if (settings.getMinDiffRate().compareTo(bestDiffRate) > 0) {
            return TrendType.FLUCTUATE;
        }
        // 计算上涨/下跌幅度: (最值 - 当前值) / 当前值
        BigDecimal diffRate = bollVal.getDiff().divide(curPrice, 4, RoundingMode.HALF_UP).abs();

        if (diffRate.compareTo(settings.getMinDiffRate()) < 0) {
            return TrendType.FLUCTUATE;
        }
        Mp lastVal = bollVal.getLast();

        if (lastVal.val.compareTo(curPrice) > 0) {
            return TrendType.UP;
        } else {
            return TrendType.DOWN;
        }
    }
}
