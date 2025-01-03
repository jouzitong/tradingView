package ai.zzt.okx.calculate.vo;

import ai.zzt.okx.common.enums.TrendType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2024/8/6
 **/
@Data
public class StatisticsVO {

    /**
     * 币种
     */
    public String instId;

    /**
     * 趋势类型
     */
    public TrendType trendType;

    /**
     * 开始时间
     */
    public BigDecimal price;

    /**
     * 结束时间
     */
    public long startTime;

    /**
     * 最高利润(百分比)
     */
    public BigDecimal maxProfit = BigDecimal.ZERO;

    /**
     * 是否结束
     */
    public boolean end = false;

}
