package ai.zzt.okx.v5.entity.rest.policy.trade.req;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ai.zzt.okx.v5.entity.rest.IOkxRestReq;
import ai.zzt.okx.v5.enumeration.TriggerPxType;

import java.math.BigDecimal;

/**
 * 修改策略委托订单
 */
@Data
@SuperBuilder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AmendAlgoReq implements IOkxRestReq {
    /**
     * 产品ID
     */
    @NonNull
    @JSONField(name = "instId")
    @JsonProperty("instId")
    private String instId;

    /**
     * 策略委托单ID
     * algoId和algoClOrdId必须传一个，若传两个，以algoId为主
     */
    @JSONField(name = "algoId")
    @JsonProperty("algoId")
    private String algoId;

    /**
     * 客户自定义策略订单ID
     */
    @JSONField(name = "algoClOrdId")
    @JsonProperty("algoClOrdId")
    private String algoClOrdId;

    /**
     * 当订单修改失败时，该订单是否需要自动撤销。默认为false
     * false：不自动撤单
     * true：自动撤单
     */
    @JSONField(name = "cxlOnFail")
    @JsonProperty("cxlOnFail")
    private Boolean cxlOnFail;

    /**
     * 用户自定义修改事件ID，字母（区分大小写）与数字的组合，可以是纯字母、纯数字且长度要在1-32位之间
     */
    @JSONField(name = "reqId")
    @JsonProperty("reqId")
    private String reqId;

    /**
     * 修改的新数量
     */
    @JSONField(name = "newSz")
    @JsonProperty("newSz")
    private BigDecimal newSz;

    /**
     * 止盈触发价
     * 如果止盈触发价或者委托价为0，那代表删除止盈
     */
    @JSONField(name = "newTpTriggerPx")
    @JsonProperty("newTpTriggerPx")
    private BigDecimal newTpTriggerPx;

    /**
     * 止盈委托价
     * 委托价格为-1时，执行市价止盈
     */
    @JSONField(name = "newTpOrdPx")
    @JsonProperty("newTpOrdPx")
    private BigDecimal newTpOrdPx;

    /**
     * 止损触发价
     * 如果止损触发价或者委托价为0，那代表删除止损
     */
    @JSONField(name = "newSlTriggerPx")
    @JsonProperty("newSlTriggerPx")
    private BigDecimal newSlTriggerPx;

    /**
     * 止损委托价
     * 委托价格为-1时，执行市价止损
     */
    @JSONField(name = "newSlOrdPx")
    @JsonProperty("newSlOrdPx")
    private BigDecimal newSlOrdPx;

    /**
     * 止盈触发价类型
     */
    @JSONField(name = "newTpTriggerPxType")
    @JsonProperty("newTpTriggerPxType")
    private TriggerPxType newTpTriggerPxType;

    /**
     * 止损触发价类型
     */
    @JSONField(name = "newSlTriggerPxType")
    @JsonProperty("newSlTriggerPxType")
    private TriggerPxType newSlTriggerPxType;
}
