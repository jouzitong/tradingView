package ai.zzt.okx.settings.order.profit;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 利润设置
 *
 * @author zhouzhitong
 * @since 2024-08-24
 **/
@Data
public class ProfitSettings {

    /**
     * 利润计算是否包括杠杆. 默认 false
     * <p>
     * 描述: 预期收益率是否把杠杆溢出的利润计算进去. 例如, 原本利润是 1%, 但是开了10倍杠杆, 这时的实际利润应该是10%.
     * 但是自己的预期收益, 指的包不包含杠杆溢出的利润, 是否同步计算到参数中.
     */
    private boolean leverProfit = false;

//    /**
//     * 预计收益率. 默认收益率为 2.3%
//     * <p>
//     * 描述: 预计收益指的是, 开仓到平仓的纯利润(手续费已经计算在内了, 实际收益会大于这个值, 用于抵消手续费), 但不包括杠杆.
//     */
//    @Deprecated
//    private BigDecimal profitRate = BigDecimal.valueOf(0.023);

    /**
     * 最大收益率. 默认收益率为 5%
     * <p>
     * 描述: 当达到 "最大收益率"时, 就会立即平仓.
     */
    private BigDecimal maxProfitRate = BigDecimal.valueOf(0.05);

    /**
     * [贪心]止盈止损配置
     */
    private GreedyOcoSettings greedyOco = new GreedyOcoSettings();

    /**
     * 预期最小收益率(不包括杠杆). 默认收益率为 0.3%,
     * <p>
     * 描述: 当达到 "预期最小收益率" 时, 就会下单条件单, 以保证最小收益.
     * <p>
     * FIXME 在实际使用过程中, 收益并不可观
     */
    private BigDecimal minProfitRate = BigDecimal.valueOf(0.003);

}
