package ai.zzt.okx.tradeOrder.engine.order;

import ai.zzt.okx.calculate.context.AnalyzeContext;
import ai.zzt.okx.common.enums.TrendType;
import ai.zzt.okx.dispatcher.engine.impl.BaseOrderEngine;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.dispatcher.service.EmailMessageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * 下单引擎 V4
 * <pre>
 * 改进:
 *    1. 对placeOrderSettings改进的适配, 为了支持重复下单
 *    2. 同时持多和空仓逻辑
 *    3. 实现只支持买多或买空逻辑
 *
 * 存在的问题:
 *    1. 由于目前都是市价单, 一般不存在没有完成的订单. 等下一个版本再写相关逻辑
 * </pre>
 *
 * @author zhouzhitong
 * @since 2024/6/5
 **/
@Slf4j
public class OrderEngineV4 extends BaseOrderEngine {

    @Resource
    private EmailMessageService emailMessageService;

    @Override
    public void placeOrder(TradeOrderRequest request) {
        String instId = request.getInstId();
        AnalyzeContext analyzeContext = request.getAnalyzeContext();
        TrendType analyzeTrend = analyzeContext.getAnalyzeTrend();
        // 判断是否发邮箱通知. 不要立即发送, 放到一个队列中, 启动另一个线程发送具体的邮箱
        emailMessageService.addMessage(instId, analyzeTrend);
        super.placeOrder(request);
    }

}
