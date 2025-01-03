package ai.zzt.okx.v5.api.pri;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import ai.zzt.okx.v5.api.OkxPriApi;
import ai.zzt.okx.v5.entity.rest.R;
import ai.zzt.okx.v5.entity.rest.policy.trade.req.AlgoInfoOnPendingReq;
import ai.zzt.okx.v5.entity.rest.policy.trade.req.AmendAlgoReq;
import ai.zzt.okx.v5.entity.rest.policy.trade.req.CancelAlgoReq;
import ai.zzt.okx.v5.entity.rest.policy.trade.req.MoveStopProfitAndLossReq;
import ai.zzt.okx.v5.entity.rest.policy.trade.req.PlanDelegationReq;
import ai.zzt.okx.v5.entity.rest.policy.trade.req.PolicyDelegationOrderReq;
import ai.zzt.okx.v5.entity.rest.policy.trade.req.StopProfitAndLossReq;
import ai.zzt.okx.v5.entity.rest.policy.trade.req.TimeWeightReq;
import ai.zzt.okx.v5.entity.rest.policy.trade.rsp.AlgoInfoRsp;
import ai.zzt.okx.v5.entity.rest.policy.trade.rsp.AmendAlgoRsp;
import ai.zzt.okx.v5.entity.rest.policy.trade.rsp.CancelAlgoRsp;
import ai.zzt.okx.v5.entity.rest.policy.trade.rsp.PolicyDelegationRsp;

import java.util.List;
import java.util.Map;

/**
 * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-algo-trading-post-place-algo-order">策略交易 api</a>
 * <pre>
 * 提供
 * 单向止盈止损委托、双向止盈止损委托
 * 计划委托、
 * 时间加权委托、
 * 移动止盈止损委托
 * </pre>
 * TODO <a href="https://www.okx.com/zh-hans/help/xi-strategy-order-types">委托介绍</a>
 */
public interface TradingStrategyApi extends OkxPriApi {

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-algo-trading-post-place-algo-order">策略下单</a>
     *
     * @param req 策略下单请求体
     * @return 策略下单响应体
     * @see StopProfitAndLossReq 止盈止损委托
     * @see PlanDelegationReq 计划委托
     * @see TimeWeightReq 时间加权委托
     * @see MoveStopProfitAndLossReq 移动止盈止损委托
     */
    @POST("/api/v5/trade/order-algo")
    Single<R<PolicyDelegationRsp>> placeOrder(@Body PolicyDelegationOrderReq req);

    /**
     * 止盈止损
     * <p>
     * 用户可预先设置触发价和委托价，等市场价到达触发价时，系统会按委托价自动下单。
     * 单向止盈止损可设置单边的止盈或止损；双向止盈止损可设置双边，一边触发后另一边失效。
     * 该委托不会预先占用仓位或保证金。
     * <p>
     * todo 当用户进行单向止盈止损委托（ordType=conditional）时，如果用户同时传了止盈止损四个参数，只进行止损的功能校验，忽略止盈的业务逻辑校验。
     *
     * @param stopProfitAndLossReq 止盈止损构建类
     * @return 策略委托 下单情况
     */
    @POST("/api/v5/trade/order-algo")
    Single<R<PolicyDelegationRsp>> stopProfitAndLoss(@Body StopProfitAndLossReq stopProfitAndLossReq);

    /**
     * 计划委托
     * <p>
     * 当市场价格到达触发价格时，系统将按预先设置的委托价格和数量自动下单。
     * 该委托不会预先占用仓位或保证金。
     * 仅适用于币币、交割和永续。
     *
     * @param planDelegationReq 计划委托 构建类
     * @return 策略委托 下单情况
     */
    @POST("/api/v5/trade/order-algo")
    Single<R<PolicyDelegationRsp>> planDelegation(@Body PlanDelegationReq planDelegationReq);

    /**
     * 移动止盈止损是一种跟踪市场价格的止盈止损，它的触发价格会跟随市场波动而变化，触发成功后会下市价单。
     * <pre>
     * 实际触发价格的计算：卖出或开空时，
     * 实际触发价格 = 下单成功后最高价-回调幅度 (价距)，或下单成功后最高价 *(1-回调幅度 %) (比例)；买入或开多，
     * 实际触发价格 = 下单成功后最低价 + 回调幅度，或下单成功后最低价 *(1+ 回调幅度 %)。
     * 同时，您可以利用激活价格来设置委托被激活的价格。
     * </pre>
     *
     * @param moveStopProfitAndLossReq 移动 止盈止损构建类
     * @return 策略委托 下单情况
     */
    @POST("/api/v5/trade/order-algo")
    Single<R<PolicyDelegationRsp>> moveStopProfitAndLoss(@Body MoveStopProfitAndLossReq moveStopProfitAndLossReq);

    /**
     * 时间加权是一种大额订单拆分后分时吃单的策略。
     * <p>
     * 用户在进行大额交易时，为避免对市场造成过大冲击，需要将大单委托自动拆为多笔委托。
     *
     * @param timeWeightReq 时间加权构建类
     * @return 策略委托 下单情况
     */
    @POST("/api/v5/trade/order-algo")
    Single<R<PolicyDelegationRsp>> timeWeight(@Body TimeWeightReq timeWeightReq);

    /**
     * 撤销策略委托订单（不包含冰山委托、时间加权、移动止盈止损等高级策略订单），每次最多可以撤销10个策略委托单
     *
     * @param cancelAlgoReq 需要撤销的策略委托单
     * @return 撤销结果
     */
    @POST("/api/v5/trade/cancel-algo")
    Single<R<CancelAlgoRsp>> cancelAlgo(@Body List<CancelAlgoReq> cancelAlgoReq);

    /**
     * 撤销冰山委托、时间加权、移动止盈止损委托订单，每次最多可以撤销10个策略委托单
     *
     * @param cancelAlgoReq 需要撤销的策略委托单
     * @return 撤销结果
     */
    @POST("/api/v5/trade/cancel-advance-algo")
    Single<R<CancelAlgoRsp>> cancelAdvanceAlgo(@Body List<CancelAlgoReq> cancelAlgoReq);

    /**
     * 修改策略委托订单（仅支持止盈止损单，不包含计划委托、冰山委托、时间加权、移动止盈止损等订单）
     * 只适用于交割和永续合约。
     *
     * @param amendAlgoReq 需要修改的策略委托单
     * @return 修改结果
     */
    @POST("/api/v5/trade/amend-algo")
    Single<R<AmendAlgoRsp>> amendAlgo(@Body AmendAlgoReq amendAlgoReq);

    /**
     * 获取策略委托单信息
     * <p>
     * algoId和algoClOrdId必须传一个，若传两个，以algoId为主
     *
     * @param algoId      策略委托单ID
     * @param algoClOrdId 客户自定义策略订单ID
     * @return 策略委托单信息
     */
    @GET("/api/v5/trade/order-algo")
    Single<R<AlgoInfoRsp>> getAlgoInfo(@Query("algoId") String algoId, @Query("algoClOrdId") String algoClOrdId);

    /**
     * 获取当前账户下未触发的策略委托单列表
     *
     * @param map get url请求参数 {@link AlgoInfoOnPendingReq}转换的map
     * @return 略委托单信息
     */
    @GET("/api/v5/trade/orders-algo-pending")
    Single<R<AlgoInfoRsp>> getAlgoInfoOnPending(@QueryMap Map<String, String> map);


}
