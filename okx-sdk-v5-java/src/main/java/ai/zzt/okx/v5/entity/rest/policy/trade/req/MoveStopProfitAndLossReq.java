package ai.zzt.okx.v5.entity.rest.policy.trade.req;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * 移动止盈止损
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MoveStopProfitAndLossReq extends PolicyDelegationOrderReq {
    /**
     * 回调幅度的比例，如 "0.05"代表"5%"
     * callbackRatio和callbackSpread只能传入一个
     */
    @JSONField(name = "callbackRatio")
    @JsonProperty("callbackRatio")
    private BigDecimal callbackRatio;

    /**
     * 回调幅度的价距
     */
    @JSONField(name = "callbackSpread")
    @JsonProperty("callbackSpread")
    private BigDecimal callbackSpread;

    /**
     * 激活价格
     * 激活价格是移动止盈止损的激活条件，当市场最新成交价达到或超过激活价格，委托被激活。激活后系统开始计算止盈止损的实际触发价格。如果不填写激活价格，即下单后就被激活。
     */
    @JSONField(name = "activePx")
    @JsonProperty("activePx")
    private BigDecimal activePx;

    /**
     * 是否只减仓，true 或 false，默认false
     * 该参数仅在 交割/永续 的买卖模式下有效，开平模式忽略此参数
     */
    @JSONField(name = "reduceOnly")
    @JsonProperty("reduceOnly")
    private Boolean reduceOnly;
}
