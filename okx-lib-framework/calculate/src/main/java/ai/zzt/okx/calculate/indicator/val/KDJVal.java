package ai.zzt.okx.calculate.indicator.val;

import ai.zzt.okx.common.utils.BigDecimalUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2024/12/1
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KDJVal implements IndicatorVal {

    public BigDecimal k;

    public BigDecimal d;

    public BigDecimal j;

    @Override
    public String toString() {
        int scale = 2;
        return "KDJ" +
                "K = " + BigDecimalUtils.scale(k, scale) +
                "D = " + BigDecimalUtils.scale(d, scale) +
                "J = " + BigDecimalUtils.scale(j, scale)
                ;
    }

}
