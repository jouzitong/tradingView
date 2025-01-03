package ai.zzt.okx.v5.ws;

import ai.zzt.okx.v5.entity.ws.request.WsChannelRequestArg;
import ai.zzt.okx.v5.entity.ws.request.WsOnceRequest;
import ai.zzt.okx.v5.entity.ws.request.WsRequest;
import ai.zzt.okx.v5.entity.ws.request.WsRequestArg;
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
import ai.zzt.okx.v5.entity.ws.request.pri.LoginArg;
import ai.zzt.okx.v5.entity.ws.request.pub.EstimatedPriceArg;
import ai.zzt.okx.v5.entity.ws.request.pub.OptionSummaryArg;
import ai.zzt.okx.v5.enumeration.ws.Operation;
import ai.zzt.okx.v5.enumeration.ws.WsChannel;
import ai.zzt.okx.v5.ws.factory.OkxWsApiFactory;
import ai.zzt.okx.v5.ws.listener.BusinessWsListener;
import ai.zzt.okx.v5.ws.listener.PrivateWsListener;
import ai.zzt.okx.v5.ws.listener.PublicWsListener;
import ai.zzt.okx.v5.ws.listener.WsMessageListener;
import com.alibaba.fastjson2.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import java.time.Duration;
import java.util.List;

@Slf4j
public class OkxWsApiService extends BaseOkxWsApiService {

    @Getter
    private final SubscribeStateService subscribeStateService;

    @Setter
    @Getter
    private WsMessageListener wsMessageListener;

    public OkxWsApiService() {
        this(false);
    }

    public OkxWsApiService(boolean simulated) {
        this(OkxWsApiFactory.defaultClient(Duration.ofSeconds(PING_INTERVAL_SEC)), simulated);
    }

    public OkxWsApiService(final OkHttpClient client, boolean simulated) {
        super(client, simulated);
        this.subscribeStateService = new SubscribeStateService(this);
        for (WsChannel c : WsChannel.values()) {
            this.reconnectCountMap.put(c, 0);
            this.isConnectMap.put(c, false);
        }
    }

    /**
     * connect ws
     *
     * @param wsChannel ws channel
     */
    public void connect(final WsChannel wsChannel) {
        if (wsChannel == null) {
            return;
        }
        synchronized (wsChannel) {
            if (isConnected(wsChannel)) {
                log.info("ws connected: {}", wsChannel);
                return;
            }
            client.newWebSocket(OkxWsApiFactory.defaultRequest(wsChannel, simulated),
                    switch (wsChannel) {
                        case PUBLIC -> new PublicWsListener(this);
                        case PRIVATE -> new PrivateWsListener(this);
                        case BUSINESS -> new BusinessWsListener(this);
                    });
        }
    }

    // below is the api logic
    @Override
    public Object subscribe(WsChannel wsChannel, WsChannelRequestArg requestArg) {
        if (!subscribeStateService.hasSubscribed(wsChannel, requestArg)) {
            pureSubscribe(wsChannel, requestArg);
            subscribeStateService.addSubscribed(wsChannel, requestArg);
        } else {
            log.error("subscribe already exists: {} {}", wsChannel, requestArg);
        }
        return null;
    }

    // 一次性订阅, 如果重新连接就需要重新手动订阅
    public void pureSubscribe(WsChannel wsChannel, WsChannelRequestArg requestArg) {
        WsRequest<WsRequestArg> wsRequest = WsRequest.builder()
                .op(Operation.SUBSCRIBE)
                .args(List.of(requestArg)).build();
        send(wsChannel, JSON.toJSONString(wsRequest));
    }

    @Override
    public Object unsubscribe(WsChannel wsChannel, WsChannelRequestArg requestArg) {
        WsRequest<WsRequestArg> wsRequest = WsRequest.builder()
                .op(Operation.UNSUBSCRIBE)
                .args(List.of(requestArg)).build();
        send(wsChannel, JSON.toJSONString(wsRequest));
        return null;
    }

    @Override
    public Object sendOnceRequest(WsChannel wsChannel, String id, Operation operation, WsRequestArg... wsRequestArgs) {
        WsOnceRequest<WsRequestArg> wsOnceRequest = WsOnceRequest.builder()
                .id(id)
                .op(operation)
                .args(List.of(wsRequestArgs)).build();
        send(wsChannel, JSON.toJSONString(wsOnceRequest));
        return null;
    }

    /**
     * 手动重新连接
     *
     * @param wsChannel 通道
     */
    @SneakyThrows
    public boolean manualReconnect(WsChannel wsChannel) {
        log.info("manual reconnect [{}] channel. now state: {}", wsChannel, isConnected(wsChannel));
        if (!isConnected(wsChannel)) {
            if (wsChannel == WsChannel.PRIVATE) {
                this.hasLogin = false;
            }
            connect(wsChannel);
        }
        return isConnected(wsChannel);
    }

    //********************************** private channel
    @Override
    public void login(LoginArg loginArg, String secretKey) {
        super.login(loginArg, secretKey);
        subscribeStateService.saveLoginInfo(loginArg.getApiKey(), loginArg.getPassphrase(), secretKey);
    }


    //********************************** public channel

    /**
     * option summary channel
     *
     * @param optionSummaryArg arg
     */
    // 暂时不用
    public void subscribeOptionSummary(OptionSummaryArg optionSummaryArg) {
        subscribe(WsChannel.PUBLIC, optionSummaryArg);
    }

    public void unsubscribeOptionSummary(OptionSummaryArg optionSummaryArg) {
        unsubscribe(WsChannel.PUBLIC, optionSummaryArg);
    }

    /**
     * Estimated delivery/exercise price channel
     *
     * @param estimatedPriceArg arg
     */
    // 暂时不用
    public void subscribeEstimatedPrice(EstimatedPriceArg estimatedPriceArg) {
        subscribe(WsChannel.PUBLIC, estimatedPriceArg);
    }

    public void unsubscribeEstimatedPrice(EstimatedPriceArg estimatedPriceArg) {
        unsubscribe(WsChannel.PUBLIC, estimatedPriceArg);
    }

    //********************************** business channel

    /**
     * Deposit info channel
     *
     * @param depositInfoArg depositInfoArg
     */
    public void subscribeDepositInfo(DepositInfoArg depositInfoArg) {
        subscribe(WsChannel.BUSINESS, depositInfoArg);
    }

    public void unsubscribeDepositInfo(DepositInfoArg depositInfoArg) {
        unsubscribe(WsChannel.BUSINESS, depositInfoArg);
    }

    /**
     * Withdrawal info channel
     *
     * @param withdrawalInfoArg withdrawalInfoArg
     */
    public void subscribeWithdrawalInfo(WithdrawalInfoArg withdrawalInfoArg) {
        subscribe(WsChannel.BUSINESS, withdrawalInfoArg);
    }

    public void unsubscribeWithdrawalInfo(WithdrawalInfoArg withdrawalInfoArg) {
        unsubscribe(WsChannel.BUSINESS, withdrawalInfoArg);
    }

    /**
     * Mark price candlesticks channel
     *
     * @param markPriceCandlesticksArg markPriceCandlesticksArg
     */
    public void subscribeMarkPriceCandlesticks(MarkPriceCandlesticksArg markPriceCandlesticksArg) {
        subscribe(WsChannel.BUSINESS, markPriceCandlesticksArg);
    }

    public void unsubscribeMarkPriceCandlesticks(MarkPriceCandlesticksArg markPriceCandlesticksArg) {
        unsubscribe(WsChannel.BUSINESS, markPriceCandlesticksArg);
    }

    /**
     * Index candlesticks channel
     *
     * @param indexCandlesticksArg indexCandlesticksArg
     */
    public void subscribeIndexCandlesticks(IndexCandlesticksArg indexCandlesticksArg) {
        subscribe(WsChannel.BUSINESS, indexCandlesticksArg);
    }

    public void unsubscribeIndexCandlesticks(IndexCandlesticksArg indexCandlesticksArg) {
        unsubscribe(WsChannel.BUSINESS, indexCandlesticksArg);
    }

    /**
     * Economic calendar channel
     *
     * @param economicCalendarArg economicCalendarArg
     */
    public void subscribeEconomicCalendar(EconomicCalendarArg economicCalendarArg) {
        subscribe(WsChannel.BUSINESS, economicCalendarArg);
    }

    public void unsubscribeEconomicCalendar(EconomicCalendarArg economicCalendarArg) {
        unsubscribe(WsChannel.BUSINESS, economicCalendarArg);
    }

    /**
     * WS / Algo orders channel
     *
     * @param algoOrderArg algoOrderArg
     */
    public void subscribeAlgoOrder(AlgoOrderArg algoOrderArg) {
        subscribe(WsChannel.BUSINESS, algoOrderArg);
    }

    public void unsubscribeAlgoOrder(AlgoOrderArg algoOrderArg) {
        unsubscribe(WsChannel.BUSINESS, algoOrderArg);
    }

    /**
     * WS / Advance algo orders channel
     *
     * @param advanceAlgoOrderArg advanceAlgoOrderArg
     */
    public void subscribeAdvanceAlgoOrder(AdvanceAlgoOrderArg advanceAlgoOrderArg) {
        subscribe(WsChannel.BUSINESS, advanceAlgoOrderArg);
    }

    public void unsubscribeAdvanceAlgoOrder(AdvanceAlgoOrderArg advanceAlgoOrderArg) {
        unsubscribe(WsChannel.BUSINESS, advanceAlgoOrderArg);
    }

    /**
     * WS / Spot grid algo orders channel
     *
     * @param gridOrderSpotArg gridOrderSpotArg
     */
    public void subscribeGridOrderSpot(GridOrderSpotArg gridOrderSpotArg) {
        subscribe(WsChannel.BUSINESS, gridOrderSpotArg);
    }

    public void unsubscribeGridOrderSpot(GridOrderSpotArg gridOrderSpotArg) {
        unsubscribe(WsChannel.BUSINESS, gridOrderSpotArg);
    }

    /**
     * WS / Contract grid algo orders channel
     *
     * @param gridOrderContractArg gridOrderContractArg
     */
    public void subscribeGridOrderContract(GridOrderContractArg gridOrderContractArg) {
        subscribe(WsChannel.BUSINESS, gridOrderContractArg);
    }

    public void unsubscribeGridOrderContract(GridOrderContractArg gridOrderContractArg) {
        unsubscribe(WsChannel.BUSINESS, gridOrderContractArg);
    }

    /**
     * WS / Grid positions channel
     *
     * @param gridPositionsArg gridPositionsArg
     */
    public void subscribeGridPositions(GridPositionsArg gridPositionsArg) {
        subscribe(WsChannel.BUSINESS, gridPositionsArg);
    }

    public void unsubscribeGridPositions(GridPositionsArg gridPositionsArg) {
        unsubscribe(WsChannel.BUSINESS, gridPositionsArg);
    }

    /**
     * WS / Grid sub orders channel
     *
     * @param gridSubOrderArg gridSubOrderArg
     */
    public void subscribeGridSubOrder(GridSubOrderArg gridSubOrderArg) {
        subscribe(WsChannel.BUSINESS, gridSubOrderArg);
    }

    public void unsubscribeGridSubOrder(GridSubOrderArg gridSubOrderArg) {
        unsubscribe(WsChannel.BUSINESS, gridSubOrderArg);
    }

}