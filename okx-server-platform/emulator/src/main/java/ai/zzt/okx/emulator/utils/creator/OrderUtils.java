package ai.zzt.okx.emulator.utils.creator;

import ai.zzt.okx.common.exception.TodoRuntimeException;
import ai.zzt.okx.okx_client.utils.SwapCoinUtils;
import ai.zzt.okx.settings.order.PlaceOrderSettings;
import ai.zzt.okx.dispatcher.request.OrderRequest;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.v5.entity.ws.pri.Order;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.OrdType;
import ai.zzt.okx.v5.enumeration.OrderState;
import ai.zzt.okx.v5.enumeration.OrderType;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.Side;
import ai.zzt.okx.v5.enumeration.TriggerPxType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 订单生成器
 *
 * @author zhouzhitong
 * @since 2024/10/23
 **/
public class OrderUtils {

    /**
     * 构建开仓订单
     *
     * @param request 下单请求
     * @return 订单
     */
    public static Order buildOrderOfOpen(TradeOrderRequest request) {
        OrderRequest orderRequest = request.getRequest();
        PlaceOrderSettings settings = request.getSettingsContext().getPlaceOrderSettings();
        BigDecimal lever = settings.getLever();
        MarkPrice mp = request.getMarkPrice();

        String orderId = UUID.randomUUID().toString();
        BigDecimal openPrice = settings.getOpenOrderType() == OrderType.LIMIT ? orderRequest.getOperatePrice() : mp.getMarkPx();

        // TODO 订单号使用sessionId只是为了和仿真任务做关联
        return Order.builder()
                // TODO 订单号使用sessionId只是为了和仿真任务做关联
                .ordId(orderId)
                .clOrdId(request.getSessionId())
                .instId(request.getInstId())
                .instType(request.getMarkPrice().getInstType())
                .tdMode(settings.getTdMode())
                .side(open(orderRequest.getPositionsSide()))
                .posSide(orderRequest.getPositionsSide())
                .lever(settings.getLever())
                .ordType(settings.getOpenOrderType())
                .px(mp.getMarkPx())
                .tpOrdPx(openPrice)
                .state(OrderState.LIVE)
                .sz(sz(request.getInstId(), settings.getCash(), mp.getMarkPx(), lever))
                .cTime(mp.getTs())
                .build();
    }

    public static Order buildOrderOfClose(TradeOrderRequest request) {
        PlaceOrderSettings settings = request.getSettingsContext().getPlaceOrderSettings();

        OrderRequest orderRequest = request.getRequest();
        OrdType ordType = orderRequest.getOrdType();
        OrderType orderType = settings.getCloseOrderType();
        List<Order.AttachAloOrder> attachAloOrders = null;
        if (ordType == OrdType.CONDITIONAL) {
            Order.AttachAloOrder aao = new Order.AttachAloOrder();
            aao.setTpOrdPx(orderRequest.getOperatePrice());
            aao.setTpTriggerPxType(TriggerPxType.MARK);
            // TODO 目前还不支持止损
            attachAloOrders = new ArrayList<>();
            attachAloOrders.add(aao);
            orderType = OrderType.LIMIT;
        }

        String orderId = UUID.randomUUID().toString();
        //                .sz(sz(orderRequest.getCash(), request.getMarkPrice().getMarkPx()))
        // TODO 目前是全仓平, 暂时不设置大小
        // 放置条件单
        return Order.builder().instId(request.getInstId())
                .instType(request.getMarkPrice().getInstType())
                .tdMode(settings.getTdMode())
                .side(close(orderRequest.getPositionsSide()))
                .posSide(orderRequest.getPositionsSide())
                .ordId(orderId).clOrdId(orderId)
                .ordType(orderType)
                .px(request.getMarkPrice().getMarkPx())
                .tpOrdPx(request.getRequest()
                        .getOperatePrice())
//                .sz(sz(orderRequest.getCash(), request.getMarkPrice().getMarkPx()))
                .sz(null) // TODO 目前是全仓平, 暂时不设置大小
                .cTime(request.getMarkPrice().getTs()).attachAlgoOrds(attachAloOrders) // 放置条件单
                .build();
    }

    protected static BigDecimal sz(String instId, BigDecimal cash, BigDecimal mp, BigDecimal level) {
        return SwapCoinUtils.coin(instId, cash, mp, level);
    }


    protected static Side close(PositionsSide posSide) {
        if (posSide == PositionsSide.LONG) {
            return Side.SELL;
        } else if (posSide == PositionsSide.SHORT) {
            return Side.BUY;
        }
        throw new TodoRuntimeException();
    }

    protected static Side open(PositionsSide posSide) {
        if (posSide == PositionsSide.LONG) {
            return Side.BUY;
        } else if (posSide == PositionsSide.SHORT) {
            return Side.SELL;
        }
        throw new TodoRuntimeException();
    }


}
