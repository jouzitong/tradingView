package ai.zzt.okx.okx_client;

import ai.zzt.okx.common.Application;
import ai.zzt.okx.common.base.system.Initialization;
import ai.zzt.okx.common.utils.ThreadUtils;
import ai.zzt.okx.okx_client.context.AccountContext;
import ai.zzt.okx.okx_client.context.InstrumentContext;
import ai.zzt.okx.okx_client.properties.OkxProperties;
import ai.zzt.okx.okx_client.serivce.OkxWsApiServiceWrapper;
import ai.zzt.okx.v5.api.factory.OkxRestApiFactory;
import ai.zzt.okx.v5.api.pri.AccountApi;
import ai.zzt.okx.v5.api.pub.OpenDataApi;
import ai.zzt.okx.v5.entity.rest.R;
import ai.zzt.okx.v5.entity.ws.pub.Instruments;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import ai.zzt.okx.v5.enumeration.ws.WsChannel;
import ai.zzt.okx.v5.ws.listener.WsMessageListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouzhitong
 * @since 2024/5/4
 **/
@Configuration
@Slf4j
@ComponentScan("ai.zzt.okx.okx_client")
@AllArgsConstructor
public class OkxConfiguration implements Initialization {

    private final OkxProperties okxProperties;

    private final AccountContext accountContext;

    private final OkxWsApiServiceWrapper okxWsApiService;

    private final AccountApi accountApi;

    private final WsMessageListener wsMessageListener;

    private final OpenDataApi openDataApi;

    @Override
    public int order() {
        return Initialization.HIGH;
    }

    /**
     * 主要是同步持仓和委托单信息
     */
    @Override
    public void initialize() {
        log.info("okx api initialization");
        // 判断是否需要开启代理
        if (okxProperties.isProxy()) {
            Application.disableSSLVerification();
        }
        // 同步产品信息
        initSyncInstrument();
        // 初始化 websocket 连接
        initWsApiServer();

    }

    private void initWsApiServer() {
        Set<WsChannel> supportWsChannelList = okxProperties.getSupportWsChannelList();
        if (CollectionUtils.isEmpty(supportWsChannelList)) {
            log.info("不需要与 OKX 建立 websocket 连接");
            return;
        }
        log.info("初始化与OKX的 websocket 连接");
        okxWsApiService.setWsMessageListener(wsMessageListener);
        log.info("connect ws start. channel: {}", supportWsChannelList);
        for (WsChannel channel : supportWsChannelList) {
            okxWsApiService.connect(channel);
            int count = 0;
            while (!okxWsApiService.isConnected(channel)) {
                log.info("wait {} connect result [count: {}]", channel, count);
                ThreadUtils.sleep(1000, TimeUnit.MILLISECONDS);
                if (count++ > 30) {
                    log.error("connect ws fail");
                    throw new RuntimeException("connect ws fail");
                }
            }
        }

        // 如果存在私有通道，需要登录
        if (supportWsChannelList.contains(WsChannel.PRIVATE)) {
            okxWsApiService.login();
            ThreadUtils.sleep(3000, TimeUnit.MILLISECONDS);
        }

        log.info("connect ws finish");
    }

    /**
     * 同步产品信息
     */
    private void initSyncInstrument() {
        log.info("同步所有币种信息");
        do_initSyncInstrument(InstrumentType.SWAP);
        do_initSyncInstrument(InstrumentType.SPOT);
        do_initSyncInstrument(InstrumentType.MARGIN);
        log.info("当前所有可用币种: {}", InstrumentContext.getAllEnableInstrumentsOfId(InstrumentType.SWAP));
    }

    private void do_initSyncInstrument(InstrumentType type) {
        R<Instruments> instrumentsR = OkxRestApiFactory.get(openDataApi.getInstruments(type, null));
        InstrumentContext.add(instrumentsR.getData());
    }

}
