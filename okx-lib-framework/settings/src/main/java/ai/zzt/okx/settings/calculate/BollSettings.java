package ai.zzt.okx.settings.calculate;

import ai.zzt.okx.common.enums.IndicatorType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;

import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2024/6/1
 **/
@Setter
@Getter
@ToString
@NoArgsConstructor
public class BollSettings extends BaseCalculateSettings {

    /**
     * 计算周期. 默认 16
     * <p>
     * 描述: 计算周期, 相当于一个滑动窗口的大小, 单位为``K线``.
     */
    private int period = 16;

    /**
     * 标准差. 默认 1
     */
    private Integer diffMinCount = 1;

    /**
     * 最小价格差. 默认 2%
     */
    private BigDecimal minDiffRate = BigDecimal.valueOf(0.02);

    /**
     * 回撤率【支撑点】. 默认 30%
     */
    private BigDecimal retraceRate = BigDecimal.valueOf(0.3);

    public BollSettings(String id, InstrumentType instType) {
        super(id, instType);
    }

    @Override
    public IndicatorType calculateType() {
        return IndicatorType.BOLL;
    }
}
