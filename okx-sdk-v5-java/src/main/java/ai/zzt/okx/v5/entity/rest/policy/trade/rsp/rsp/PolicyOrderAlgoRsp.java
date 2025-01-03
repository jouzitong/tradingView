package ai.zzt.okx.v5.entity.rest.policy.trade.rsp.rsp;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ai.zzt.okx.v5.entity.rest.IOkxRestRsp;
import ai.zzt.okx.v5.entity.rest.IRestEntity;
import ai.zzt.okx.v5.enumeration.OrdType;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.Side;
import ai.zzt.okx.v5.enumeration.TriggerPxType;
import ai.zzt.okx.v5.enumeration.trade.ActualSide;
import ai.zzt.okx.v5.enumeration.trade.AmendPxOnTriggerType;
import ai.zzt.okx.v5.enumeration.trade.OrderRetState;
import ai.zzt.okx.v5.enumeration.trade.TgtCcyEnum;
import ai.zzt.okx.v5.enumeration.ws.QuickMgnType;
import ai.zzt.okx.v5.enumeration.ws.TdMode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 策略修改
 */
@ToString
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyOrderAlgoRsp implements IRestEntity, IOkxRestRsp {
    /**
     * 产品类型
     */
    @JSONField(name = "instType")
    @JsonProperty("instType")
    private String instType;

    /**
     * 产品ID
     */
    @JSONField(name = "instId")
    @JsonProperty("instId")
    private String instId;

    /**
     * 保证金币种，仅适用于单币种保证金模式下的全仓杠杆订单
     */
    @JSONField(name = "ccy")
    @JsonProperty("ccy")
    private String ccy;

    /**
     * 最新一笔订单ID
     */
    @JSONField(name = "ordId")
    @JsonProperty("ordId")
    private String ordId;

    /**
     * 订单ID列表，当止盈止损存在市价拆单时，会有多个。
     */
    @JSONField(name = "ordIdList")
    @JsonProperty("ordIdList")
    private List<String> ordIdList;

    /**
     * 	策略委托单ID
     */
    @JSONField(name = "algoId")
    @JsonProperty("algoId")
    private String algoId;

    /**
     * 	客户自定义订单ID
     */
    @JSONField(name = "clOrdId")
    @JsonProperty("clOrdId")
    private String clOrdId;

    /**
     * 	委托数量
     */
    @JSONField(name = "sz")
    @JsonProperty("sz")
    private BigDecimal sz;

    /**
     * 	策略委托触发时，平仓的百分比。1 代表100%
     */
    @JSONField(name = "closeFraction")
    @JsonProperty("closeFraction")
    private BigDecimal closeFraction;


    /**
     * 	订单类型
     */
    @JSONField(name = "ordType")
    @JsonProperty("ordType")
    private OrdType ordType;

    /**
     * 	订单方向
     */
    @JSONField(name = "side")
    @JsonProperty("side")
    private Side side;

    /**
     * 	持仓方向
     */
    @JSONField(name = "posSide")
    @JsonProperty("posSide")
    private PositionsSide posSide;

    /**
     * 	交易模式
     */
    @JSONField(name = "tdMode")
    @JsonProperty("tdMode")
    private TdMode tdMode;

    /**
     * 委托数量的类型
     * base_ccy: 交易货币 ；quote_ccy：计价货币
     * 仅适用于币币单向止盈止损市价买单
     * 默认买为计价货币，卖为交易货币
     */
    @JSONField(name = "tgtCcy")
    @JsonProperty("tgtCcy")
    private TgtCcyEnum tgtCcy;

    /**
     * 订单状态
     */
    @JSONField(name = "state")
    @JsonProperty("state")
    private OrderRetState state;

    /**
     * 杠杆倍数，0.01到125之间的数值，仅适用于 币币杠杆/交割/永续
     */
    @JSONField(name = "lever")
    @JsonProperty("lever")
    private BigDecimal lever;

    /**
     * 止盈触发价
     */
    @JSONField(name = "tpTriggerPx")
    @JsonProperty("tpTriggerPx")
    private BigDecimal tpTriggerPx;

    /**
     * 止盈触发价类型
     */
    @JSONField(name = "tpTriggerPxType")
    @JsonProperty("tpTriggerPxType")
    private TriggerPxType tpTriggerPxType;

    /**
     * 止盈委托价
     */
    @JSONField(name = "tpOrdPx")
    @JsonProperty("tpOrdPx")
    private BigDecimal tpOrdPx;

    /**
     * 止损触发价
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
     * 止损委托价
     */
    @JSONField(name = "slOrdPx")
    @JsonProperty("slOrdPx")
    private BigDecimal slOrdPx;

    /**
     * 计划委托触发价格
     */
    @JSONField(name = "triggerPx")
    @JsonProperty("triggerPx")
    private BigDecimal triggerPx;


    /**
     * 计划委托触发价格类型
     */
    @JSONField(name = "triggerPxType")
    @JsonProperty("triggerPxType")
    private TriggerPxType triggerPxType;

    /**
     * 订单委托价格
     */
    @JSONField(name = "ordPx")
    @JsonProperty("ordPx")
    private BigDecimal ordPx;

    /**
     * 实际委托量
     */
    @JSONField(name = "actualSz")
    @JsonProperty("actualSz")
    private BigDecimal actualSz;

    /**
     * 实际委托价
     */
    @JSONField(name = "actualPx")
    @JsonProperty("actualPx")
    private BigDecimal actualPx;

    /**
     * 	实际触发方向
     */
    @JSONField(name = "actualSide")
    @JsonProperty("actualSide")
    private ActualSide actualSide;

    /**
     * 策略委托触发时间，Unix时间戳的毫秒数格式，如 1597026383085
     */
    @JSONField(name = "triggerTime")
    @JsonProperty("triggerTime")
    private Long triggerTime;

    /**
     * 价格比例
     * 仅适用于冰山委托和时间加权委托
     */
    @JSONField(name = "pxVar")
    @JsonProperty("pxVar")
    private BigDecimal pxVar;

    /**
     * 价距
     * 仅适用于冰山委托和时间加权委托
     */
    @JSONField(name = "pxSpread")
    @JsonProperty("pxSpread")
    private BigDecimal pxSpread;

    /**
     * 单笔数量
     * 仅适用于冰山委托和时间加权委托
     */
    @JSONField(name = "szLimit")
    @JsonProperty("szLimit")
    private BigDecimal szLimit;

    /**
     * 挂单限制价
     * 仅适用于冰山委托和时间加权委托
     */
    @JSONField(name = "pxLimit")
    @JsonProperty("pxLimit")
    private BigDecimal pxLimit;

    /**
     * 订单标签
     */
    @JSONField(name = "tag")
    @JsonProperty("tag")
    private String tag;

    /**
     * 下单间隔
     * 仅适用于时间加权委托
     */
    @JSONField(name = "timeInterval")
    @JsonProperty("timeInterval")
    private BigDecimal timeInterval;

    /**
     * 回调幅度的比例
     * 仅适用于移动止盈止损
     */
    @JSONField(name = "callbackRatio")
    @JsonProperty("callbackRatio")
    private BigDecimal callbackRatio;

    /**
     * 回调幅度的价距
     * 仅适用于移动止盈止损
     */
    @JSONField(name = "callbackSpread")
    @JsonProperty("callbackSpread")
    private BigDecimal callbackSpread;

    /**
     * 移动止盈止损激活价格
     * 仅适用于移动止盈止损
     */
    @JSONField(name = "activePx")
    @JsonProperty("activePx")
    private BigDecimal activePx;

    /**
     * 移动止盈止损触发价格
     * 仅适用于移动止盈止损
     */
    @JSONField(name = "moveTriggerPx")
    @JsonProperty("moveTriggerPx")
    private BigDecimal moveTriggerPx;

    /**
     * 是否只减仓
     * true或false
     */
    @JSONField(name = "reduceOnly")
    @JsonProperty("reduceOnly")
    private Boolean reduceOnly;

    /**
     * 一键借币类型，仅适用于杠杆逐仓的一键借币模式
     */
    @JSONField(name = "quickMgnType")
    @JsonProperty("quickMgnType")
    private QuickMgnType quickMgnType;

    /**
     * 下单时的最新成交价
     */
    @JSONField(name = "last")
    @JsonProperty("last")
    private BigDecimal last;

    /**
     * 代表策略触发失败的原因，已撤销和已生效时为""，委托失败时有值，如 51008；
     * 仅适用于单向止盈止损委托、双向止盈止损委托、移动止盈止损委托、计划委托。
     */
    @JSONField(name = "failCode")
    @JsonProperty("failCode")
    private String failCode;

    /**
     * 客户自定义策略订单ID
     */
    @JSONField(name = "algoClOrdId")
    @JsonProperty("algoClOrdId")
    private String algoClOrdId;

    /**
     * 是否启用开仓价止损
     * 仅适用于分批止盈的止损订单
     * 0：不开启，默认值
     * 1：开启
     */
    @JSONField(name = "amendPxOnTriggerType")
    @JsonProperty("amendPxOnTriggerType")
    private AmendPxOnTriggerType amendPxOnTriggerType;


    /**
     * 附带止盈止损信息
     * 适用于单币种保证金模式/跨币种保证金模式/组合保证金模式
     */
    @JSONField(name = "attachAlgoOrds")
    @JsonProperty("attachAlgoOrds")
    private List<AttachAlgoOrdRep> attachAlgoOrds;

    /**
     * 	订单创建时间，Unix时间戳的毫秒数格式，如 1597026383085
     */
    @JSONField(name = "cTime")
    @JsonProperty("cTime")
    private Long cTime;

    /**
     * 订单更新时间，Unix时间戳的毫秒数格式，如 1597026383085
     */
    @JSONField(name = "uTime")
    @JsonProperty("uTime")
    private Long uTime;


}
