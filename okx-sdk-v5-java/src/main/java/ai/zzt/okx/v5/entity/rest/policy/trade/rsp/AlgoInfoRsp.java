package ai.zzt.okx.v5.entity.rest.policy.trade.rsp;

import ai.zzt.okx.v5.entity.rest.IOkxRestRsp;
import ai.zzt.okx.v5.entity.rest.IRestEntity;
import ai.zzt.okx.v5.entity.rest.trading.order.booking.req.PlaceOrderReq;
import ai.zzt.okx.v5.entity.ws.biz.AlgoOrder;
import ai.zzt.okx.v5.enumeration.OrderState;
import ai.zzt.okx.v5.enumeration.OrderType;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.Side;
import ai.zzt.okx.v5.enumeration.TriggerPxType;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import ai.zzt.okx.v5.enumeration.ws.QuickMgnType;
import ai.zzt.okx.v5.enumeration.ws.TdMode;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

/**
 * 策略委托单信息
 */
@ToString
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AlgoInfoRsp implements IRestEntity, IOkxRestRsp {
    /**
     * 产品类型
     */
    @JSONField(name = "instType")
    @JsonProperty("instType")
    private InstrumentType instType;

    /**
     * Instrument ID
     */
    @JSONField(name = "instId")
    @JsonProperty("instId")
    private String instId;

    /**
     * 保证金币种，
     * 仅适用于单币种保证金模式下的全仓杠杆订单
     */
    @JSONField(name = "ccy")
    @JsonProperty("ccy")
    private String ccy;

    /**
     * 最新一笔订单ID, the order ID associated with the algo order.
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
     * Algo ID
     */
    @JSONField(name = "algoId")
    @JsonProperty("algoId")
    private String algoId;

    /**
     * Client Order ID as assigned by the client
     */
    @JSONField(name = "clOrdId")
    @JsonProperty("clOrdId")
    private String clOrdId;

    /**
     * Quantity to buy or sell. SPOT/MARGIN: in the unit of currency. FUTURES/SWAP/OPTION: in the unit of contract.
     */
    @JSONField(name = "sz")
    @JsonProperty("sz")
    private BigDecimal sz;

    /**
     * Order type
     * iceberg: Iceberg order
     * twap: TWAP order
     * move_order_stop: Trailing order
     */
    @JSONField(name = "ordType")
    @JsonProperty("ordType")
    private OrderType ordType;

    /**
     * Order side, buy sell
     */
    @JSONField(name = "side")
    @JsonProperty("side")
    private Side side;

    /**
     * Position side
     * net
     * long or short Only applicable to FUTURES/SWAP
     */
    @JSONField(name = "posSide")
    @JsonProperty("posSide")
    private PositionsSide posSide;

    /**
     * Trade mode, cross: cross isolated: isolated cash: cash
     */
    @JSONField(name = "tdMode")
    @JsonProperty("tdMode")
    private TdMode tdMode;

    /**
     * Order quantity unit setting for sz
     * base_ccy: Base currency ,quote_ccy: Quote currency
     * Only applicable to SPOT Market Orders
     * Default is quote_ccy for buy, base_ccy for sell
     */
    @JSONField(name = "tgtCcy")
    @JsonProperty("tgtCcy")
    private String tgtCcy;

    /**
     * Leverage, from 0.01 to 125.
     * Only applicable to MARGIN/FUTURES/SWAP
     */
    @JSONField(name = "lever")
    @JsonProperty("lever")
    private Double lever;

    /**
     * Order status
     * live: to be effective
     * effective: effective
     * partially_effective: partially effective
     * canceled: canceled
     * order_failed: order failed
     */
    @JSONField(name = "state")
    @JsonProperty("state")
    private OrderState state;

    /**
     * Take-profit trigger price.
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
     * Take-profit order price.
     */
    @JSONField(name = "tpOrdPx")
    @JsonProperty("tpOrdPx")
    private BigDecimal tpOrdPx;

    /**
     * Stop-loss trigger price.
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
     * Stop-loss order price.
     */
    @JSONField(name = "slOrdPx")
    @JsonProperty("slOrdPx")
    private BigDecimal slOrdPx;

    /**
     * Trigger price
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
     * Order price
     */
    @JSONField(name = "ordPx")
    @JsonProperty("ordPx")
    private BigDecimal ordPx;

    /**
     * Actual order quantity
     */
    @JSONField(name = "actualSz")
    @JsonProperty("actualSz")
    private BigDecimal actualSz;

    /**
     * Actual order price
     */
    @JSONField(name = "actualPx")
    @JsonProperty("actualPx")
    private BigDecimal actualPx;

    /**
     * Actual trigger side
     */
    @JSONField(name = "actualSide")
    @JsonProperty("actualSide")
    private String actualSide;

    /**
     * Trigger time, Unix timestamp format in milliseconds, e.g. 1597026383085
     */
    @JSONField(name = "triggerTime")
    @JsonProperty("triggerTime")
    private Long triggerTime;

    /**
     * Price ratio
     * Only applicable to iceberg order or twap order
     */
    @JSONField(name = "pxVar")
    @JsonProperty("pxVar")
    private BigDecimal pxVar;

    /**
     * Price variance
     * Only applicable to iceberg order or twap order
     */
    @JSONField(name = "pxSpread")
    @JsonProperty("pxSpread")
    private BigDecimal pxSpread;

    /**
     * Average amount
     * Only applicable to iceberg order or twap order
     */
    @JSONField(name = "szLimit")
    @JsonProperty("szLimit")
    private BigDecimal szLimit;

    /**
     * Price limit
     * Only applicable to iceberg order or twap order
     */
    @JSONField(name = "pxLimit")
    @JsonProperty("pxLimit")
    private BigDecimal pxLimit;

    /**
     * Order tag
     */
    @JSONField(name = "tag")
    @JsonProperty("tag")
    private String tag;

    /**
     * Time interval
     * Only applicable to twap order
     */
    @JSONField(name = "timeInterval")
    @JsonProperty("timeInterval")
    private String timeInterval;

    /**
     * Callback price ratio
     * Only applicable to move_order_stop order
     */
    @JSONField(name = "callbackRatio")
    @JsonProperty("callbackRatio")
    private BigDecimal callbackRatio;

    /**
     * Callback price variance
     * Only applicable to move_order_stop order
     */
    @JSONField(name = "callbackSpread")
    @JsonProperty("callbackSpread")
    private BigDecimal callbackSpread;

    /**
     * Active price
     * Only applicable to move_order_stop order
     */
    @JSONField(name = "activePx")
    @JsonProperty("activePx")
    private BigDecimal activePx;

    /**
     * Trigger price
     * Only applicable to move_order_stop order
     */
    @JSONField(name = "moveTriggerPx")
    @JsonProperty("moveTriggerPx")
    private BigDecimal moveTriggerPx;

    /**
     * Whether the order can only reduce the position size. Valid options: true or false.
     */
    @JSONField(name = "reduceOnly")
    @JsonProperty("reduceOnly")
    private Boolean reduceOnly;

    /**
     * 一键借币类型，仅适用于杠杆逐仓的一键借币模式
     * manual：手动，auto_borrow：自动借币，auto_repay：自动还币
     */
    @JSONField(name = "quickMgnType")
    @JsonProperty("quickMgnType")
    private QuickMgnType quickMgnType;

    /**
     * 最新成交价
     */
    @JSONField(name = "last")
    @JsonProperty("last")
    private BigDecimal last;

    /**
     * It represents that the reason that algo order fails to trigger. It is "" when the state is effective/canceled. There will be value when the state is order_failed, e.g. 51008;
     * Only applicable to Stop Order, Trailing Stop Order, Trigger order.
     */
    @JSONField(name = "failCode")
    @JsonProperty("failCode")
    private String failCode;

    /**
     * Client Algo Order ID as assigned by the client.
     */
    @JSONField(name = "algoClOrdId")
    @JsonProperty("algoClOrdId")
    private String algoClOrdId;

    /**
     * Whether to enable Cost-price SL. Only applicable to SL order of split TPs.
     * 0: disable, the default value
     * 1: Enable
     */
    @JSONField(name = "amendPxOnTriggerType")
    @JsonProperty("amendPxOnTriggerType")
    private Integer amendPxOnTriggerType;

    /**
     * 附带止盈止损信息
     */
    @JSONField(name = "attachAlgoOrds")
    @JsonProperty("attachAlgoOrds")
    private List<PlaceOrderReq.AttachAlgoOrder> attachAlgoOrds;

    /**
     * Linked TP order detail, only applicable to TP limit order of one-cancels-the-other order(oco)
     */
    @JSONField(name = "linkedOrd")
    @JsonProperty("linkedOrd")
    private AlgoOrder.LinkedOrder linkedOrd;

    /**
     * Creation time, Unix timestamp format in milliseconds, e.g. 1597026383085
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
