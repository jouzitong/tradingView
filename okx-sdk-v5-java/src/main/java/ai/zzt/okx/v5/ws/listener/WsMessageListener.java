package ai.zzt.okx.v5.ws.listener;

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
import ai.zzt.okx.v5.entity.ws.response.ErrorResponse;
import ai.zzt.okx.v5.entity.ws.response.WsOnceResponse;
import ai.zzt.okx.v5.entity.ws.response.WsResponse;
import ai.zzt.okx.v5.entity.ws.response.WsSubscribeResponse;
import ai.zzt.okx.v5.entity.ws.response.biz.AdvanceAlgoOrderArg;
import ai.zzt.okx.v5.entity.ws.response.biz.AlgoOrderArg;
import ai.zzt.okx.v5.entity.ws.response.biz.DepositInfoArg;
import ai.zzt.okx.v5.entity.ws.response.biz.EconomicCalendarArg;
import ai.zzt.okx.v5.entity.ws.response.biz.GridOrderContractArg;
import ai.zzt.okx.v5.entity.ws.response.biz.GridOrderSpotArg;
import ai.zzt.okx.v5.entity.ws.response.biz.GridPositionsArg;
import ai.zzt.okx.v5.entity.ws.response.biz.GridSubOrderArg;
import ai.zzt.okx.v5.entity.ws.response.biz.IndexCandlesticksArg;
import ai.zzt.okx.v5.entity.ws.response.biz.MarkPriceCandlesticksArg;
import ai.zzt.okx.v5.entity.ws.response.biz.WithdrawalInfoArg;
import ai.zzt.okx.v5.entity.ws.response.pri.AccountArg;
import ai.zzt.okx.v5.entity.ws.response.pri.AccountGreeksArg;
import ai.zzt.okx.v5.entity.ws.response.pri.AmendOrderArg;
import ai.zzt.okx.v5.entity.ws.response.pri.BalanceAndPositionArg;
import ai.zzt.okx.v5.entity.ws.response.pri.CancelOrderArg;
import ai.zzt.okx.v5.entity.ws.response.pri.LiquidationWarningArg;
import ai.zzt.okx.v5.entity.ws.response.pri.MassCancelOrderArg;
import ai.zzt.okx.v5.entity.ws.response.pri.OrderArg;
import ai.zzt.okx.v5.entity.ws.response.pri.PlaceOrderArg;
import ai.zzt.okx.v5.entity.ws.response.pri.PositionsArg;
import ai.zzt.okx.v5.entity.ws.response.pub.AdlWarningArg;
import ai.zzt.okx.v5.entity.ws.response.pub.EstimatedPriceArg;
import ai.zzt.okx.v5.entity.ws.response.pub.FundingRateArg;
import ai.zzt.okx.v5.entity.ws.response.pub.IndexTickersArg;
import ai.zzt.okx.v5.entity.ws.response.pub.InstrumentsArg;
import ai.zzt.okx.v5.entity.ws.response.pub.LiquidationOrdersArg;
import ai.zzt.okx.v5.entity.ws.response.pub.MarkPriceArg;
import ai.zzt.okx.v5.entity.ws.response.pub.OpenInterestArg;
import ai.zzt.okx.v5.entity.ws.response.pub.OptionSummaryArg;
import ai.zzt.okx.v5.entity.ws.response.pub.PriceLimitArg;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * handle all message from server
 */
@Slf4j
public abstract class WsMessageListener {

    public void onOperateError(@NonNull ErrorResponse response) {
        log.debug("onOperateError: {}", response);
        log.debug("operate error, please ref https://www.okx.com/docs-v5/zh/#error-code-websocket.");
    }

    public void onLoginSuccess() {
        log.debug("onLoginSuccess");
    }

    // private

    public void onOperateAccount(@NonNull WsResponse<AccountArg> response) {
        log.debug("onOperateAccount: {}", response);
    }

    public void onReceiveAccount(@NonNull WsSubscribeResponse<AccountArg, Account> response) {
        log.debug("onReceiveAccount: {}", response);
    }

    public void onOperatePositions(@NonNull WsResponse<PositionsArg> response) {
        log.debug("onOperatePositions: {}", response);
    }

    public void onReceivePositions(@NonNull WsSubscribeResponse<PositionsArg, Positions> response) {
        log.debug("onReceivePositions: {}", response);
    }

    public void onOperateBalanceAndPosition(@NonNull WsResponse<BalanceAndPositionArg> response) {
        log.debug("onOperateBalanceAndPosition: {}", response);
    }

    public void onReceiveBalanceAndPosition(@NonNull WsSubscribeResponse<BalanceAndPositionArg, BalanceAndPosition> response) {
        log.debug("onReceiveBalanceAndPosition: {}", response);
    }

    public void onOperateLiquidationWarning(@NonNull WsResponse<LiquidationWarningArg> response) {
        log.debug("onOperateLiquidationWarning: {}", response);
    }

    public void onReceiveLiquidationWarning(@NonNull WsSubscribeResponse<LiquidationWarningArg, LiquidationWarning> response) {
        log.debug("onReceiveLiquidationWarning: {}", response);
    }

    public void onOperateAccountGreeks(@NonNull WsResponse<AccountGreeksArg> response) {
        log.debug("onOperateAccountGreeks: {}", response);
    }

    public void onReceiveAccountGreeks(@NonNull WsSubscribeResponse<AccountGreeksArg, AccountGreeks> response) {
        log.debug("onReceiveAccountGreeks: {}", response);
    }

    public void onOperateOrders(@NonNull WsResponse<OrderArg> response) {
        log.debug("onOperateOrders: {}", response);
    }

    public void onReceiveOrders(@NonNull WsSubscribeResponse<OrderArg, Order> response) {
        log.debug("onReceiveOrders: {}", response);
    }

    // public

    public void onOperateInstruments(WsResponse<InstrumentsArg> response) {
        log.debug("onOperateInstruments: {}", response);
    }

    public void onReceiveInstruments(@NonNull WsSubscribeResponse<InstrumentsArg, Instruments> response) {
        log.debug("onReceiveInstruments: {}", response);
    }

    public void onOperateOpenInterest(@NonNull WsResponse<OpenInterestArg> response) {
        log.debug("onOperateOpenInterest: {}", response);
    }

    public void onReceiveOpenInterest(@NonNull WsSubscribeResponse<OpenInterestArg, OpenInterest> response) {
        log.debug("onReceiveOpenInterest: {}", response);
    }

    public void onOperateFundingRate(@NonNull WsResponse<FundingRateArg> response) {
        log.debug("onOperateFundingRate: {}", response);
    }

    public void onReceiveFundingRate(@NonNull WsSubscribeResponse<FundingRateArg, FundingRate> response) {
        log.debug("onReceiveFundingRate: {}", response);
    }

    public void onOperatePriceLimit(@NonNull WsResponse<PriceLimitArg> response) {
        log.debug("onOperatePriceLimit: {}", response);
    }

    public void onReceivePriceLimit(@NonNull WsSubscribeResponse<PriceLimitArg, PriceLimit> response) {
        log.debug("onReceivePriceLimit: {}", response);
    }

    public void onOperateOptSummary(@NonNull WsResponse<OptionSummaryArg> response) {
        log.debug("onOperateOptSummary: {}", response);
    }

    public void onReceiveOptSummary(@NonNull WsSubscribeResponse<OptionSummaryArg, OptionSummary> response) {
        log.debug("onReceiveOptSummary: {}", response);
    }

    public void onOperateEstimatedPrice(@NonNull WsResponse<EstimatedPriceArg> response) {
        log.debug("onOperateEstimatedPrice: {}", response);
    }

    public void onReceiveEstimatedPrice(@NonNull WsSubscribeResponse<EstimatedPriceArg, EstimatedPrice> response) {
        log.debug("onReceiveEstimatedPrice: {}", response);
    }

    public void onOperateMarkPrice(@NonNull WsResponse<MarkPriceArg> response) {
        log.debug("onOperateMarkPrice: {}", response);
    }

    public void onReceiveMarkPrice(@NonNull WsSubscribeResponse<MarkPriceArg, MarkPrice> response) {
        log.debug("onReceiveMarkPrice: {}", response);
    }

    public void onOperateIndexTickers(@NonNull WsResponse<IndexTickersArg> response) {
        log.debug("onOperateIndexTickers: {}", response);
    }

    public void onReceiveIndexTickers(@NonNull WsSubscribeResponse<IndexTickersArg, IndexTickers> response) {
        log.debug("onReceiveIndexTickers: {}", response);
    }

    public void onOperateLiquidationOrders(@NonNull WsResponse<LiquidationOrdersArg> response) {
        log.debug("onOperateLiquidationOrders: {}", response);
    }

    public void onReceiveLiquidationOrders(@NonNull WsSubscribeResponse<LiquidationOrdersArg, LiquidationOrders> response) {
        log.debug("onReceiveLiquidationOrders: {}", response);
    }

    public void onOperateAdlWarning(@NonNull WsResponse<AdlWarningArg> response) {
        log.debug("onOperateAdlWarning: {}", response);
    }

    public void onReceiveAdlWarning(@NonNull WsSubscribeResponse<AdlWarningArg, AdlWarning> response) {
        log.debug("onReceiveAdlWarning: {}", response);
    }

    // biz

    public void onOperateDepositInfo(WsResponse<DepositInfoArg> response) {
        log.debug("onOperateDepositInfo: {}", response);
    }

    public void onReceiveDepositInfo(@NonNull WsSubscribeResponse<DepositInfoArg, DepositInfo> response) {
        log.debug("onReceiveDepositInfo: {}", response);
    }

    public void onOperateWithdrawalInfo(@NonNull WsResponse<WithdrawalInfoArg> response) {
        log.debug("onOperateWithdrawalInfo: {}", response);
    }

    public void onReceiveWithdrawalInfo(@NonNull WsSubscribeResponse<WithdrawalInfoArg, WithdrawalInfo> response) {
        log.debug("onReceiveWithdrawalInfo: {}", response);
    }

    public void onOperateMarkPriceCandle(@NonNull WsResponse<MarkPriceCandlesticksArg> response) {
        log.debug("onOperateMarkPriceCandle: {}", response);
    }

    public void onReceiveMarkPriceCandle(@NonNull WsSubscribeResponse<MarkPriceCandlesticksArg, MarkPriceCandlesticks> response) {
        log.debug("onReceiveMarkPriceCandle: {}", response);
    }

    public void onOperateIndexCandle(@NonNull WsResponse<IndexCandlesticksArg> response) {
        log.debug("onOperateIndexCandle: {}", response);
    }

    public void onReceiveIndexCandle(@NonNull WsSubscribeResponse<IndexCandlesticksArg, IndexCandlesticks> response) {
        log.debug("onReceiveIndexCandle: {}", response);
    }

    public void onOperateEconomicCalendar(@NonNull WsResponse<EconomicCalendarArg> response) {
        log.debug("onOperateEconomicCalendar: {}", response);
    }

    public void onReceiveEconomicCalendar(@NonNull WsSubscribeResponse<EconomicCalendarArg, EconomicCalendar> response) {
        log.debug("onReceiveEconomicCalendar: {}", response);
    }

    public void onOperateAlgoOrders(@NonNull WsResponse<AlgoOrderArg> response) {
        log.debug("onOperateAlgoOrders: {}", response);
    }

    public void onReceiveAlgoOrders(@NonNull WsSubscribeResponse<AlgoOrderArg, AlgoOrder> response) {
        log.debug("onReceiveAlgoOrders: {}", response);
    }

    public void onOperateAdvanceAlgoOrders(@NonNull WsResponse<AdvanceAlgoOrderArg> response) {
        log.debug("onOperateAdvanceAlgoOrders: {}", response);
    }

    public void onReceiveAdvanceAlgoOrders(@NonNull WsSubscribeResponse<AdvanceAlgoOrderArg, AdvanceAlgoOrder> response) {
        log.debug("onReceiveAdvanceAlgoOrders: {}", response);
    }

    public void onOperateGridOrdersSpot(@NonNull WsResponse<GridOrderSpotArg> response) {
        log.debug("onOperateGridOrdersSpot: {}", response);
    }

    public void onReceiveGridOrdersSpot(@NonNull WsSubscribeResponse<GridOrderSpotArg, GridOrderSpot> response) {
        log.debug("onReceiveGridOrdersSpot: {}", response);
    }

    public void onOperateGridOrdersContract(@NonNull WsResponse<GridOrderContractArg> response) {
        log.debug("onOperateGridOrdersContract: {}", response);
    }

    public void onReceiveGridOrdersContract(@NonNull WsSubscribeResponse<GridOrderContractArg, GridOrderContract> response) {
        log.debug("onReceiveGridOrdersContract: {}", response);
    }

    public void onOperateGridPositions(@NonNull WsResponse<GridPositionsArg> response) {
        log.debug("onOperateGridPositions: {}", response);
    }

    public void onReceiveGridPositions(@NonNull WsSubscribeResponse<GridPositionsArg, GridPositions> response) {
        log.debug("onReceiveGridPositions: {}", response);
    }

    public void onOperateGridSubOrders(@NonNull WsResponse<GridSubOrderArg> response) {
        log.debug("onOperateGridSubOrders: {}", response);
    }

    public void onReceiveGridSubOrders(@NonNull WsSubscribeResponse<GridSubOrderArg, GridSubOrder> response) {
        log.debug("onReceiveGridSubOrders: {}", response);
    }

    // once response

    // private
    public void onPlaceOrderResponse(@NonNull WsOnceResponse<PlaceOrderArg> response) {
        log.debug("onPlaceOrderResponse: {}", response);
    }

    public void onBatchPlaceOrdersResponse(@NonNull WsOnceResponse<PlaceOrderArg> response) {
        log.debug("onBatchPlaceOrdersResponse: {}", response);
    }

    public void onCancelOrderResponse(@NonNull WsOnceResponse<CancelOrderArg> response) {
        log.debug("onCancelOrderResponse: {}", response);
    }

    public void onBatchCancelOrdersResponse(@NonNull WsOnceResponse<CancelOrderArg> response) {
        log.debug("onBatchCancelOrdersResponse: {}", response);
    }

    public void onAmendOrderResponse(@NonNull WsOnceResponse<AmendOrderArg> response) {
        log.debug("onAmendOrderResponse: {}", response);
    }

    public void onBatchAmendOrdersResponse(@NonNull WsOnceResponse<AmendOrderArg> response) {
        log.debug("onBatchAmendOrdersResponse: {}", response);
    }

    public void onMassCancelOrdersResponse(@NonNull WsOnceResponse<MassCancelOrderArg> response) {
        log.debug("onMassCancelOrdersResponse: {}", response);
    }

}
