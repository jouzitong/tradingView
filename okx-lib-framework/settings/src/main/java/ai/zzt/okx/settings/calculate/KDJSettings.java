package ai.zzt.okx.settings.calculate;

import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.settings.calculate.strategy.kdj.KDJDeviationStrategy;
import ai.zzt.okx.settings.calculate.strategy.kdj.KDJGoldenCrossStrategy;
import ai.zzt.okx.settings.calculate.strategy.kdj.KDJJStrategy;
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
public class KDJSettings extends BaseCalculateSettings {

    /**
     * 计算周期. 默认 16
     * <p>
     * 描述: 计算周期, 相当于一个滑动窗口的大小, 单位为``K线``.
     */
    private int period = 16;

    private int m1 = 3;

    private int m2 = 3;

    /**
     * 平滑系数
     */
    private BigDecimal alpha = BigDecimal.valueOf(2.0 / 3);

//    /**
//     * 超买/超卖区域策略
//     */
//    private KDJOverStrategy overStrategy;

    /**
     * 偏离值策略
     */
    private KDJDeviationStrategy deviationStrategy;

    /**
     * 交叉策略
     */
    private KDJGoldenCrossStrategy goldenCrossStrategy;

    /**
     * J线策略
     */
    private KDJJStrategy jLineStrategy;

//    @Deprecated
    public KDJSettings(String id, InstrumentType instType) {
        super(id, instType);
    }

    @Override
    public IndicatorType calculateType() {
        return IndicatorType.KDJ;
    }

    public void setjLineStrategy(KDJJStrategy jLineStrategy) {
        this.jLineStrategy = jLineStrategy;
    }

}
