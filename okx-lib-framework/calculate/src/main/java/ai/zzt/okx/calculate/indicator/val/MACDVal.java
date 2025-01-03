package ai.zzt.okx.calculate.indicator.val;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2024-08-18
 **/
@Data
public class MACDVal implements IndicatorVal {

    /**
     * DIF（离差值）
     */
    private BigDecimal dif;

    /**
     * DEA（差离平均值）
     */
    private BigDecimal dea;

    /**
     * macd值（DIF-DEA）
     */
    private BigDecimal macd;

}
