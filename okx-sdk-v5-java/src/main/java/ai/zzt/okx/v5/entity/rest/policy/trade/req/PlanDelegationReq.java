package ai.zzt.okx.v5.entity.rest.policy.trade.req;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ai.zzt.okx.v5.entity.rest.policy.trade.AttachAlgoOrd;
import ai.zzt.okx.v5.enumeration.TriggerPxType;

import java.math.BigDecimal;
import java.util.List;

/**
 * 计划委托
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PlanDelegationReq extends PolicyDelegationOrderReq {
    /**
     * 计划委托触发价格
     */
    @NonNull
    @JSONField(name = "triggerPx")
    @JsonProperty("triggerPx")
    private BigDecimal triggerPx;

    /**
     * 委托价格
     * 委托价格为-1时，执行市价委托
     */
    @NonNull
    @JSONField(name = "orderPx")
    @JsonProperty("orderPx")
    private BigDecimal orderPx;

    /**
     * 计划委托触发价格类型
     */
    @JSONField(name = "triggerPxType")
    @JsonProperty("triggerPxType")
    private TriggerPxType triggerPxType;

    /**
     * 附带止盈止损信息
     * 适用于单币种保证金模式/跨币种保证金模式/组合保证金模式
     */
    @JSONField(name = "attachAlgoOrds")
    @JsonProperty("attachAlgoOrds")
    private List<AttachAlgoOrd> attachAlgoOrds;
}
