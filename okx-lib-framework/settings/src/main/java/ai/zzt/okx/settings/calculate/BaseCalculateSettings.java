package ai.zzt.okx.settings.calculate;

import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.common.base.settings.BaseSettings;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;

/**
 * 计算基础配置
 *
 * @author zhouzhitong
 * @since 2024/6/1
 **/
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class BaseCalculateSettings extends BaseSettings {

    /**
     * FIXME 权重
     */
    protected int weights = 80;

    public BaseCalculateSettings(String id, InstrumentType instType) {
        super(id);
    }

    /**
     * 计算类型
     *
     * @return 计算类型
     */
    public abstract IndicatorType calculateType();

}
