package ai.zzt.okx.v5.api.pub;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import ai.zzt.okx.v5.api.OkxApi;
import ai.zzt.okx.v5.api.pub.query.ConvertCoinReq;
import ai.zzt.okx.v5.entity.rest.R;
import ai.zzt.okx.v5.entity.rest.RO;
import ai.zzt.okx.v5.entity.rest.RSingle;
import ai.zzt.okx.v5.entity.rest.pub.CoinSupportList;
import ai.zzt.okx.v5.entity.rest.pub.ConvertContractCoin;
import ai.zzt.okx.v5.entity.ws.pub.FundingRate;
import ai.zzt.okx.v5.entity.ws.pub.Instruments;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.entity.ws.pub.SystemTime;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;

import java.util.List;
import java.util.Map;


/**
 * okx public api
 * <p>
 * TODO Need to reorganize and subdivide the API
 *
 * @author Jou Ziton
 * @since 2024/5/18
 **/
public interface OpenDataApi extends OkxApi {

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#public-data-rest-api-get-system-time">系统时间</a>
     *
     * @return 系统时间
     */
    @GET("/api/v5/public/time")
    Single<R<SystemTime>> getServerTime();

    /**
     * 获取系统支持的所有币种
     *
     * @return 系统支持的所有币种
     */
    @GET("/api/v5/rubik/stat/trading-data/support-coin")
    Single<RSingle<CoinSupportList>> getAllCcy();

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#funding-rate">获取资金费率</a>
     *
     * @param instId 产品ID
     * @return
     */
    @GET("/api/v5/public/funding-rate")
    Single<R<FundingRate>> getFundingRate(@Query("instId") String instId);

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#public-data-rest-api-get-instruments">获取交易产品基本信息</a>
     *
     * @param instType 产品类型
     * @param instId   产品ID
     * @return
     */
    @GET("/api/v5/public/instruments")
    Single<R<Instruments>> getInstruments(@Query("instType") InstrumentType instType, @Query("instId") String instId);


    /**
     * <a href="https://www.okx.com/docs-v5/zh/#public-data-rest-api-get-mark-price">获取标记价格</a>
     *
     * @param instType 产品类型
     * @param instId   产品ID, 允许为空
     * @return
     */
    @GET("/api/v5/public/mark-price")
    Single<R<MarkPrice>> getMarkPrice(@Query("instType") InstrumentType instType, @Query("instId") String instId);

    /**
     * <a href="https://www.okx.com/docs-v5/zh/#public-data-rest-api-get-mark-price-candlesticks">获取标记价格K线数据</a>
     * <p>
     * 限速：20次/2s
     *
     * @param instId 产品ID, 允许为空
     * @param after  请求此时间戳之前（更旧的数据）的分页内容，传的值为对应接口的ts
     * @param before 请求此时间戳之后（更新的数据）的分页内容，传的值为对应接口的ts, 单独使用时，会返回最新的数据。
     * @param bar    时间粒度，默认值1m如 [1m/3m/5m/15m/30m/1H/2H/4H]香港时间开盘价k线：[6H/12H/1D/1W/1M] UTC时间开盘价k线：[6Hutc/12Hutc/1Dutc/1Wutc/1Mutc]
     * @param limit  分页返回的结果集数量，最大为100，不填默认返回100条
     * @return 注意： 和下面“获取获取标记价格历史K线数据”的数据一样 {@link this#getHistoryMarkPriceCandles}
     */
    @GET("/api/v5/market/mark-price-candles")
    Single<RO<List<String>>> getMarkPriceCandles(@Query("instId") String instId,
                                                 @Query("after") Long after, @Query("before") Long before,
                                                 @Query("bar") String bar, @Query("limit") Integer limit);


    /**
     * <a href="https://www.okx.com/docs-v5/zh/#public-data-rest-api-get-mark-price-candlesticks-history">获取标记价格历史K线数据</a>
     * <p>
     * 限速：10次/2s
     *
     * @param instId 产品ID, 允许为空
     * @param after  请求此时间戳之前（更旧的数据）的分页内容，传的值为对应接口的ts
     * @param before 请求此时间戳之后（更新的数据）的分页内容，传的值为对应接口的ts, 单独使用时，会返回最新的数据。
     * @param bar    时间粒度，默认值1m如 [1m/3m/5m/15m/30m/1H/2H/4H]香港时间开盘价k线：[6H/12H/1D/1W/1M] UTC时间开盘价k线：[6Hutc/12Hutc/1Dutc/1Wutc/1Mutc]
     * @param limit  分页返回的结果集数量，最大为100，不填默认返回100条
     * @return
     */
    @GET("/api/v5/market/history-mark-price-candles")
    Single<RO<List<String>>> getHistoryMarkPriceCandles(@Query("instId") String instId, @Query("after") Long after, @Query("before") Long before, @Query("bar") String bar, @Query("limit") Integer limit);

    /**
     * <a href="https://www.okx.com/docs-v5/zh/?shell#public-data-rest-api-unit-convert">张币转换</a>
     *
     * @param map
     * @return 张币转换
     * @see ConvertCoinReq 封装的请求参数
     */
    @GET("/api/v5/public/convert-contract-coin")
    Single<R<ConvertContractCoin>> getConvertContractCoin(@QueryMap Map<String, Object> map);

}
