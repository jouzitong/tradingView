package ai.zzt.okx.dispatcher.engine;

import ai.zzt.okx.dispatcher.request.TradeOrderRequest;

/**
 * @author zhouzhitong
 * @since 2024/6/4
 **/
public interface OrderEngine {

    /**
     * 下单
     *
     * @param request 下单请求
     */
    void placeOrder(TradeOrderRequest request);

}
