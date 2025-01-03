package ai.zzt.okx.dispatcher.engine;

import ai.zzt.okx.dispatcher.request.TradeOrderRequest;

/**
 * @author zhouzhitong
 * @since 2024/6/4
 **/
public interface ICalculateAnalyzeEngine {

    /**
     * 初始化计算分析引擎
     * @param request 下单请求
     */
    void init(TradeOrderRequest request);

    /**
     * 开始计算分析
     *
     * @param request 下单请求
     */
    void analyze(TradeOrderRequest request);

}
