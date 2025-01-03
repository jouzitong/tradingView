package ai.zzt.okx.calculate.vo;

import ai.zzt.okx.common.base.settings.BaseSettings;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2024/8/6
 **/
@Data
public class StatisticsSettingsVO extends BaseSettings {

    /**
     * 最大时间(单位:ms)
     */
    public long maxTime = 1000L * 60 * 60 * 24;

    /**
     * 预期收益(单位:百分比). 默认: 1%
     */
    public BigDecimal profit = BigDecimal.valueOf(0.005);

    public StatisticsSettingsVO() {

    }

}
