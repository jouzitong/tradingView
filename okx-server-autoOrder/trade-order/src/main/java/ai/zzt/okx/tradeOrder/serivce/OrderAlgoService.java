package ai.zzt.okx.tradeOrder.serivce;

import ai.zzt.okx.common.properties.AppProperties;
import ai.zzt.okx.common.utils.ThreadUtils;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.v5.api.factory.OkxRestApiFactory;
import ai.zzt.okx.v5.api.pri.OrderAlgoApi;
import ai.zzt.okx.v5.entity.rest.R;
import ai.zzt.okx.v5.entity.rest.policy.trade.req.StopProfitAndLossReq;
import ai.zzt.okx.v5.entity.rest.policy.trade.rsp.PolicyDelegationRsp;
import ai.zzt.okx.v5.enumeration.OrdType;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.Side;
import ai.zzt.okx.v5.enumeration.TriggerPxType;
import ai.zzt.okx.v5.enumeration.trade.TgtCcyEnum;
import ai.zzt.okx.v5.enumeration.ws.TdMode;
import io.reactivex.rxjava3.core.Single;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2024-08-27
 **/
@Service
@Slf4j
@AllArgsConstructor
public class OrderAlgoService {

    private final OrderAlgoApi orderAlgoApi;

    protected final AppProperties appProperties;

    public boolean closeOrder(TradeOrderRequest request) {
        // TODO
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
        return true;
    }


    @PostConstruct
    public void test() {
        ThreadUtils.runIO(() -> {
            if (!appProperties.isSimulated()) {
                throw new RuntimeException("正式环境不允许启动该代码");
            }
            ThreadUtils.sleep(10);
            StopProfitAndLossReq stopProfitAndLossReq = StopProfitAndLossReq.builder()
                    .instId("CRV-USDT-SWAP")
                    .tdMode(TdMode.CROSS)
                    .posSide(PositionsSide.NET)
                    .side(Side.BUY)
                    .ordType(OrdType.CONDITIONAL)
                    .ccy("USDT")
                    .tgtCcy(TgtCcyEnum.BASE_CCY)
                    .algoClOrdId("xxxxxxx")
                    .sz(new BigDecimal("10"))
                    .tpTriggerPx(new BigDecimal("0.4"))
                    .tag("dddddddd")
                    .tpTriggerPxType(TriggerPxType.LAST)
                    .tpOrdPx(new BigDecimal("0.42"))
//                    .slTriggerPx(new BigDecimal("0.8"))
//                    .slTriggerPxType(TriggerPxType.LAST)
                    //.slOrdPx(new BigDecimal("0.7"))
                    .build();
            Single<R<PolicyDelegationRsp>> single = orderAlgoApi.placeOrder(stopProfitAndLossReq);
            R<PolicyDelegationRsp> execute = OkxRestApiFactory.get(single);
            System.out.println(execute);
        });
    }

}
