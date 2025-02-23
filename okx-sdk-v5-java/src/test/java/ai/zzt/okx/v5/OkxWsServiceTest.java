package ai.zzt.okx.v5;

import ai.zzt.okx.v5.entity.ws.response.pri.AccountArg;
import ai.zzt.okx.v5.entity.ws.response.pri.PlaceOrderArg;
import ai.zzt.okx.v5.entity.ws.response.pub.OpenInterestArg;
import com.alibaba.fastjson2.JSON;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ai.zzt.okx.v5.entity.ws.biz.IndexCandlesticks;
import ai.zzt.okx.v5.entity.ws.pri.Account;
import ai.zzt.okx.v5.entity.ws.pub.OpenInterest;
import ai.zzt.okx.v5.entity.ws.request.WsRequest;
import ai.zzt.okx.v5.entity.ws.request.pri.LoginArg;
import ai.zzt.okx.v5.entity.ws.request.pub.InstrumentsArg;
import ai.zzt.okx.v5.entity.ws.response.ErrorResponse;
import ai.zzt.okx.v5.entity.ws.response.WsOnceResponse;
import ai.zzt.okx.v5.entity.ws.response.WsResponse;
import ai.zzt.okx.v5.entity.ws.response.WsSubscribeResponse;
import ai.zzt.okx.v5.entity.ws.response.biz.IndexCandlesticksArg;
import ai.zzt.okx.v5.enumeration.ws.Channel;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import ai.zzt.okx.v5.enumeration.ws.Operation;
import ai.zzt.okx.v5.enumeration.ws.WsChannel;
import ai.zzt.okx.v5.ws.OkxWsApiService;
import ai.zzt.okx.v5.ws.listener.WsMessageListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OkxWsServiceTest {

    private OkxWsApiService getOkxWsService() {
        return new OkxWsApiService(true);
    }

    @Test
    public void entityTest() {
        WsRequest wsRequest = WsRequest.builder()
                .op(Operation.SUBSCRIBE)
                .args(List.of(
                        InstrumentsArg.builder()
                                .channel(Channel.INSTRUMENTS)
                                .instType(InstrumentType.SPOT)
                                .build()
                )).build();
        log.info("{}: {}", wsRequest, JSON.toJSONString(wsRequest));
    }

    @Test
    public void test() throws InterruptedException {
        OkxWsApiService wsApiService = getOkxWsService();
        wsApiService.connect(WsChannel.PUBLIC);
        wsApiService.connect(WsChannel.PRIVATE);
//        wsApiService.connect(WsChannel.BUSINESS);

        wsApiService.setWsMessageListener(new WsMessageListener() {

            @Override
            public void onLoginSuccess() {
                log.info("onLoginSuccess");
            }

            @Override
            public void onOperateAccount(@NonNull WsResponse<AccountArg> response) {
                log.info("onOperateAccount: {}", JSON.toJSONString(response));
            }

            @Override
            public void onReceiveAccount(@NonNull WsSubscribeResponse<AccountArg, Account> response) {
                log.info("onReceiveAccount: {}", JSON.toJSONString(response));
            }

            @Override
            public void onOperateError(@NonNull ErrorResponse response) {
                log.info("onOperateError: {}", JSON.toJSONString(response));
            }

            @Override
            public void onReceiveOpenInterest(@NonNull WsSubscribeResponse<OpenInterestArg, OpenInterest> response) {
                log.info("onReceiveOpenInterest: {}", JSON.toJSONString(response));
            }

            @Override
            public void onOperateOpenInterest(@NonNull WsResponse<OpenInterestArg> response) {
                log.info("onOperateOpenInterest: {}", JSON.toJSONString(response));
            }

            @Override
            public void onOperateIndexCandle(@NonNull WsResponse<IndexCandlesticksArg> response) {
                log.info("onOperateIndexCandle: {}", JSON.toJSONString(response));
            }

            @Override
            public void onReceiveIndexCandle(@NonNull WsSubscribeResponse<IndexCandlesticksArg, IndexCandlesticks> response) {
                log.info("onReceiveIndexCandle: {}", JSON.toJSONString(response));
            }

            @Override
            public void onPlaceOrderResponse(@NonNull WsOnceResponse<PlaceOrderArg> response) {
                log.info("onPlaceOrderResponse: {}", JSON.toJSONString(response));
            }
        });

//        wsApiService.subscribeAccount(AccountArg.builder()
//                .ccy("BTC")
//                .build());
        TimeUnit.SECONDS.sleep(5L);
//        wsApiService.subscribeOpenInterest(OpenInterestArg.builder()
//                .instId("LTC-USD-SWAP")
//                .build());
//        TimeUnit.SECONDS.sleep(5L);
//        wsApiService.subscribeOpenInterest(OpenInterestArg.builder()
//                .instId("BTC-USD-SWAP")
//                .build());
//        TimeUnit.SECONDS.sleep(10L);
//        wsApiService.unsubscribeOpenInterest(OpenInterestArg.builder()
//                .instId("BTC-USD-SWAP")
//                .build());

//        wsApiService.subscribeOpenInterest(OpenInterestArg.builder()
//                .instId("LTC-USD-SWAP")
//                .build());
//        TimeUnit.SECONDS.sleep(10L);
//        wsApiService.unsubscribeOpenInterest(OpenInterestArg.builder()
//                .instId("LTC-USD-SWAP")
//                .build());
        TimeUnit.SECONDS.sleep(5);
        // 模拟盘
        String apiKey = System.getenv("API_KEY");
        String passphrase = System.getenv("PASSPHRASE");
        String secretKey = System.getenv("SECRET_KEY");
        wsApiService.login(LoginArg.builder()
                .apiKey(apiKey)
                .passphrase(passphrase)
                .timestamp(System.currentTimeMillis() / 1000 + "")
                .build(), secretKey);

        TimeUnit.SECONDS.sleep(5);

//        wsApiService.placeOrder("aaa", PlaceOrderArg.builder()
//                .sz(BigDecimal.ONE)
//                .ordType(OrderType.LIMIT)
//                .side(Side.SELL)
//                .tdMode(TdMode.CASH)
//                .instId("BTC-USDT")
//                .build());

//        wsApiService.subscribeAccount(AccountArg.builder()
//                .ccy("BTC")
//                .build());

//        wsApiService.subscribePositions(PositionsArg.builder()
//                .instType(InstrumentType.ANY)
//                .build());

//        wsApiService.subscribeBalanceAndPosition(BalanceAndPositionArg.builder().build());
//        wsApiService.subscribeLiquidationWarning(LiquidationWarningArg.builder()
//                        .instType(InstrumentType.ANY)
//                .build());
//        wsApiService.subscribeAccountGreeks(AccountGreeksArg.builder().build());
//        wsApiService.subscribeDepositInfo(DepositInfoArg.builder()
//                .ccy("BTC")
//                .build());

//        wsApiService.subscribeIndexCandle(IndexCandleArg.builder()
//                .channel(Channel.INDEX_CANDLE_1M)
//                .instId("BTC-USD")
//                .build());
        TimeUnit.MINUTES.sleep(30L);
    }

}
