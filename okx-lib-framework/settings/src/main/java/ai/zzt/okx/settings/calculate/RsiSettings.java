package ai.zzt.okx.settings.calculate;

import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.common.enums.TrendType;
import ai.zzt.okx.v5.enumeration.Bar;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2024/6/1
 **/
@Setter
@Getter
@ToString
@NoArgsConstructor
public class RsiSettings extends BaseCalculateSettings {

    /**
     * 计算周期. 默认 12. 常见周期为: 12, 16, 24
     */
    private int period = 16;

    /**
     * TODO 平滑线. 常见的线有: SMA, EMA, WMA
     */
    private String smoothingType = "SMA";

    /**
     * TODO 平滑长度 默认 17
     */
    private int smoothingLength = 17;

    /**
     * 超买界限. 默认 70
     */
    private BigDecimal overbought = BigDecimal.valueOf(90);

    /**
     * 超卖界限. 默认 30
     */
    private BigDecimal oversold = BigDecimal.valueOf(10);

    public RsiSettings(String id, InstrumentType instType) {
        super(id, instType);
    }

    public RsiSettings(String id, InstrumentType instType, Bar bar) {
        super(id, instType);
    }

    @Override
    public IndicatorType calculateType() {
        return IndicatorType.RSI;
    }

    public TrendType getTrendType(BigDecimal rsiIndex) {
        if (rsiIndex == null) {
            return TrendType.FLUCTUATE;
        }
        if (rsiIndex.compareTo(overbought) >= 0) {
            return TrendType.DOWN;
        } else if (rsiIndex.compareTo(oversold) <= 0) {
            return TrendType.UP;
        }
        return TrendType.FLUCTUATE;
    }

    public BigDecimal between() {
        return oversold.subtract(overbought).divide(BigDecimal.valueOf(2), 2, BigDecimal.ROUND_HALF_UP);
    }

}
