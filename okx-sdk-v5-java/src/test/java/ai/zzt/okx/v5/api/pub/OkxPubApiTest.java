package ai.zzt.okx.v5.api.pub;

import io.reactivex.rxjava3.core.Single;
import org.junit.jupiter.api.Test;
import ai.zzt.okx.v5.api.BaseApiTest;
import ai.zzt.okx.v5.api.factory.OkxRestApiFactory;
import ai.zzt.okx.v5.api.pri.OrderApi;
import ai.zzt.okx.v5.api.pub.query.ConvertCoinReq;
import ai.zzt.okx.v5.entity.rest.R;
import ai.zzt.okx.v5.entity.rest.RO;
import ai.zzt.okx.v5.entity.rest.RSingle;
import ai.zzt.okx.v5.entity.rest.pub.CoinSupportList;
import ai.zzt.okx.v5.entity.rest.pub.ConvertContractCoin;
import ai.zzt.okx.v5.entity.rest.trading.order.booking.req.PlaceOrderReq;
import ai.zzt.okx.v5.entity.rest.trading.order.booking.rsp.PlaceOrderRsp;
import ai.zzt.okx.v5.entity.ws.pub.FundingRate;
import ai.zzt.okx.v5.entity.ws.pub.Instruments;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.entity.ws.pub.SystemTime;
import ai.zzt.okx.v5.enumeration.OrderType;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.Side;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import ai.zzt.okx.v5.enumeration.ws.TdMode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Jou Ziton
 * @since 2024/5/18
 **/
public class OkxPubApiTest extends BaseApiTest {

    private final  OpenDataApi openDataApi = createOkxOrderAlgoApi(OpenDataApi.class);

    private final OrderApi orderApi = createOkxOrderAlgoApi(OrderApi.class);

    static {
        // TODO 临时加上解决服务器不能访问https的问题, 还没研究明白.
        System.setProperty("https.proxyPort", "7890");
        System.setProperty("https.proxyHost", "127.0.0.1");
    }

    @Test
    public void testGetServerTime() {
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            Single<R<SystemTime>> serverTime = openDataApi.getServerTime();
            R<SystemTime> execute = test(serverTime);
            long end = System.currentTimeMillis();
            System.out.println("okx time: " + execute);
            long ts = execute.getData().get(0).getTs();
            System.out.print("请求耗时: " + (ts - start) + "\t");
            System.out.print("响应耗时:" + (end - ts) + "\t");
            System.out.print("时间差: " + (end - start) + "\t");
            System.out.println();
        }
    }

    @Test
    public void testAllCcy() {
        RSingle<CoinSupportList> test = test(openDataApi.getAllCcy());
        System.out.println(test);
    }

    @Test
    public void testGetFundingRate() {
        Single<R<FundingRate>> fundingRate = openDataApi.getFundingRate("BTC-USDT-SWAP");
        R<FundingRate> execute = OkxRestApiFactory.get(fundingRate);
        System.out.println(execute);
    }

    @Test
    public void test_getInstruments() {
        Single<R<Instruments>> instruments = openDataApi.getInstruments(InstrumentType.SWAP, null);
        R<Instruments> execute = OkxRestApiFactory.get(instruments);
        List<Instruments> data = execute.getData();
        List<String> instIds = data.stream().map(Instruments::getInstId).toList();
        System.out.println(instIds);
    }

    @Test
    public void getMarkPriceTest() {
        Single<R<MarkPrice>> markPrice = openDataApi.getMarkPrice(InstrumentType.SWAP, null);
        R<MarkPrice> execute = OkxRestApiFactory.get(markPrice);
        System.out.println(execute);
    }

    @Test
    public void getMarkPriceCandles() {
        Single<RO<List<String>>> markPrice = openDataApi.getMarkPriceCandles("BTC-USDT-SWAP", null, null, "1m", null);
        RO<List<String>> execute = OkxRestApiFactory.get(markPrice);
        System.out.println(execute);
    }


    @Test
    public void getHistoryMarkPriceCandles() {
        Single<RO<List<String>>> markPrice = openDataApi.getHistoryMarkPriceCandles("CRV-USDT-SWAP", null, null, "1m", 100);
        RO<List<String>> execute = OkxRestApiFactory.get(markPrice);
        System.out.println(execute);
        String s = execute.getData().get(99).get(0);
        System.out.println(s);
        Single<RO<List<String>>> markPrice1 = openDataApi.getHistoryMarkPriceCandles("CRV-USDT-SWAP", Long.valueOf(s), null, "1m", 100);
        RO<List<String>> execute1 = OkxRestApiFactory.get(markPrice1);
        System.out.println(execute1);
    }

    @Test
    public void getHistoryMarkPriceLongCandles() {
        Single<RO<List<String>>> markPrice = openDataApi.getMarkPriceCandles("CRV-USDT-SWAP", null, null, "1m", 100);
        RO<List<String>> execute = OkxRestApiFactory.get(markPrice);
        System.out.println(execute);
        String s = execute.getData().get(99).get(0);
        System.out.println(s);
        Long l = Long.valueOf(s);
        Single<RO<List<String>>> markPrice1 = openDataApi.getMarkPriceCandles("CRV-USDT-SWAP", l, null, "1m", 100);
        RO<List<String>> execute1 = OkxRestApiFactory.get(markPrice1);
        System.out.println(execute1);
        Long a= Long.valueOf(execute.getData().get(0).get(0)) - 1000 * 60 * 1L*99;
        System.out.println(a);
        Single<RO<List<String>>> markPrice2 = openDataApi.getMarkPriceCandles("CRV-USDT-SWAP", a, null, "1m", 100);
        RO<List<String>> execute2 = OkxRestApiFactory.get(markPrice2);
        System.out.println(execute2);

        String s1 = execute2.getData().get(99).get(0);
        System.out.println(s1);
        Long l1 = Long.valueOf(s1);
        Single<RO<List<String>>> markPrice3 = openDataApi.getMarkPriceCandles("CRV-USDT-SWAP", l1, null, "1m", 100);
        RO<List<String>> execute3 = OkxRestApiFactory.get(markPrice3);
        System.out.println(execute3);
        Long a1= Long.valueOf(execute.getData().get(0).get(0)) - 1000 * 60 * 1L*199;
        System.out.println(a1);
        Single<RO<List<String>>> markPrice4 = openDataApi.getMarkPriceCandles("CRV-USDT-SWAP", a1, null, "1m", 100);
        RO<List<String>> execute4 = OkxRestApiFactory.get(markPrice4);
        System.out.println(execute4);
    }

    @Test
    public void getConvertContractCoin() {
        String instId = "ADA-USDT-SWAP";
        BigDecimal sz = BigDecimal.valueOf(100);
        ConvertCoinReq req = new ConvertCoinReq();
        req.setInstId(instId);
        req.setType(1);
        req.setSz(sz);
        req.setPx(new BigDecimal("0.3751"));
        req.setUnit("usds");
        req.setOpType("open");
        Map<String, Object> map = OkxRestApiFactory.paramToMap(req);
        Single<R<ConvertContractCoin>> convertContractCoin = openDataApi.getConvertContractCoin(map);
        R<ConvertContractCoin> execute = OkxRestApiFactory.get(convertContractCoin);
        System.out.println(execute.getData());

        PlaceOrderReq req1 = new PlaceOrderReq();
        req1.setInstId(instId);
        req1.setTdMode(TdMode.CROSS); //全仓
        req1.setSide(Side.BUY);
        req1.setOrdType(OrderType.MARKET);//市场价
        req1.setCcy("USDT"); //
        req1.setSz(execute.getData().get(0).getSz());
        req1.setPosSide(PositionsSide.LONG);
        Single<R<PlaceOrderRsp>> rSingle = orderApi.placeOrder(req1);

        R<PlaceOrderRsp> execute1 = OkxRestApiFactory.get(rSingle);
        System.out.println(execute1.getData());

    }


}
