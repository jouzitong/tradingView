package ai.zzt.okx.v5.api.pri;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ai.zzt.okx.v5.api.OkxPriApi;
import ai.zzt.okx.v5.entity.rest.R;
import ai.zzt.okx.v5.entity.rest.trading.order.booking.req.AmendOrderReq;
import ai.zzt.okx.v5.entity.rest.trading.order.booking.req.CloseOrderReq;
import ai.zzt.okx.v5.entity.rest.trading.order.booking.rsp.AmendOrderRsp;
import ai.zzt.okx.v5.entity.rest.trading.order.booking.req.CancelOrderReq;
import ai.zzt.okx.v5.entity.rest.trading.order.booking.rsp.CancelOrderRsp;
import ai.zzt.okx.v5.entity.rest.trading.order.booking.req.PlaceOrderReq;
import ai.zzt.okx.v5.entity.rest.trading.order.booking.rsp.PlaceOrderRsp;
import ai.zzt.okx.v5.entity.ws.pri.Order;

/**
 * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-trade">订单客户端</a>
 *
 * @author Jouziton
 * @since 2024/5/16
 **/
public interface OrderApi extends OkxPriApi {

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-trade-post-place-order">下单</a>
     * <pre>
     * 限速：60次/2s
     * 跟单交易带单产品的限速：4次/2s
     * 限速规则（期权以外）：UserID + Instrument ID
     * 限速规则（只限期权）：UserID + Instrument Family
     * </pre>
     *
     * @param request request
     * @return trade order response
     */
    @POST("/api/v5/trade/order")
    Single<R<PlaceOrderRsp>> placeOrder(@Body PlaceOrderReq request);

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-trade-post-place-multiple-orders">批量下单</a>
     * <p>
     * TODO 还没验证
     *
     * @param requests
     * @return
     */
    Single<R<PlaceOrderRsp>> batchOrder(@Body PlaceOrderReq[] requests);

    /**
     * 撤单
     * 撤销之前下的未完成订单。
     * <p>
     * 限速：60次/2s
     * 限速规则（期权以外）：UserID + Instrument ID
     * 限速规则（只限期权）：UserID + Instrument Family
     *
     * @param request request
     * @return CancelOrderRsp
     */
    @POST("/api/v5/trade/cancel-order")
    Single<R<CancelOrderRsp>> cancelOrder(@Body CancelOrderReq request);

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-trade-post-amend-order">修改订单</a>
     *
     * @param request request
     * @return AmendOrderRsp
     */
    Single<R<AmendOrderRsp>> amendOrder(@Body AmendOrderReq request);

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-trade-post-amend-multiple-orders">市价平仓</a>
     * <p>
     * 市价平掉指定交易产品的持仓
     * <p>
     * 限速：20次/2s
     * 限速规则（期权以外）：UserID + Instrument ID
     * 限速规则（只限期权）：UserID + Instrument Family
     */
    @POST("/api/v5/trade/close-position")
    Single<R<PlaceOrderRsp>> closePosition(@Body CloseOrderReq placeOrderReq);

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-trade-get-order-details">获取订单信息</a>
     *
     * @param instId  交易对
     * @param ordId   订单ID
     * @param clOrdId 客户自定义订单ID
     * @return 订单信息
     */
    @POST("/api/v5/trade/order")
    Single<R<Order>> getOrder(@Query("instId") String instId, @Query("ordId") String ordId, @Query("clOrdId") String clOrdId);

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-trade-get-order-list">获取未成交订单列表</a>
     *
     * @return 未成交订单列表
     */
    @GET("/api/v5/trade/orders-pending")
    Single<R<Order>> getOrdersPending();

}
