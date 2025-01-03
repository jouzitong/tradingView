package ai.zzt.okx.v5.api.pri;

import io.reactivex.rxjava3.core.Single;
import org.jetbrains.annotations.Nullable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ai.zzt.okx.v5.api.OkxPriApi;
import ai.zzt.okx.v5.entity.rest.R;
import ai.zzt.okx.v5.entity.rest.account.Balance;
import ai.zzt.okx.v5.entity.rest.account.req.LeverageReq;
import ai.zzt.okx.v5.entity.rest.account.resp.LeverageResp;
import ai.zzt.okx.v5.entity.ws.biz.AlgoOrder;
import ai.zzt.okx.v5.entity.ws.pri.Order;
import ai.zzt.okx.v5.entity.ws.pri.Positions;

/**
 * <a href="https://www.okx.com/docs-v5/zh/?shell#trading-account-rest-api">交易账户 API</a>
 *
 * @author Jouziton
 * @since 2024/5/16
 **/
public interface AccountApi extends OkxPriApi {

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#trading-account-rest-api-get-balance">查看账户余额</a>
     * <p>
     * 限速：10次/2s
     * <p>
     * 限速规则：UserID
     *
     * @param ccy 币种，如 BTC
     *            支持多币种查询（不超过20个），币种之间半角逗号分隔
     * @return account balance list
     */
    @GET("/api/v5/account/balance")
    Single<R<Balance>> getBalance(@Nullable @Query("ccy") String ccy);

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#trading-account-rest-api-get-positions">查看持仓信息</a>
     * <p>
     * 获取该账户下拥有实际持仓的信息。账户为买卖模式会显示净持仓（net），账户为开平仓模式下会分别返回开多（long）或开空（short）的仓位。按照仓位创建时间倒序排列。
     *
     * @return 持仓信息
     */
    @GET("/api/v5/account/positions")
    Single<R<Positions>> getPositions();

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#trading-account-rest-api-get-orders-pending">获取未成交订单列表</a>
     * <p>
     * 获取当前账户下所有未成交订单信息
     *
     * @return 订单列表
     */
    @GET("/api/v5/trade/orders-pending")
    Single<R<Order>> getPendingOrders();

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-algo-trading-get-algo-order-list">获取未成交的委托单列表</a>
     * <p>
     * 获取当前账户下所有未成交的委托单信息
     *
     * @return 委托单列表
     */
    @GET("/api/v5/trade/orders-algo-pending")
    Single<R<AlgoOrder>> getPendingAlgoOrders(@Query("ordType") String ordType, @Query("instType") String instType);


    /**
     * <a href="https://www.okx.com/docs-v5/zh/#trading-account-rest-api-get-leverage">获取杠杆倍数</a>
     *
     * @param instId  产品类型
     * @param ccy     币种
     * @param mgnMode 保证金模式
     * @return
     */
    @GET("/api/v5/account/leverage-info")
    Single<R<LeverageResp>> getLeverageInfo(@Query("instId") String instId, @Query("ccy") String ccy, @Query("mgnMode") String mgnMode);

    /**
     * <a href="https://www.okx.com/docs-v5/zh/#trading-account-rest-api-set-leverage">设置杠杆倍数</a>
     *
     * @param req 请求参数
     * @return SetLeverageResp
     */
    @POST("/api/v5/account/leverage-info")
    Single<R<LeverageResp>> setLeverage(@Body LeverageReq req);


}
