package ai.zzt.okx.okx_client.serivce;

import ai.zzt.okx.common.utils.ThreadUtils;
import ai.zzt.okx.okx_client.properties.OkxProperties;
import ai.zzt.okx.v5.entity.ws.request.WsChannelRequestArg;
import ai.zzt.okx.v5.entity.ws.request.biz.AlgoOrderArg;
import ai.zzt.okx.v5.entity.ws.request.pri.LoginArg;
import ai.zzt.okx.v5.entity.ws.request.pri.PositionsArg;
import ai.zzt.okx.v5.entity.ws.request.pub.FundingRateArg;
import ai.zzt.okx.v5.entity.ws.request.pub.MarkPriceArg;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import ai.zzt.okx.v5.enumeration.ws.WsChannel;
import ai.zzt.okx.v5.ws.OkxWsApiService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * okx websocket server 包装类
 *
 * @author zhouzhitong
 * @since 2024/5/8
 **/
@Slf4j
public class OkxWsApiServiceWrapper extends OkxWsApiService {

    private final OkxProperties okxProperties;

    private final boolean simulated;

    public OkxWsApiServiceWrapper(boolean simulated, OkxProperties okxProperties) {
        super(simulated);
        this.simulated = simulated;
        this.okxProperties = okxProperties;
    }

    /**
     * 获取已经订阅的产品列表
     *
     * @return 产品列表
     */
    public List<String> getSubMpList() {
        List<WsChannelRequestArg> subList = getSubscribeStateService().getSubList(WsChannel.PUBLIC);
        return subList.stream()
                .filter(arg -> arg instanceof MarkPriceArg)
                .map(arg -> ((MarkPriceArg) arg).getInstId())
                .toList();
    }

    /**
     * 订阅产品市场价格信息
     */
    public void subMarkPrice(String instId) {
        log.info("subMarkPrice instId: {}", instId);
        WsChannel wsChannel = WsChannel.PUBLIC;
        WsChannelRequestArg requestArg = MarkPriceArg
                .builder()
                .instId(instId)
                .build();
        super.subscribe(wsChannel, requestArg);
    }

    /**
     * 取消订阅产品市场价格信息
     */
    public void unsubMarkPrice(String instId) {
        log.info("unsubMarkPrice instId: {}", instId);
        WsChannel wsChannel = WsChannel.PUBLIC;
        WsChannelRequestArg requestArg = MarkPriceArg
                .builder()
                .instId(instId)
                .build();
        super.unsubscribe(wsChannel, requestArg);
    }

    /**
     * 订阅产品信息
     *
     * @param instId 产品Id
     */
    public void subscribeInstId(String instId) {
        log.info("subscribeInstId instId: {}", instId);
        subMarkPrice(instId);

        subscribePosition(instId);
        subscribeAlgoOrder(instId);
    }

    private void subscribeFundingRate(String instId) {
        log.info("subscribe funding rate instId: {}", instId);
        WsChannelRequestArg requestArg = FundingRateArg
                .builder()
                .instId(instId)
                .build();
        super.subscribe(WsChannel.PUBLIC, requestArg);
    }

    private void subscribePosition(String instId) {
        log.info("subscribe Position rate instId: {}", instId);

        WsChannelRequestArg requestArg = PositionsArg
                .builder()
                .instId(instId)
                .instType(InstrumentType.SWAP)
                .build();
        super.subscribe(WsChannel.PRIVATE, requestArg);
    }

    private void subscribeAlgoOrder(String instId) {
        log.info("subscribe algo order instId: {}", instId);
        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WsChannelRequestArg requestArg = AlgoOrderArg
                .builder()
                .instId(instId)
                .instType(InstrumentType.SWAP)
                .build();
        super.subscribe(WsChannel.BUSINESS, requestArg);
    }

    /**
     * 登陆
     */
    public void login() {
        OkxProperties.Api api = okxProperties.getApi(simulated);
        LoginArg loginArg = LoginArg.builder()
                .apiKey(api.getApiKey())
                .passphrase(api.getPassPhrase())
                .build();
        super.login(loginArg, api.getSecretKey());
        ThreadUtils.sleep(5);
    }

    /**
     * 取消订阅产品信息
     *
     * @param instId 产品ID
     */
    public void unsubscribeInstId(String instId) {
        log.info("unsubscribe instId: {}", instId);
        WsChannel wsChannel = WsChannel.PUBLIC;

        WsChannelRequestArg requestArg = MarkPriceArg
                .builder()
                .instId(instId)
                .build();
        super.unsubscribe(wsChannel, requestArg);
    }

}
