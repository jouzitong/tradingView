package ai.zzt.okx.v5.ws.pub;

import ai.zzt.okx.v5.entity.ws.request.pub.AdlWarningArg;
import ai.zzt.okx.v5.entity.ws.request.pub.FundingRateArg;
import ai.zzt.okx.v5.entity.ws.request.pub.IndexTickersArg;
import ai.zzt.okx.v5.entity.ws.request.pub.InstrumentsArg;
import ai.zzt.okx.v5.entity.ws.request.pub.MarkPriceArg;
import ai.zzt.okx.v5.entity.ws.request.pub.OpenInterestArg;
import ai.zzt.okx.v5.entity.ws.request.pub.OptionSummaryArg;
import ai.zzt.okx.v5.entity.ws.request.pub.PriceLimitArg;
import ai.zzt.okx.v5.enumeration.ws.Operation;
import ai.zzt.okx.v5.enumeration.ws.WsChannel;
import ai.zzt.okx.v5.ws.WsApi;

/**
 * TODO <a href="https://www.okx.com/docs-v5/zh/?shell#public-data-websocket">公共数据 WS API</a>
 *
 * @author Jou Ziton
 * @since 2024/5/18
 **/
public interface OpenDataWsApi extends WsApi {

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#public-data-websocket-instruments-channel">产品频道</a>
     */
    default void instruments(InstrumentsArg instrumentsArg) {
        instruments(instrumentsArg, Operation.SUBSCRIBE);
    }

    default void instruments(InstrumentsArg instrumentsArg, Operation op) {
        operate(op, WsChannel.PUBLIC, instrumentsArg);
    }

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#public-data-websocket-open-interest-channel">持仓量</a>
     *
     * @param openInterestArg 持仓量参数
     */
    default void openInterest(OpenInterestArg openInterestArg) {
        openInterest(openInterestArg, Operation.SUBSCRIBE);
    }

    default void openInterest(OpenInterestArg openInterestArg, Operation op) {
        operate(op, WsChannel.PUBLIC, openInterestArg);
    }

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#public-data-websocket-funding-rate-channel">资金费率</a>
     *
     * @param fundingRateArg 资金费率参数
     */
    default void fundingRate(FundingRateArg fundingRateArg) {
        fundingRate(fundingRateArg, Operation.SUBSCRIBE);
    }

    default void fundingRate(FundingRateArg fundingRateArg, Operation op) {
        operate(op, WsChannel.PUBLIC, fundingRateArg);
    }

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#public-data-websocket-price-limit-channel">限价</a>
     *
     * @param priceLimitArg 限价参数
     */
    default void priceLimit(PriceLimitArg priceLimitArg) {
        priceLimit(priceLimitArg, Operation.SUBSCRIBE);
    }

    default void priceLimit(PriceLimitArg priceLimitArg, Operation op) {
        operate(op, WsChannel.PUBLIC, priceLimitArg);
    }


    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#public-data-websocket-index-tickers-channel">指数标记</a>
     *
     * @param indexTickersArg 指数标记参数
     */
    default void indexTickers(IndexTickersArg indexTickersArg) {
        indexTickers(indexTickersArg, Operation.SUBSCRIBE);
    }

    default void indexTickers(IndexTickersArg indexTickersArg, Operation op) {
        operate(op, WsChannel.PUBLIC, indexTickersArg);
    }

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#public-data-websocket-mark-price-channel">标记价格</a>
     *
     * @param arg 标记价格参数
     */
    default void markPrice(MarkPriceArg arg) {
        markPrice(arg, Operation.SUBSCRIBE);
    }

    default void markPrice(MarkPriceArg arg, Operation op) {
        operate(op, WsChannel.PUBLIC, arg);
    }

//    /**
//     * <a href="https://www.okx.com/docs-v5/zh/?shell#public-data-websocket-liquidation-orders-channel">爆仓订单</a>
//     *
//     * @param liquidationWarningArg 爆仓订单参数
//     */
//    default void liquidationOrders(LiquidationWarningArg liquidationWarningArg) {
//        liquidationOrders(liquidationWarningArg, Operation.SUBSCRIBE);
//    }
//
//    default void liquidationOrders(LiquidationWarningArg liquidationWarningArg, Operation op) {
//        operate(op, WsChannel.PUBLIC, liquidationWarningArg);
//    }

    default void adlWarning(AdlWarningArg arg) {
        adlWarning(arg, Operation.SUBSCRIBE);
    }

    default void adlWarning(AdlWarningArg arg, Operation op) {

    }

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#public-data-websocket-option-summary-channel">期权概要</a>
     *
     * @param optionSummaryArg 期权概要参数
     */
    default void optionSummary(OptionSummaryArg optionSummaryArg) {
        optionSummary(optionSummaryArg, Operation.SUBSCRIBE);
    }

    default void optionSummary(OptionSummaryArg optionSummaryArg, Operation op) {
        operate(op, WsChannel.PUBLIC, optionSummaryArg);
    }

    // TODO 其他频道

}