package ai.zzt.okx.calculate.vo;

import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.common.utils.K;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhouzhitong
 * @since 2024-08-18
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndicatorVO {

    /**
     * 当前K线数据
     */
    private K k;

    /**
     * 指标数据
     */
    private Object[] indicators;

    public <T> T get(IndicatorType type) {
        return (T) indicators[type.getCode()];
    }

}
