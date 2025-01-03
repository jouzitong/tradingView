package ai.zzt.okx.v5.entity.ws.biz;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ai.zzt.okx.v5.entity.ws.WsSubscribeEntity;
import ai.zzt.okx.v5.enumeration.OrderState;
import ai.zzt.okx.v5.enumeration.OrderType;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.Side;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import ai.zzt.okx.v5.enumeration.ws.TdMode;

import java.math.BigDecimal;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class AdvanceAlgoOrder implements WsSubscribeEntity {

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
     * Only applicable to cross MARGIN orders in Single-currency margin.
     */
    @JSONField(name = "ccy")
    @JsonProperty("ccy")
    protected String ccy;

    /**
     * Order ID, the order ID associated with the algo order.
     */
    @JSONField(name = "ordId")
    @JsonProperty("ordId")
    protected String ordId;

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
     * Quantity to buy or sell. SPOT/MARGIN: in the unit of currency. FUTURES/SWAP/OPTION: in the unit of contract.
     */
    @JSONField(name = "sz")
    @JsonProperty("sz")
    protected BigDecimal sz;

    /**
     * Order type
     * iceberg: Iceberg order
     * twap: TWAP order
     * move_order_stop: Trailing order
     */
    @JSONField(name = "ordType")
    @JsonProperty("ordType")
    protected OrderType ordType;

    /**
     * Order side, buy sell
     */
    @JSONField(name = "side")
    @JsonProperty("side")
    protected Side side;

    /**
     * Position side
     * net
     * long or short Only applicable to FUTURES/SWAP
     */
    @JSONField(name = "posSide")
    @JsonProperty("posSide")
    protected PositionsSide posSide;

    /**
     * Trade mode, cross: cross isolated: isolated cash: cash
     */
    @JSONField(name = "tdMode")
    @JsonProperty("tdMode")
    protected TdMode tdMode;

    /**
     * Order quantity unit setting for sz
     * base_ccy: Base currency ,quote_ccy: Quote currency
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
    protected Double lever;

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
    protected OrderState state;

    /**
     * Take-profit trigger price.
     */
    @JSONField(name = "tpTriggerPx")
    @JsonProperty("tpTriggerPx")
    protected BigDecimal tpTriggerPx;

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
     * Order price
     */
    @JSONField(name = "ordPx")
    @JsonProperty("ordPx")
    protected BigDecimal ordPx;

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
     * Creation time, Unix timestamp format in milliseconds, e.g. 1597026383085
     */
    @JSONField(name = "cTime")
    @JsonProperty("cTime")
    protected Long cTime;

    /**
     * Price ratio
     * Only applicable to iceberg order or twap order
     */
    @JSONField(name = "pxVar")
    @JsonProperty("pxVar")
    protected BigDecimal pxVar;

    /**
     * Price variance
     * Only applicable to iceberg order or twap order
     */
    @JSONField(name = "pxSpread")
    @JsonProperty("pxSpread")
    protected BigDecimal pxSpread;

    /**
     * Average amount
     * Only applicable to iceberg order or twap order
     */
    @JSONField(name = "szLimit")
    @JsonProperty("szLimit")
    protected BigDecimal szLimit;

    /**
     * Price limit
     * Only applicable to iceberg order or twap order
     */
    @JSONField(name = "pxLimit")
    @JsonProperty("pxLimit")
    protected BigDecimal pxLimit;

    /**
     * Time interval
     * Only applicable to twap order
     */
    @JSONField(name = "timeInterval")
    @JsonProperty("timeInterval")
    protected String timeInterval;

    /**
     * Algo Order count
     * Only applicable to iceberg order or twap order
     */
    @JSONField(name = "count")
    @JsonProperty("count")
    protected Integer count;

    /**
     * Callback price ratio
     * Only applicable to move_order_stop order
     */
    @JSONField(name = "callbackRatio")
    @JsonProperty("callbackRatio")
    protected BigDecimal callbackRatio;

    /**
     * Callback price variance
     * Only applicable to move_order_stop order
     */
    @JSONField(name = "callbackSpread")
    @JsonProperty("callbackSpread")
    protected BigDecimal callbackSpread;

    /**
     * Active price
     * Only applicable to move_order_stop order
     */
    @JSONField(name = "activePx")
    @JsonProperty("activePx")
    protected BigDecimal activePx;

    /**
     * Trigger price
     * Only applicable to move_order_stop order
     */
    @JSONField(name = "moveTriggerPx")
    @JsonProperty("moveTriggerPx")
    protected BigDecimal moveTriggerPx;

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
     * Whether the order can only reduce the position size. Valid options: true or false.
     */
    @JSONField(name = "reduceOnly")
    @JsonProperty("reduceOnly")
    protected Boolean reduceOnly;

    /**
     * Push time of algo order information, millisecond format of Unix timestamp, e.g. 1597026383085
     */
    @JSONField(name = "pTime")
    @JsonProperty("pTime")
    protected Long pTime;


}
