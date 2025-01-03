package ai.zzt.okx.emulator.service.impl;

import ai.zzt.okx.storage.config.KStoreProperties;
import ai.zzt.okx.storage.engine.IKLineEngine;
import ai.zzt.okx.storage.service.KStoreService;
import ai.zzt.okx.dispatcher.engine.BaseTradeOrderEngine;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 分析策略+判断下单点位
 */
@Slf4j
@Service
public class EmulateTradeOrderEngine extends BaseTradeOrderEngine {

    @Resource
    private KStoreProperties kStoreProperties;

    @Resource
    private KStoreService kStoreService;

    private final ThreadLocal<IKLineEngine> threadLocal = new ThreadLocal<>();

    public EmulateTradeOrderEngine() {
    }

    @Override
    protected void doDispatch(MarkPrice mp) {
        TradeOrderRequest request = requestManager.buildRequest(mp);
//        IKLineEngine ikLineEngine = threadLocal.get();
//        if (ikLineEngine == null) {
//            ikLineEngine = new DefaultKLineEngine(kStoreProperties, kStoreService);
//            threadLocal.set(ikLineEngine);
//        }
//        ikLineEngine.addMp(mp);

        calculateAnalyzeEngine.analyze(request);
        orderEngine.placeOrder(request);
    }

    public void clean() {
        threadLocal.remove();
    }

}
