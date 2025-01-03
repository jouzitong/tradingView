package ai.zzt.okx.v5.api.pri;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ai.zzt.okx.v5.api.OkxPriApi;
import ai.zzt.okx.v5.entity.rest.R;
import ai.zzt.okx.v5.entity.rest.policy.trade.req.MoveStopProfitAndLossReq;
import ai.zzt.okx.v5.entity.rest.policy.trade.req.PlanDelegationReq;
import ai.zzt.okx.v5.entity.rest.policy.trade.req.PolicyAmendAlgoReq;
import ai.zzt.okx.v5.entity.rest.policy.trade.req.PolicyDelegationOrderReq;
import ai.zzt.okx.v5.entity.rest.policy.trade.req.StopProfitAndLossReq;
import ai.zzt.okx.v5.entity.rest.policy.trade.req.TimeWeightReq;
import ai.zzt.okx.v5.entity.rest.policy.trade.req.TradeAlgoReq;
import ai.zzt.okx.v5.entity.rest.policy.trade.rsp.PolicyDelegationRsp;
import ai.zzt.okx.v5.entity.rest.policy.trade.rsp.rsp.PolicyAmendRsp;
import ai.zzt.okx.v5.entity.rest.policy.trade.rsp.rsp.PolicyOrderAlgoRsp;

import java.util.List;

/**
 * TODO <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-algo-trading">策略交易</a>
 *
 * @author zhouzhitong
 * @since 2024/5/18
 **/
public interface OrderAlgoApi extends OkxPriApi {

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-algo-trading-post-place-algo-order">策略下单</a>
     *
     * @param req 策略下单请求体
     * @return 策略下单响应体
     * @see StopProfitAndLossReq 止盈止损委托
     * @see PlanDelegationReq 计划委托
     * @see TimeWeightReq 时间加权委托
     * @see MoveStopProfitAndLossReq 移动止盈止损委托
     *
     */
    @POST("/api/v5/trade/order-algo")
    Single<R<PolicyDelegationRsp>> placeOrder(@Body PolicyDelegationOrderReq req);

    @POST("/api/v5/trade/order-algo")
    Single<R<PolicyDelegationRsp>> placeOrder(@Body StopProfitAndLossReq req);

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-algo-trading-post-cancel-algo-order">撤销策略委托订单</a>
     * <p>
     *     撤销策略委托订单（不包含冰山委托、时间加权、移动止盈止损等高级策略订单），每次最多可以撤销10个策略委托单
     *
     *      限速：20次/2s
     *      限速规则（期权以外）：UserID + Instrument ID
     *      限速规则（只限期权）：UserID + Instrument Family
     * </p>
     * @param req 撤销策略委托订单
     * @return 策略下单响应体
     *
     */
    @POST("/api/v5/trade/cancel-algos")
    Single<R<PolicyDelegationRsp>> cancelAlgos(@Body List<TradeAlgoReq> req);

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-algo-trading-post-amend-algo-order">修改策略委托订单</a>
     * <p>
     * 修改策略委托订单（仅支持止盈止损单，不包含计划委托、冰山委托、时间加权、移动止盈止损等订单）
     * 只适用于交割和永续合约。
     *
     *  限速：20次/2s
     *  限速规则：UserID + Instrument ID
     * </p>
     * @param req 修改策略委托订单
     * @return 策略下单响应体
     *
     */
    @POST("/api/v5/trade/amend-algos")
    Single<R<PolicyAmendRsp>> amendAlgos(@Body PolicyAmendAlgoReq req);

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-algo-trading-post-cancel-advance-algo-order"> 撤销高级策略委托订单</a>
     * <p>
     * 撤销冰山委托、时间加权、移动止盈止损委托订单，每次最多可以撤销10个策略委托单
     * 限速：20次/2s
     * 限速规则（期权以外）：UserID + Instrument ID
     * 限速规则（只限期权）：UserID + Instrument Family
     * </p>
     * @param req 策略委托单
     * @return 策略下单响应体
     *
     */
    @POST("/api/v5/trade/cancel-advance-algos")
    Single<R<PolicyDelegationRsp>> cancelAdvanceAlgos(@Body List<TradeAlgoReq> req);

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-algo-trading-get-algo-order-details"> 获取策略委托单信息</a>
     * <p>
     * 限速：20次/2s
     * 限速规则：UserID
     * </p>
     * @param algoId 策略委托单ID algoId和algoClOrdId必须传一个，若传两个，以algoId为主
     * @param algoClOrdId 客户自定义策略订单ID algoId和algoClOrdId必须传一个，若传两个，以algoId为主
     * @return 策略下单响应体
     *
     */
    @GET("/api/v5/trade/order-algo")
    Single<R<PolicyOrderAlgoRsp>> orderAlgos(@Query("algoId") String algoId, @Query(value = "algoClOrdId") String algoClOrdId);

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-algo-trading-get-algo-order-list"> 获取未完成策略委托单列表</a>
     * <p>
     *获取当前账户下未触发的策略委托单列表
     *
     * 限速：20次/2s
     * 限速规则：UserID
     * </p>
     * @param algoId 策略委托单ID
     * @param algoClOrdId 客户自定义策略订单ID字母（区分大小写）与数字的组合，可以是纯字母、纯数字且长度要在1-32位之间
     * @param instType 产品类型 SPOT：币币 SWAP：永续合约 FUTURES：交割合约 FUTURES：交割合约
     * @param instId 产品ID，如 BTC-USDT
     * @param ordType 订单类型
     * @param before 请求此ID之前（更旧的数据）的分页内容，传的值为对应接口的algoId
     * @param after 请求此ID之前（更旧的数据）的分页内容，传的值为对应接口的algoId
     * @param limit 返回结果的数量，最大为100，默认100条
     * @return 策略下单响应体
     *
     */
    @GET("/api/v5/trade/orders-algo-pending")
    Single<R<PolicyOrderAlgoRsp>> ordersAlgoPending(@Query("algoId") String algoId, @Query("algoClOrdId") String algoClOrdId, @Query("instType") String instType, @Query("instId") String instId, @Query("ordType") String ordType, @Query("after") String after, @Query("before") String before,@Query("limit") Integer limit);

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-algo-trading-get-algo-order-history"> 获取历史策略委托单列表</a>
     * <p>
     *获取最近3个月当前账户下所有策略委托单列表
     *
     * 限速：20次/2s
     * 限速规则：UserID
     * </p>
     * @param ordType 订单类型
     * @param state 订单状态
     * @param algoId 策略委托单ID
     * @param instType 产品类型 SPOT：币币 SWAP：永续合约 FUTURES：交割合约 FUTURES：交割合约
     * @param instId 产品ID，如 BTC-USDT
     * @param before 请求此ID之前（更旧的数据）的分页内容，传的值为对应接口的algoId
     * @param after 请求此ID之前（更旧的数据）的分页内容，传的值为对应接口的algoId
     * @param limit 返回结果的数量，最大为100，默认100条
     * @return 策略下单响应体
     *
     */
    @GET("/api/v5/trade/orders-algo-history")
    Single<R<PolicyOrderAlgoRsp>> ordersAlgoHistory(@Query("ordType") String ordType, @Query("state") String state,@Query("algoId") String algoId, @Query("instType") String instType, @Query("instId") String instId,  @Query("after") String after, @Query("before") String before,@Query("limit") Integer limit);

}
