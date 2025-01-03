package ai.zzt.okx.data.dispatcher;

import ai.zzt.okx.data.service.impl.MarkPriceService;
import ai.zzt.okx.data.vo.TrendContext;
import ai.zzt.okx.okx_client.listener.event.MarkPriceEvent;
import ai.zzt.okx.dispatcher.engine.ICalculateAnalyzeEngine;
import ai.zzt.okx.dispatcher.manage.RequestManager;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/10/28
 **/
@Service
@Slf4j
public class CollectionDispatcher {

    @Resource
    private MarkPriceService markPriceService;

    @Resource
    protected ICalculateAnalyzeEngine calculateAnalyzeEngine;

    @Resource
    protected RequestManager requestManager;

    @Resource
    private final TrendContext trendContext = new TrendContext();

    public final void add(String instId) {
        // TODO 暂时不用
//        synchronized (instId.intern()) {
//            MarkPrice markPrice = new MarkPrice();
//            markPrice.setInstId(instId);
//            TradeOrderRequest request = requestManager.buildRequest(markPrice);
//            calculateAnalyzeEngine.init(request);
//        }
    }

    @EventListener
    public void onReceiveMarkPrice(MarkPriceEvent event) {
        try {
            List<MarkPrice> data = event.markPrices();
            markPriceService.addAll(data);
//            for (MarkPrice mp : data) {
//                TradeOrderRequest request = requestManager.buildRequest(mp);
//                calculateAnalyzeEngine.analyze(request);
//                TrendType analyzeTrend = request.getAnalyzeContext().getAnalyzeTrend();
//                trendContext.update(mp.getInstId(), analyzeTrend);
//            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

}
