package ai.zzt.okx.v5.entity.rest.policy.trade.req;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ai.zzt.okx.v5.enumeration.TpOrderKind;
import ai.zzt.okx.v5.enumeration.TriggerPxType;

import java.math.BigDecimal;

/**
 * 止盈止损
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class StopProfitAndLossReq extends PolicyDelegationOrderReq {
    /**
     * 止盈触发价，如果填写此参数，必须填写止盈委托价
     */
    @JSONField(name = "tpTriggerPx")
    @JsonProperty("tpTriggerPx")
    private BigDecimal tpTriggerPx;

    /**
     * 止盈触发价类型
     * 默认为last
     */
    @JSONField(name = "tpTriggerPxType")
    @JsonProperty("tpTriggerPxType")
    private TriggerPxType tpTriggerPxType;

    /**
     * 止盈委托价，如果填写此参数，必须填写止盈触发价
     * 委托价格为-1时，执行市价止盈
     */
    @JSONField(name = "tpOrdPx")
    @JsonProperty("tpOrdPx")
    private BigDecimal tpOrdPx;

    /**
     * 止盈订单类型
     * condition: 条件单
     * limit: 限价单
     * 默认为condition
     */
    @JSONField(name = "tpOrdKind")
    @JsonProperty("tpOrdKind")
    private TpOrderKind tpOrdKind;

    /**
     * 止损触发价，如果填写此参数，必须填写止损委托价
     */
    @JSONField(name = "slTriggerPx")
    @JsonProperty("slTriggerPx")
    private BigDecimal slTriggerPx;

    /**
     * 止损触发价类型
     */
    @JSONField(name = "slTriggerPxType")
    @JsonProperty("slTriggerPxType")
    private TriggerPxType slTriggerPxType;

    /**
     * 止损委托价，如果填写此参数，必须填写止损触发价
     * 委托价格为-1时，执行市价止损
     */
    @JSONField(name = "slOrdPx")
    @JsonProperty("slOrdPx")
    private BigDecimal slOrdPx;

    /**
     * 决定用户所下的止盈止损订单是否与该交易产品对应的仓位关联。若关联，仓位被撤销时，该止盈止损订单会被同时撤销；若不关联，仓位被撤销时，该止盈止损订单不受影响。
     * <p>
     * 有效值：
     * true：下单与仓位关联的止盈止损订单
     * false：下单与仓位不关联的止盈止损订单
     * <p>
     * 默认值为false。若传入true，用户必须同时传入 reduceOnly = true，说明当下单与仓位关联的止盈止损订单时，必须为只减仓。
     * <p>
     * 适用于单币种保证金模式、跨币种保证金模式。
     */
    @JSONField(name = "cxlOnClosePos")
    @JsonProperty("cxlOnClosePos")
    private Boolean cxlOnClosePos;

    /**
     * 是否只减仓，true 或 false，默认false
     * 仅适用于币币杠杆，以及买卖模式下的交割/永续
     * 仅适用于单币种保证金模式和跨币种保证金模式
     */
    @JSONField(name = "reduceOnly")
    @JsonProperty("reduceOnly")
    private Boolean reduceOnly;

}
