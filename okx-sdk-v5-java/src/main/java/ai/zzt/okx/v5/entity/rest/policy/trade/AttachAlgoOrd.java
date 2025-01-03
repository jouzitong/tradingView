package ai.zzt.okx.v5.entity.rest.policy.trade;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import ai.zzt.okx.v5.enumeration.TriggerPxType;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AttachAlgoOrd implements Serializable {
    /**
     * 下单附带止盈止损时，客户自定义的策略订单ID，字母（区分大小写）与数字的组合，可以是纯字母、纯数字且长度要在1-32位之间。
     * 订单完全成交，下止盈止损委托单时，该值会传给algoClOrdId。
     */
    @JSONField(name = "attachAlgoClOrdId")
    @JsonProperty("attachAlgoClOrdId")
    private String attachAlgoClOrdId;

    /**
     * 止盈触发价，如果填写此参数，必须填写止盈委托价
     */
    @JSONField(name = "tpTriggerPx")
    @JsonProperty("tpTriggerPx")
    private BigDecimal tpTriggerPx;

    /**
     * 计划委托触发价格类型
     */
    @JSONField(name = "triggerPxType")
    @JsonProperty("triggerPxType")
    private TriggerPxType triggerPxType;

    /**
     * 止盈委托价，如果填写此参数，必须填写止盈触发价
     * 委托价格为-1时，执行市价止盈
     */
    @JSONField(name = "tpOrdPx")
    @JsonProperty("tpOrdPx")
    private BigDecimal tpOrdPx;

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
}
