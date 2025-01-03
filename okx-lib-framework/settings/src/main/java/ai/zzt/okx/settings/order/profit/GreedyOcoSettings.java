package ai.zzt.okx.settings.order.profit;

import ai.zzt.okx.common.base.settings.OrderSettings;
import lombok.Data;

import java.math.BigDecimal;

/**
 * [贪心]移动止盈止损配置
 *
 * @author zhouzhitong
 * @since 2024-08-24
 **/
@Data
public class GreedyOcoSettings implements OrderSettings {

    /**
     * TODO 是否启用, 目前还没解决 欧易接口对接止盈止损问题
     */
    private boolean enable = false;

    /**
     * (贪心)止盈止损收益率(不包括杠杆). 默认收益率为 0.005%
     * <p>
     * 描述: 当收益率达到预期收益{@link ProfitSettings#getMinProfitRate() MinProfitRate}时, 会进行止盈止损,
     * 止盈止损的收益率是[当前收益率 - (贪心)止盈止损收益率]（并且保证收益率大于最小收益率）
     */
    private BigDecimal greedyOcoRate = BigDecimal.valueOf(0.005);

    /**
     * (贪心)止盈止损收益率(不包括杠杆) 触发条件值. 默认 0.002%
     * <p>
     * 描述: 在第一次触发(贪心)止盈止损之后, 每当收益率涨幅大于【此值】时, 就会重新触发(贪心)止盈止损. 不会出现上次比这次收益小的情况
     */
    private BigDecimal greedyOcoRateTrigger = BigDecimal.valueOf(0.002);

    /**
     * 最多触发次数. 默认 4 次.
     * <p>
     * 描述: 触发(贪心)止盈止损的次数, 超过此次数后, 则不会再触发. 注意第一次触发不算在这之内
     */
    private Integer maxTriggerCount = 4;

}
