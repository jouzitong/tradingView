package ai.zzt.okx.v5.api.pri;

import io.reactivex.rxjava3.core.Single;
import org.junit.jupiter.api.Test;
import ai.zzt.okx.v5.api.BaseApiTest;
import ai.zzt.okx.v5.api.factory.OkxRestApiFactory;
import ai.zzt.okx.v5.entity.rest.R;
import ai.zzt.okx.v5.entity.rest.trading.order.booking.req.PlaceOrderReq;
import ai.zzt.okx.v5.entity.rest.trading.order.booking.rsp.PlaceOrderRsp;
import ai.zzt.okx.v5.enumeration.OrderType;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.Side;
import ai.zzt.okx.v5.enumeration.ws.TdMode;

import java.math.BigDecimal;

/**
 * @author Jou Ziton
 * @since 2024/6/26
 **/
public class OrderApiTest extends BaseApiTest {

    private final OrderApi orderApi = createOkxOrderAlgoApi(OrderApi.class);

    @Test
    public void placeOrderTest() {
        String instId = "BTC-USDT-SWAP";
        PositionsSide side = PositionsSide.LONG;

        PlaceOrderReq req = new PlaceOrderReq();
        req.setInstId(instId);
        req.setTdMode(TdMode.CROSS); //全仓
        if (side == PositionsSide.SHORT) {
            req.setSide(Side.SELL);
        } else {
            req.setSide(Side.BUY);
        }
        req.setOrdType(OrderType.MARKET);//市场价
        req.setCcy("USDT"); //
//        req.setSz(new BigDecimal("0.333").divide(mp.getMarkPx(), RoundingMode.FLOOR));
        req.setSz(BigDecimal.valueOf(10));
        if (req.getSz().compareTo(BigDecimal.ZERO) == 0) {
            req.setSz(new BigDecimal("1"));
        }
        req.setTgtCcy("quote_ccy");
        req.setPosSide(side);
        Single<R<PlaceOrderRsp>> rSingle = orderApi.placeOrder(req);
        R<PlaceOrderRsp> placeOrderRspR = OkxRestApiFactory.get(rSingle);
        System.out.println(placeOrderRspR);

    }


}
