package ai.zzt.okx.dispatcher.service;

import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;

/**
 * @author zhouzhitong
 * @since 2024/5/28
 **/
public interface OrderService {

    InstrumentType type();

    /**
     * 开仓
     * <p>
     * 为了安全考虑, 应该在下单前, 先获取账户信息, 还有下单的时间
     *
     * @param request 下单请求
     * @return 是否成功
     */
    boolean placeOrder(TradeOrderRequest request);

    /**
     * 平仓
     *
     * @param request 平仓请求
     * @return 是否成功
     */
    boolean closeOrder(TradeOrderRequest request);

}
