package ai.zzt.okx.v5.api.trade;

import io.reactivex.rxjava3.core.Single;
import org.junit.jupiter.api.Test;
import ai.zzt.okx.v5.api.BaseApiTest;
import ai.zzt.okx.v5.api.factory.OkxRestApiFactory;
import ai.zzt.okx.v5.api.pri.TradingStrategyApi;
import ai.zzt.okx.v5.entity.rest.R;
import ai.zzt.okx.v5.entity.rest.policy.trade.req.StopProfitAndLossReq;
import ai.zzt.okx.v5.entity.rest.policy.trade.rsp.PolicyDelegationRsp;
import ai.zzt.okx.v5.enumeration.OrdType;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.Side;
import ai.zzt.okx.v5.enumeration.ws.TdMode;

import java.math.BigDecimal;

public class TradingStrategyApiTest extends BaseApiTest {

    private final TradingStrategyApi tradingStrategyApi = createOkxOrderAlgoApi(TradingStrategyApi.class);

    /**
     * 止盈止损策略下单
     */
    @Test
    public void testStopProfitAndLoss() {
        StopProfitAndLossReq stopProfitAndLossReq = StopProfitAndLossReq.builder()
                .instId("BTC-USDT-SWAP")
                .tdMode(TdMode.CROSS)
                .side(Side.BUY)
                .posSide(PositionsSide.LONG)
                .ordType(OrdType.CONDITIONAL)
                .ordType(OrdType.LIMIT)
                .sz(new BigDecimal("10"))
                .tpTriggerPx(new BigDecimal("15"))
                .tpOrdPx(new BigDecimal("18"))
                .build();
        Single<R<PolicyDelegationRsp>> single = tradingStrategyApi.stopProfitAndLoss(stopProfitAndLossReq);
        R<PolicyDelegationRsp> execute = OkxRestApiFactory.get(single);
        System.out.println(execute);
    }

//    /**
//     * 计划委托策略下单
//     */
//    @Test
//    public void testPlanDelegation() {
//        List<AttachAlgoOrd> attachAlgoOrds = new ArrayList<>();
//        AttachAlgoOrd attachAlgoOrd = AttachAlgoOrd.builder()
//                .attachAlgoClOrdId("xxxx")
//                .slTriggerPx("100")
//                .slOrdPx("600")
//                .tpTriggerPx("25921")
//                .tpOrdPx("2001")
//                .build();
//        attachAlgoOrds.add(attachAlgoOrd);
//        PlanDelegationReq planDelegationReq = PlanDelegationReq.builder()
//                .instId("BTC-USDT-SWAP")
//                .side(Side.BUY)
//                .tdMode(TdMode.CROSS)
//                .posSide(PositionsSide.NET)
//                .sz(new BigDecimal("10"))
//                .ordType(OrdType.TRIGGER)
//                .triggerPx(new BigDecimal("25920"))
//                .triggerPxType(TriggerPxType.LAST)
//                .orderPx(new BigDecimal("-1"))
//                .attachAlgoOrds(attachAlgoOrds)
//                .build();
//        Single<R<PolicyDelegationRsp>> single = tradingStrategyApi.planDelegation(planDelegationReq);
//        R<PolicyDelegationRsp> execute = OkxRestApiFactory.execute(single);
//        System.out.println(execute);
//    }
//
//    /**
//     * 移动止盈止损策略下单
//     */
//    @Test
//    public void testMoveStopProfitAndLoss() {
//        MoveStopProfitAndLossReq moveStopProfitAndLossReq = MoveStopProfitAndLossReq.builder()
//                .instId("BTC-USDT-SWAP")
//                .side(Side.BUY)
//                .tdMode(TdMode.CROSS)
//                .ordType(OrdType.MOVE_ORDER_STOP)
//                .sz(new BigDecimal("10"))
//                .posSide(PositionsSide.NET)
//                .callbackRatio(new BigDecimal("0.05"))
//                .reduceOnly(true)
//                .build();
//        Single<R<PolicyDelegationRsp>> single = tradingStrategyApi.moveStopProfitAndLoss(moveStopProfitAndLossReq);
//        R<PolicyDelegationRsp> execute = OkxRestApiFactory.execute(single);
//        System.out.println(execute);
//    }
//
//    /**
//     *
//     */
//    @Test
//    public void testTimeWeightReq() {
//        TimeWeightReq timeWeightReq = TimeWeightReq.builder()
//                .instId("BTC-USDT-SWAP")
//                .side(Side.BUY)
//                .tdMode(TdMode.CROSS)
//                .ordType(OrdType.TWAP)
//                .sz(new BigDecimal("10"))
//                .posSide(PositionsSide.NET)
//                .szLimit("10")
//                .pxLimit("100")
//                .timeInterval("10")
//                .pxSpread("10")
//                .build();
//        Single<R<PolicyDelegationRsp>> single = tradingStrategyApi.timeWeight(timeWeightReq);
//        R<PolicyDelegationRsp> execute = OkxRestApiFactory.execute(single);
//        System.out.println(execute);
//    }
//
//    public TradingStrategyApi createTradingStrategyApi() {
//        String apiKey = "44cad3f1-1deb-4a2c-9ff0-a36857b2bf08";
//        String secretKey = "8537260ABE502FCD1C7F373D308AEB12";
//        String passphrase = "ZZt012818..";
//        boolean simulation = true;
//        return OkxRestApiFactory.buildClient(Duration.ofSeconds(10), TradingStrategyApi.class);
//    }
//
//    @Test
//    public void testCancelAlgo() {
//        CancelAlgoReq cancelAlgoReq1 = CancelAlgoReq.builder()
//                .algoId("590919993110396111")
//                .instId("BTC-USDT")
//                .build();
//        CancelAlgoReq cancelAlgoReq2 = CancelAlgoReq.builder()
//                .algoId("590920138287841222")
//                .instId("BTC-USDT")
//                .build();
//        List<CancelAlgoReq> request = Arrays.asList(cancelAlgoReq1, cancelAlgoReq2);
//        Single<R<CancelAlgoRsp>> single = tradingStrategyApi.cancelAlgo(request);
//        R<CancelAlgoRsp> execute = OkxRestApiFactory.execute(single);
//        System.out.println(execute);
//    }
//
//    @Test
//    public void testCancelAdvanceAlgo() {
//        CancelAlgoReq cancelAlgoReq1 = CancelAlgoReq.builder()
//                .algoId("590920768125665111")
//                .instId("BTC-USDT")
//                .build();
//        CancelAlgoReq cancelAlgoReq2 = CancelAlgoReq.builder()
//                .algoId("590920799650058222")
//                .instId("BTC-USDT")
//                .build();
//        List<CancelAlgoReq> request = Arrays.asList(cancelAlgoReq1, cancelAlgoReq2);
//        Single<R<CancelAlgoRsp>> single = tradingStrategyApi.cancelAdvanceAlgo(request);
//        R<CancelAlgoRsp> execute = OkxRestApiFactory.execute(single);
//        System.out.println(execute);
//    }
//
//    @Test
//    public void testAmendAlgo() {
//        AmendAlgoReq amendAlgoReq = AmendAlgoReq.builder()
//                .algoId("2510789768709120")
//                .newSz(new BigDecimal(2))
//                .instId("BTC-USDT-SWAP")
//                .build();
//        Single<R<AmendAlgoRsp>> single = tradingStrategyApi.amendAlgo(amendAlgoReq);
//        R<AmendAlgoRsp> execute = OkxRestApiFactory.execute(single);
//        System.out.println(execute);
//    }
//
//    @Test
//    public void testGetAlgoInfo() {
//        String algoId = "681187161907138560";
//        Single<R<AlgoInfoRsp>> single = tradingStrategyApi.getAlgoInfo(algoId, null);
//        R<AlgoInfoRsp> execute = OkxRestApiFactory.execute(single);
//        System.out.println(execute);
//    }
//
//    @Test
//    public void testGetAlgoInfoOnPending() {
//        AlgoInfoOnPendingReq req = AlgoInfoOnPendingReq.builder()
//                .ordType(OrdType.CONDITIONAL)
//                .build();
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, String> map = objectMapper.convertValue(req, Map.class);
//        Single<R<AlgoInfoRsp>> single = tradingStrategyApi.getAlgoInfoOnPending(map);
//        R<AlgoInfoRsp> execute = OkxRestApiFactory.execute(single);
//        System.out.println(execute);
//    }
}
