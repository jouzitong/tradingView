package ai.zzt.okx.v5.entity.ws.biz;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ai.zzt.okx.v5.entity.rest.IOkxRestRsp;
import ai.zzt.okx.v5.entity.ws.WsSubscribeEntity;
import ai.zzt.okx.v5.enumeration.OrdType;
import ai.zzt.okx.v5.enumeration.OrderState;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.Side;
import ai.zzt.okx.v5.enumeration.TriggerPxType;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import ai.zzt.okx.v5.enumeration.ws.TdMode;

import java.math.BigDecimal;
import java.util.List;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class AlgoOrder implements WsSubscribeEntity, IOkxRestRsp {

    /**
     * Instrument type
     */
    @JSONField(name = "instType")
    @JsonProperty("instType")
    protected InstrumentType instType;

    /**
     * Instrument ID
     */
    @JSONField(name = "instId")
    @JsonProperty("instId")
    protected String instId;

    /**
     * Margin currency
     * Only applicable to cross M
     */
    @JSONField(name = "ccy")
    @JsonProperty("ccy")
    protected String ccy;

    /**
     * Latest order ID, the order ID associated with the algo order.
     */
    @JSONField(name = "ordId")
    @JsonProperty("ordId")
    protected String ordId;

    /**
     * Order ID list. There will be multiple order IDs when there is TP/SL splitting order.
     */
    @JSONField(name = "ordIdList")
    @JsonProperty("ordIdList")
    protected List<String> ordIdList;

    /**
     * Algo ID
     */
    @JSONField(name = "algoId")
    @JsonProperty("algoId")
    protected String algoId;

    /**
     * Client Order ID as assigned by the client
     */
    @JSONField(name = "clOrdId")
    @JsonProperty("clOrdId")
    protected String clOrdId;

    /**
     * Quantity to buy or sell.
     * SPOT/MARGIN: in the unit of currency.
     * FUTURES/SWAP/OPTION: in the unit of contract.
     */
    @JSONField(name = "sz")
    @JsonProperty("sz")
    protected BigDecimal sz;

    /**
     * Order type
     * conditional: One-way stop order
     * oco: One-cancels-the-other order
     * trigger: Trigger order
     */
    @JSONField(name = "ordType")
    @JsonProperty("ordType")
    protected OrdType ordType;

    /**
     * Order side
     * buy
     * sell
     */
    @JSONField(name = "side")
    @JsonProperty("side")
    protected Side side;

    /**
     * Position side
     * net
     * long or short
     * Only applicable to FUTURES/SWAP
     */
    @JSONField(name = "posSide")
    @JsonProperty("posSide")
    protected PositionsSide posSide;

    /**
     * Trade mode
     * cross: cross
     * isolated: isolated
     * cash: cash
     */
    @JSONField(name = "tdMode")
    @JsonProperty("tdMode")
    protected TdMode tdMode;

    /**
     * Order quantity unit setting for sz
     * base_ccy: Base currency
     * quote_ccy: Quote currency
     * Only applicable to SPOT Market Orders
     * Default is quote_ccy for buy, base_ccy for sell
     */
    @JSONField(name = "tgtCcy")
    @JsonProperty("tgtCcy")
    protected String tgtCcy;

    /**
     * Leverage, from 0.01 to 125.
     * Only applicable to MARGIN/FUTURES/SWAP
     */
    @JSONField(name = "lever")
    @JsonProperty("lever")
    protected BigDecimal lever;

    /**
     * Order status
     * live: to be effective
     * effective: effective
     * canceled: canceled
     * order_failed: order failed
     * partially_failed: partially failed
     */
    @JSONField(name = "state")
    @JsonProperty("state")
    protected OrderState state;

    /**
     * Take-profit trigger price.
     */
    @JSONField(name = "tpTriggerPx")
    @JsonProperty("tpTriggerPx")
    protected BigDecimal tpTriggerPx;

    /**
     * Take-profit trigger price type.
     * last: last price
     * index: index price
     * mark: mark price
     */
    @JSONField(name = "tpTriggerPxType")
    @JsonProperty("tpTriggerPxType")
    protected TriggerPxType tpTriggerPxType;

    /**
     * Take-profit order price.
     */
    @JSONField(name = "tpOrdPx")
    @JsonProperty("tpOrdPx")
    protected BigDecimal tpOrdPx;

    /**
     * Stop-loss trigger price.
     */
    @JSONField(name = "slTriggerPx")
    @JsonProperty("slTriggerPx")
    protected BigDecimal slTriggerPx;

    /**
     * Stop-loss trigger price type.
     * last: last price
     * index: index price
     * mark: mark price
     */
    @JSONField(name = "slTriggerPxType")
    @JsonProperty("slTriggerPxType")
    protected TriggerPxType slTriggerPxType;

    /**
     * Stop-loss order price.
     */
    @JSONField(name = "slOrdPx")
    @JsonProperty("slOrdPx")
    protected BigDecimal slOrdPx;

    /**
     * Trigger price
     */
    @JSONField(name = "triggerPx")
    @JsonProperty("triggerPx")
    protected BigDecimal triggerPx;

    /**
     * Trigger price type.
     * last: last price
     * index: index price
     * mark: mark price
     */
    @JSONField(name = "triggerPxType")
    @JsonProperty("triggerPxType")
    protected TriggerPxType triggerPxType;

    /**
     * Order price
     */
    @JSONField(name = "ordPx")
    @JsonProperty("ordPx")
    protected BigDecimal ordPx;

    /**
     * Last filled price while placing
     */
    @JSONField(name = "last")
    @JsonProperty("last")
    protected BigDecimal last;

    /**
     * Actual order quantity
     */
    @JSONField(name = "actualSz")
    @JsonProperty("actualSz")
    protected BigDecimal actualSz;

    /**
     * Actual order price
     */
    @JSONField(name = "actualPx")
    @JsonProperty("actualPx")
    protected BigDecimal actualPx;

    /**
     * Estimated national value in USD of order
     */
    @JSONField(name = "notionalUsd")
    @JsonProperty("notionalUsd")
    protected BigDecimal notionalUsd;

    /**
     * Order tag
     */
    @JSONField(name = "tag")
    @JsonProperty("tag")
    protected String tag;

    /**
     * Actual trigger side
     * Only applicable to oco order and conditional order
     */
    @JSONField(name = "actualSide")
    @JsonProperty("actualSide")
    protected String actualSide;

    /**
     * Trigger time, Unix timestamp format in milliseconds, e.g. 1597026383085
     */
    @JSONField(name = "triggerTime")
    @JsonProperty("triggerTime")
    protected Long triggerTime;

    /**
     * Whether the order can only reduce the position size. Valid options: true or false.
     */
    @JSONField(name = "reduceOnly")
    @JsonProperty("reduceOnly")
    protected Boolean reduceOnly;

    /**
     * It represents that the reason that algo order fails to trigger. It is "" when the state is effective/canceled. There will be value when the state is order_failed, e.g. 51008;
     * Only applicable to Stop Order, Trailing Stop Order, Trigger order.
     */
    @JSONField(name = "failCode")
    @JsonProperty("failCode")
    protected String failCode;

    /**
     * Client Algo Order ID as assigned by the client.
     */
    @JSONField(name = "algoClOrdId")
    @JsonProperty("algoClOrdId")
    protected String algoClOrdId;

    /**
     * Client Request ID as assigned by the client for order amendment. "" will be returned if there is no order amendment.
     */
    @JSONField(name = "reqId")
    @JsonProperty("reqId")
    protected String reqId;

    /**
     * The result of amending the order
     * -1: failure
     * 0: success
     */
    @JSONField(name = "amendResult")
    @JsonProperty("amendResult")
    protected Integer amendResult;

    /**
     * Whether to enable Cost-price SL. Only applicable to SL order of split TPs.
     * 0: disable, the default value
     * 1: Enable
     */
    @JSONField(name = "amendPxOnTriggerType")
    @JsonProperty("amendPxOnTriggerType")
    protected Integer amendPxOnTriggerType;

    /**
     * Creation time Unix timestamp format in milliseconds, e.g. 1597026383085
     */
    @JSONField(name = "cTime")
    @JsonProperty("cTime")
    protected Long cTime;

    /**
     * Order updated time, Unix timestamp format in milliseconds, e.g. 1597026383085
     */
    @JSONField(name = "uTime")
    @JsonProperty("uTime")
    protected Long uTime;

    /**
     * Attached SL/TP orders info
     * Applicable to Single-currency margin/Multi-currency margin/Portfolio margin
     */
    @JSONField(name = "attachAlgoOrds")
    @JsonProperty("attachAlgoOrds")
    protected List<AttachAloOrder> attachAlgoOrds;

    /**
     * Linked TP order detail, only applicable to TP limit order of one-cancels-the-other order(oco)
     */
    @JSONField(name = "linkedOrd")
    @JsonProperty("linkedOrd")
    protected LinkedOrder linkedOrd;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LinkedOrder {
        /**
         * 策略订单唯一标识
         */
        @JSONField(name = "algoId")
        @JsonProperty("algoId")
        protected String algoId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachAloOrder {

        /**
         * 下单附带止盈止损时，客户自定义的策略订单ID
         */
        @JSONField(name = "attachAlgoClOrdId")
        @JsonProperty("attachAlgoClOrdId")
        protected String attachAlgoClOrdId;

        /**
         * 止盈触发价
         */
        @JSONField(name = "tpTriggerPx")
        @JsonProperty("tpTriggerPx")
        protected BigDecimal tpTriggerPx;

        /**
         * 止盈触发价类型
         */
        @JSONField(name = "tpTriggerPxType")
        @JsonProperty("tpTriggerPxType")
        protected TriggerPxType tpTriggerPxType;

        /**
         * 止盈委托价
         */
        @JSONField(name = "tpOrdPx")
        @JsonProperty("tpOrdPx")
        protected BigDecimal tpOrdPx;

        /**
         * 止损触发价
         */
        @JSONField(name = "slTriggerPx")
        @JsonProperty("slTriggerPx")
        protected BigDecimal slTriggerPx;

        /**
         * 止损触发价类型
         */
        @JSONField(name = "slTriggerPxType")
        @JsonProperty("slTriggerPxType")
        protected TriggerPxType slTriggerPxType;

        /**
         * 止损委托价
         */
        @JSONField(name = "slOrdPx")
        @JsonProperty("slOrdPx")
        protected BigDecimal slOrdPx;
    }

}
