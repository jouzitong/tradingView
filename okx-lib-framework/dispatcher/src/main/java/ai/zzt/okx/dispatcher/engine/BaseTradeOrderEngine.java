package ai.zzt.okx.dispatcher.engine;

import ai.zzt.okx.common.context.SystemContext;
import ai.zzt.okx.common.properties.AppProperties;
import ai.zzt.okx.common.utils.ThreadUtils;
import ai.zzt.okx.dispatcher.manage.RequestManager;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.message.email.bean.DefaultMessage;
import ai.zzt.okx.message.service.INoticeService;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/6/4
 **/
@Slf4j
public abstract class BaseTradeOrderEngine implements TradeOrderEngine {

    @Resource
    protected ICalculateAnalyzeEngine calculateAnalyzeEngine;

    @Resource
    protected RequestManager requestManager;

    @Resource
    protected OrderEngine orderEngine;

    @Resource
    private INoticeService noticeService;

    @Resource
    private AppProperties appProperties;

    @Override
    public final void init(String instId) {
        synchronized (instId.intern()) {
            MarkPrice markPrice = new MarkPrice();
            markPrice.setInstId(instId);
            TradeOrderRequest request = requestManager.buildRequest(markPrice);
            calculateAnalyzeEngine.init(request);
        }
    }

    @Override
    public void delete(String instId) {
        synchronized (instId.intern()) {
            requestManager.remove(instId);
        }
    }

    @Override
    public final void dispatch(List<MarkPrice> mps) {
        for (MarkPrice mp : mps) {
            ThreadUtils.runCalculate(() -> {
                try {
                    dispatch(mp);
                } catch (Exception e) {
                    log.error("doDispatch error.", e);
                }
            });
        }
    }

    @Override
    public final void dispatch(MarkPrice mp) {
        // 一次只处理一个合约的数据. TODO 应该更细致处理, 锁粒度太大了
        try {
            synchronized (mp.getInstId().intern()) {
                doDispatch(mp);
            }
        } catch (Exception e) {
            log.error("doDispatch error", e);
            SystemContext.close();

            /*
                邮箱通知:
                    系统异常: e.getMessage
             */
            String id = "量化交易-系统异常";
            DefaultMessage message = new DefaultMessage(
                    id, id, id,
                    e.getMessage());
//            noticeService.sendMessage(id, message, appProperties.getMail().getReceivers().get(0), 1000 * 60 * 60);
        }
    }

    protected void doDispatch(MarkPrice data) {
        TradeOrderRequest request = requestManager.buildRequest(data);
        calculateAnalyzeEngine.analyze(request);
        // 判断系统是否运行, 系统关闭时, 不考虑下单
        if (!SystemContext.isRunning()) {
            log.trace("系统已关闭, 不处理数据");
            return;
        }
        orderEngine.placeOrder(request);
    }

}
