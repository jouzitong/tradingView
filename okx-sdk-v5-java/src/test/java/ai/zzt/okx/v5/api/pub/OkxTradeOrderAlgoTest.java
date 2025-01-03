package ai.zzt.okx.v5.api.pub;

import io.reactivex.rxjava3.core.Single;
import org.junit.jupiter.api.Test;
import ai.zzt.okx.v5.api.BaseApiTest;
import ai.zzt.okx.v5.api.factory.OkxRestApiFactory;
import ai.zzt.okx.v5.api.pri.OrderAlgoApi;
import ai.zzt.okx.v5.entity.rest.R;
import ai.zzt.okx.v5.entity.rest.policy.trade.req.PolicyAmendAlgoReq;
import ai.zzt.okx.v5.entity.rest.policy.trade.req.StopProfitAndLossReq;
import ai.zzt.okx.v5.entity.rest.policy.trade.req.TradeAlgoReq;
import ai.zzt.okx.v5.entity.rest.policy.trade.rsp.PolicyDelegationRsp;
import ai.zzt.okx.v5.entity.rest.policy.trade.rsp.rsp.PolicyAmendRsp;
import ai.zzt.okx.v5.entity.rest.policy.trade.rsp.rsp.PolicyOrderAlgoRsp;
import ai.zzt.okx.v5.enumeration.OrdType;
import ai.zzt.okx.v5.enumeration.OrderState;
import ai.zzt.okx.v5.enumeration.Side;
import ai.zzt.okx.v5.enumeration.TriggerPxType;
import ai.zzt.okx.v5.enumeration.ws.TdMode;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jou Ziton
 * @since 2024/5/18
 **/
public class OkxTradeOrderAlgoTest extends BaseApiTest {

    private final OrderAlgoApi orderAlgoApi = createOkxOrderAlgoApi(OrderAlgoApi.class);

    @Test
    public void orderAlgoTest() {
        StopProfitAndLossReq stopProfitAndLossReq = StopProfitAndLossReq.builder()
                .instId("CRV-USDT")
                .tdMode(TdMode.CROSS)
                .side(Side.BUY)
                .ordType(OrdType.CONDITIONAL)
                .ccy("USDT")
                //.tgtCcy(TgtCcyEnum.BASE_CCY)
                // .algoClOrdId("xxxxxxx")
                .sz(new BigDecimal("10"))
                .tpTriggerPx(new BigDecimal("0.4"))
                // .tag("dddddddd")
                .tpTriggerPxType(TriggerPxType.LAST)
                //.slTriggerPx(new BigDecimal("0.8"))
                //.slTriggerPxType(TriggerPxType.LAST)
                .tpOrdPx(new BigDecimal("0.42"))
                //.slOrdPx(new BigDecimal("0.7"))
                .build();
        Single<R<PolicyDelegationRsp>> single = orderAlgoApi.placeOrder(stopProfitAndLossReq);
        R<PolicyDelegationRsp> execute = OkxRestApiFactory.get(single);
        System.out.println(execute);
    }

    @Test
    public void testGetOrderAlgos() {
        Single<R<PolicyOrderAlgoRsp>> single = orderAlgoApi.orderAlgos("715355204761268224", null);
        R<PolicyOrderAlgoRsp> execute = OkxRestApiFactory.get(single);
        System.out.println(execute);
    }

    @Test
    public void testAmendAlgos() {
        //止损订单不支持修改
        PolicyAmendAlgoReq amendAlgoReq = PolicyAmendAlgoReq.builder().instId("CRV-USDT").algoId("715364912075223040").newSz(new BigDecimal("20")).build();
        Single<R<PolicyAmendRsp>> single = orderAlgoApi.amendAlgos(amendAlgoReq);
        R<PolicyAmendRsp> execute = OkxRestApiFactory.get(single);
        System.out.println(execute);
    }

    @Test
    public void testCancelAdvanceAlgos() {
        List<TradeAlgoReq> list = Arrays.asList(TradeAlgoReq.builder().algoId("715367027300478976").instId("CRV-USDT").build());
        Single<R<PolicyDelegationRsp>> single = orderAlgoApi.cancelAdvanceAlgos(list);
        R<PolicyDelegationRsp> execute = OkxRestApiFactory.get(single);
        System.out.println(execute);
    }

    @Test
    public void testCancelAlgo() {
        List<TradeAlgoReq> list = Arrays.asList(TradeAlgoReq.builder().algoId("715364912075223040").instId("CRV-USDT").build());
        Single<R<PolicyDelegationRsp>> single = orderAlgoApi.cancelAlgos(list);
        R<PolicyDelegationRsp> execute = OkxRestApiFactory.get(single);
        System.out.println(execute);
    }

    @Test
    public void testOrdersAlgoPending() {
        Single<R<PolicyOrderAlgoRsp>> rSingle = orderAlgoApi.ordersAlgoPending(null, null, null, null, OrdType.CONDITIONAL.value(), null, null, null);
        R<PolicyOrderAlgoRsp> execute = OkxRestApiFactory.get(rSingle);
        System.out.println(execute);
    }

    @Test
    public void testOrdersAlgoHistory() {
        Single<R<PolicyOrderAlgoRsp>> rSingle = orderAlgoApi.ordersAlgoHistory(OrdType.CONDITIONAL.value(), OrderState.CANCELED.value(), null, null, null, null, null, null);
        R<PolicyOrderAlgoRsp> execute = OkxRestApiFactory.get(rSingle);
        System.out.println(execute);
    }

}
