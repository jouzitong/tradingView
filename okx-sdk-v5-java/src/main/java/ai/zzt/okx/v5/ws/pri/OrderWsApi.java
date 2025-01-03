package ai.zzt.okx.v5.ws.pri;

import ai.zzt.okx.v5.entity.ws.request.WsRequestArg;
import ai.zzt.okx.v5.entity.ws.request.pri.AmendOrderArg;
import ai.zzt.okx.v5.entity.ws.request.pri.CancelOrderArg;
import ai.zzt.okx.v5.entity.ws.request.pri.MassCancelOrderArg;
import ai.zzt.okx.v5.entity.ws.request.pri.OrderArg;
import ai.zzt.okx.v5.entity.ws.request.pri.PlaceOrderArg;
import ai.zzt.okx.v5.enumeration.ws.Operation;
import ai.zzt.okx.v5.enumeration.ws.WsChannel;
import ai.zzt.okx.v5.ws.WsApi;

/**
 * TODO <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-trade">交易 WS API</a>
 *
 * @author Jou Ziton
 * @since 2024/5/18
 **/
public interface OrderWsApi extends WsApi {

    Object sendOnceRequest(WsChannel wsChannel, String id, Operation operation, WsRequestArg... requestArgs);

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-trade-ws-order-channel">订单频道</a>
     *
     * @param orderArg 订单参数
     */
    default void order(OrderArg orderArg) {
        order(orderArg, Operation.SUBSCRIBE);
    }

    default void order(OrderArg orderArg, Operation op) {
        operate(op, WsChannel.PRIVATE, orderArg);
    }

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-trade-ws-place-order">下单</a>
     * <p>
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-trade-ws-place-order">批量下单</a>
     *
     * @param id       请求ID
     * @param orderArg 订单参数
     */
    default void placeOrder(String id, PlaceOrderArg... orderArg) {
        if (orderArg == null || orderArg.length == 0) {
            return;
        }
        Operation op = orderArg.length == 1 ? Operation.ORDER : Operation.BATCH_ORDERS;
        sendOnceRequest(WsChannel.PRIVATE, id, op, orderArg);
    }

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-trade-ws-cancel-order">撤单</a>
     * <p>
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-trade-ws-cancel-order">批量撤单</a>
     *
     * @param id   请求ID
     * @param args 撤单参数
     */
    default void cancelOrder(String id, CancelOrderArg... args) {
        if (args == null || args.length == 0) {
            return;
        }
        Operation op = args.length == 1 ? Operation.CANCEL_ORDER : Operation.BATCH_CANCEL_ORDERS;
        sendOnceRequest(WsChannel.PRIVATE, id, op, args);
    }

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-trade-ws-amend-order">修改订单</a>
     * <p>
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-trade-ws-amend-order">批量修改订单</a>
     * <p>
     * <p>
     * 速：60次/2s
     * <p>
     * 跟单交易带单产品的限速：4次/2s
     *
     * @param id   请求ID
     * @param args 修改订单参数
     */
    default void amendOrder(String id, AmendOrderArg... args) {
        if (args == null || args.length == 0) {
            return;
        }
        Operation op = args.length == 1 ? Operation.AMEND_ORDER : Operation.BATCH_AMEND_ORDERS;
        sendOnceRequest(WsChannel.PRIVATE, id, op, args);
    }

    // TODO 撤销 MMP 订单
    default void massCancelOrders(String id, MassCancelOrderArg massCancelOrderArg) {
        sendOnceRequest(WsChannel.PRIVATE, id, Operation.MASS_CANCEL_ORDERS, massCancelOrderArg);
    }
}
