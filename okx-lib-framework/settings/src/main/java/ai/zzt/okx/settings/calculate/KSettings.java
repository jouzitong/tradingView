package ai.zzt.okx.settings.calculate;

import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.settings.calculate.strategy.k.KLineLossStrategy;
import ai.zzt.okx.v5.enumeration.Bar;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhouzhitong
 * @since 2024/6/1
 **/
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class KSettings extends BaseCalculateSettings {

    /**
     * 计算周期. 默认 100
     */
    private int period = 100;

    /**
     * 超低低价策略
     */
    private KLineLossStrategy lossStrategy = new KLineLossStrategy();

    public KSettings(String id, InstrumentType instType) {
        super(id, instType);
    }

    public KSettings(String id, InstrumentType instType, Bar bar) {
        super(id, instType);
    }

    @Override
    public IndicatorType calculateType() {
        return IndicatorType.K_LINE;
    }

}
