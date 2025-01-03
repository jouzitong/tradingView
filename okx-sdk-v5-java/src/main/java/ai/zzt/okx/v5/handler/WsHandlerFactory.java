package ai.zzt.okx.v5.handler;

import ai.zzt.okx.v5.entity.ws.biz.AdvanceAlgoOrder;
import ai.zzt.okx.v5.entity.ws.biz.AlgoOrder;
import ai.zzt.okx.v5.entity.ws.biz.DepositInfo;
import ai.zzt.okx.v5.entity.ws.biz.EconomicCalendar;
import ai.zzt.okx.v5.entity.ws.biz.GridOrderContract;
import ai.zzt.okx.v5.entity.ws.biz.GridOrderSpot;
import ai.zzt.okx.v5.entity.ws.biz.GridPositions;
import ai.zzt.okx.v5.entity.ws.biz.GridSubOrder;
import ai.zzt.okx.v5.entity.ws.biz.IndexCandlesticks;
import ai.zzt.okx.v5.entity.ws.biz.MarkPriceCandlesticks;
import ai.zzt.okx.v5.entity.ws.biz.WithdrawalInfo;
import ai.zzt.okx.v5.entity.ws.pri.Account;
import ai.zzt.okx.v5.entity.ws.pri.AccountGreeks;
import ai.zzt.okx.v5.entity.ws.pri.BalanceAndPosition;
import ai.zzt.okx.v5.entity.ws.pri.LiquidationWarning;
import ai.zzt.okx.v5.entity.ws.pri.Order;
import ai.zzt.okx.v5.entity.ws.pri.Positions;
import ai.zzt.okx.v5.entity.ws.pub.AdlWarning;
import ai.zzt.okx.v5.entity.ws.pub.EstimatedPrice;
import ai.zzt.okx.v5.entity.ws.pub.FundingRate;
import ai.zzt.okx.v5.entity.ws.pub.IndexTickers;
import ai.zzt.okx.v5.entity.ws.pub.Instruments;
import ai.zzt.okx.v5.entity.ws.pub.LiquidationOrders;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.entity.ws.pub.OpenInterest;
import ai.zzt.okx.v5.entity.ws.pub.OptionSummary;
import ai.zzt.okx.v5.entity.ws.pub.PriceLimit;
import ai.zzt.okx.v5.entity.ws.request.biz.AdvanceAlgoOrderArg;
import ai.zzt.okx.v5.entity.ws.request.biz.AlgoOrderArg;
import ai.zzt.okx.v5.entity.ws.request.biz.DepositInfoArg;
import ai.zzt.okx.v5.entity.ws.request.biz.EconomicCalendarArg;
import ai.zzt.okx.v5.entity.ws.request.biz.GridOrderContractArg;
import ai.zzt.okx.v5.entity.ws.request.biz.GridOrderSpotArg;
import ai.zzt.okx.v5.entity.ws.request.biz.GridPositionsArg;
import ai.zzt.okx.v5.entity.ws.request.biz.GridSubOrderArg;
import ai.zzt.okx.v5.entity.ws.request.biz.IndexCandlesticksArg;
import ai.zzt.okx.v5.entity.ws.request.biz.MarkPriceCandlesticksArg;
import ai.zzt.okx.v5.entity.ws.request.biz.WithdrawalInfoArg;
import ai.zzt.okx.v5.entity.ws.request.pri.AccountArg;
import ai.zzt.okx.v5.entity.ws.request.pri.AccountGreeksArg;
import ai.zzt.okx.v5.entity.ws.request.pri.BalanceAndPositionArg;
import ai.zzt.okx.v5.entity.ws.request.pri.OrderArg;
import ai.zzt.okx.v5.entity.ws.request.pri.PositionsArg;
import ai.zzt.okx.v5.entity.ws.request.pub.AdlWarningArg;
import ai.zzt.okx.v5.entity.ws.request.pub.EstimatedPriceArg;
import ai.zzt.okx.v5.entity.ws.request.pub.FundingRateArg;
import ai.zzt.okx.v5.entity.ws.request.pub.IndexTickersArg;
import ai.zzt.okx.v5.entity.ws.request.pub.InstrumentsArg;
import ai.zzt.okx.v5.entity.ws.request.pub.LiquidationOrdersArg;
import ai.zzt.okx.v5.entity.ws.request.pub.MarkPriceArg;
import ai.zzt.okx.v5.entity.ws.request.pub.OpenInterestArg;
import ai.zzt.okx.v5.entity.ws.request.pub.OptionSummaryArg;
import ai.zzt.okx.v5.entity.ws.request.pub.PriceLimitArg;
import ai.zzt.okx.v5.entity.ws.response.Event;
import ai.zzt.okx.v5.entity.ws.response.pri.AmendOrderArg;
import ai.zzt.okx.v5.entity.ws.response.pri.CancelOrderArg;
import ai.zzt.okx.v5.entity.ws.response.pri.MassCancelOrderArg;
import ai.zzt.okx.v5.entity.ws.response.pri.PlaceOrderArg;
import ai.zzt.okx.v5.enumeration.ws.Channel;
import ai.zzt.okx.v5.enumeration.ws.Operation;
import ai.zzt.okx.v5.enumeration.ws.WsChannel;
import ai.zzt.okx.v5.ws.OkxWsApiService;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import static ai.zzt.okx.v5.constant.OkxConstants.*;

@Slf4j
public class WsHandlerFactory {

    public static WsHandler getHandler(WsChannel wsChannel,
                                       OkxWsApiService okxWsApiService,
                                       String message) {
        JSONObject jsonObject = JSONObject.parseObject(message);
        String event = jsonObject.getString(RSP_EVENT);
        if (event != null) {
            Event evt = Event.fromValue(event);
            // login handler
            if (evt == Event.LOGIN) {
                return new WsLoginHandler(wsChannel, message);
            }

            // error
            if (evt == Event.ERROR) {
                return new WsErrorHandler(wsChannel, message);
            }

            // subscribe / unsubscribe
            if (evt == Event.SUBSCRIBE || evt == Event.UNSUBSCRIBE) {
                return getSubscribeHandler(wsChannel, message, false);
            }
        }

        // subscribe push entity
        if (jsonObject.containsKey(RSP_ARG) && jsonObject.containsKey(RSP_DATA)) {
            return getSubscribeHandler(wsChannel, message, true);
        }

        // once request
        Operation operation = Operation.fromValue(jsonObject.getString(RSP_OP));
        if (jsonObject.containsKey(RSP_ID) && operation != null) {
            return getOnceHandler(wsChannel, operation, message);
        }

        return null;
    }

    public static WsHandler getOnceHandler(WsChannel wsChannel,
                                           Operation operation,
                                           String message) {
        return switch (operation) {
            // private
            case ORDER, BATCH_ORDERS -> new WsOnceHandler<>(PlaceOrderArg.class, wsChannel, message, operation);
            case CANCEL_ORDER, BATCH_CANCEL_ORDERS ->
                    new WsOnceHandler<>(CancelOrderArg.class, wsChannel, message, operation);
            case AMEND_ORDER, BATCH_AMEND_ORDERS ->
                    new WsOnceHandler<>(AmendOrderArg.class, wsChannel, message, operation);
            case MASS_CANCEL_ORDERS -> new WsOnceHandler<>(MassCancelOrderArg.class, wsChannel, message, operation);
            // business
            // public
            default -> null;
        };
    }

    private static WsHandler getSubscribeHandler(
            WsChannel wsChannel,
            String message,
            boolean pushData) {
        Channel channel = Channel.fromValue(JSONObject.parseObject(message).getJSONObject(RSP_ARG).getString(RSP_CHANNEL));
        assert channel != null;
        return switch (channel) {
            // private
            case ACCOUNT -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.pri.AccountArg.class, Account.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(AccountArg.class,
                            channel, message, wsChannel);
            case POSITIONS -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.pri.PositionsArg.class, Positions.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(PositionsArg.class,
                            channel, message, wsChannel);
            case BALANCE_AND_POSITION -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.pri.BalanceAndPositionArg.class, BalanceAndPosition.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(BalanceAndPositionArg.class,
                            channel, message, wsChannel);
            case LIQUIDATION_WARNING -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.pub.LiquidationOrdersArg.class, LiquidationWarning.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(LiquidationOrdersArg.class,
                            channel, message, wsChannel);
            case ACCOUNT_GREEKS -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.pri.AccountGreeksArg.class, AccountGreeks.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(AccountGreeksArg.class,
                            channel, message, wsChannel);
            case ORDERS -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.pri.OrderArg.class, Order.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(OrderArg.class,
                            channel, message, wsChannel);
            // public
            case INSTRUMENTS -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.pub.InstrumentsArg.class, Instruments.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(InstrumentsArg.class,
                            channel, message, wsChannel);
            case OPEN_INTEREST -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.pub.OpenInterestArg.class, OpenInterest.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(OpenInterestArg.class,
                            channel, message, wsChannel);
            case FUNDING_RATE -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.pub.FundingRateArg.class, FundingRate.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(FundingRateArg.class,
                            channel, message, wsChannel);
            case PRICE_LIMIT -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.pub.PriceLimitArg.class, PriceLimit.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(PriceLimitArg.class,
                            channel, message, wsChannel);
            case OPT_SUMMARY -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.pub.OptionSummaryArg.class, OptionSummary.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(OptionSummaryArg.class,
                            channel, message, wsChannel);
            case ESTIMATED_PRICE -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.pub.EstimatedPriceArg.class, EstimatedPrice.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(EstimatedPriceArg.class,
                            channel, message, wsChannel);
            case MARK_PRICE -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.pub.MarkPriceArg.class, MarkPrice.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(MarkPriceArg.class,
                            channel, message, wsChannel);
            case INDEX_TICKERS -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.pub.IndexTickersArg.class, IndexTickers.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(IndexTickersArg.class,
                            channel, message, wsChannel);
            case LIQUIDATION_ORDERS -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.pub.LiquidationOrdersArg.class, LiquidationOrders.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(LiquidationOrdersArg.class,
                            channel, message, wsChannel);
            case ADL_WARNINGS -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.pub.AdlWarningArg.class, AdlWarning.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(AdlWarningArg.class,
                            channel, message, wsChannel);
            // business
            case DEPOSIT_INFO -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.biz.DepositInfoArg.class, DepositInfo.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(DepositInfoArg.class,
                            channel, message, wsChannel);
            case WITHDRAWAL_INFO -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.biz.WithdrawalInfoArg.class, WithdrawalInfo.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(WithdrawalInfoArg.class,
                            channel, message, wsChannel);
            case MARK_PRICE_CANDLE_3MON,
                 MARK_PRICE_CANDLE_1MON,
                 MARK_PRICE_CANDLE_1W,
                 MARK_PRICE_CANDLE_1D,
                 MARK_PRICE_CANDLE_2D,
                 MARK_PRICE_CANDLE_3D,
                 MARK_PRICE_CANDLE_5D,
                 MARK_PRICE_CANDLE_12H,
                 MARK_PRICE_CANDLE_6H,
                 MARK_PRICE_CANDLE_4H,
                 MARK_PRICE_CANDLE_2H,
                 MARK_PRICE_CANDLE_1H,
                 MARK_PRICE_CANDLE_30M,
                 MARK_PRICE_CANDLE_15M,
                 MARK_PRICE_CANDLE_5M,
                 MARK_PRICE_CANDLE_3M,
                 MARK_PRICE_CANDLE_1M,
                 MARK_PRICE_CANDLE_3M_UTC,
                 MARK_PRICE_CANDLE_1M_UTC,
                 MARK_PRICE_CANDLE_1W_UTC,
                 MARK_PRICE_CANDLE_1D_UTC,
                 MARK_PRICE_CANDLE_2D_UTC,
                 MARK_PRICE_CANDLE_3D_UTC,
                 MARK_PRICE_CANDLE_5D_UTC,
                 MARK_PRICE_CANDLE_2H_UTC,
                 MARK_PRICE_CANDLE_6H_UTC -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.biz.MarkPriceCandlesticksArg.class, MarkPriceCandlesticks.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(MarkPriceCandlesticksArg.class,
                            channel, message, wsChannel);
            case INDEX_CANDLE_3MON,
                 INDEX_CANDLE_1MON,
                 INDEX_CANDLE_1W,
                 INDEX_CANDLE_1D,
                 INDEX_CANDLE_2D,
                 INDEX_CANDLE_3D,
                 INDEX_CANDLE_5D,
                 INDEX_CANDLE_12H,
                 INDEX_CANDLE_6H,
                 INDEX_CANDLE_4H,
                 INDEX_CANDLE_2H,
                 INDEX_CANDLE_1H,
                 INDEX_CANDLE_30M,
                 INDEX_CANDLE_15M,
                 INDEX_CANDLE_5M,
                 INDEX_CANDLE_3M,
                 INDEX_CANDLE_1M,
                 INDEX_CANDLE_3M_UTC,
                 INDEX_CANDLE_1M_UTC,
                 INDEX_CANDLE_1W_UTC,
                 INDEX_CANDLE_1D_UTC,
                 INDEX_CANDLE_2D_UTC,
                 INDEX_CANDLE_3D_UTC,
                 INDEX_CANDLE_5D_UTC,
                 INDEX_CANDLE_2H_UTC,
                 INDEX_CANDLE_6H_UTC -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.biz.IndexCandlesticksArg.class, IndexCandlesticks.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(IndexCandlesticksArg.class,
                            channel, message, wsChannel);
            case ECONOMIC_CALENDAR -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.biz.EconomicCalendarArg.class, EconomicCalendar.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(EconomicCalendarArg.class,
                            channel, message, wsChannel);
            case ALGO_ORDERS -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.biz.AlgoOrderArg.class, AlgoOrder.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(AlgoOrderArg.class,
                            channel, message, wsChannel);
            case ADVANCE_ALGO_ORDERS -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.biz.AdvanceAlgoOrderArg.class, AdvanceAlgoOrder.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(AdvanceAlgoOrderArg.class,
                            channel, message, wsChannel);
            case GRID_ORDERS_SPOT -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.biz.GridOrderSpotArg.class, GridOrderSpot.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(GridOrderSpotArg.class,
                            channel, message, wsChannel);
            case GRID_ORDERS_CONTRACT -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.biz.GridOrderContractArg.class, GridOrderContract.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(GridOrderContractArg.class,
                            channel, message, wsChannel);
            case GRID_POSITIONS -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.biz.GridPositionsArg.class, GridPositions.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(GridPositionsArg.class,
                            channel, message, wsChannel);
            case GRID_SUB_ORDERS -> pushData ?
                    new WsSubscribePushHandler<>(ai.zzt.okx.v5.entity.ws.response.biz.GridSubOrderArg.class, GridSubOrder.class, channel, message, wsChannel) :
                    new WsSubscribeHandler<>(GridSubOrderArg.class,
                            channel, message, wsChannel);
        };
    }

}
