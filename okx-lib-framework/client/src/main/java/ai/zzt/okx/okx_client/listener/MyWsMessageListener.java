package ai.zzt.okx.okx_client.listener;

import ai.zzt.okx.common.base.event.EventPublisher;
import ai.zzt.okx.okx_client.context.IAccountContext;
import ai.zzt.okx.okx_client.context.MarketContextList;
import ai.zzt.okx.okx_client.context.OrderContext;
import ai.zzt.okx.okx_client.context.PositionContext;
import ai.zzt.okx.okx_client.listener.event.MarkPriceEvent;
import ai.zzt.okx.v5.entity.ws.WsSubscribeEntity;
import ai.zzt.okx.v5.entity.ws.biz.AlgoOrder;
import ai.zzt.okx.v5.entity.ws.pri.Order;
import ai.zzt.okx.v5.entity.ws.pri.Positions;
import ai.zzt.okx.v5.entity.ws.pub.FundingRate;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.entity.ws.response.WsChannelResponseArg;
import ai.zzt.okx.v5.entity.ws.response.WsResponseArg;
import ai.zzt.okx.v5.entity.ws.response.WsSubscribeResponse;
import ai.zzt.okx.v5.entity.ws.response.biz.AlgoOrderArg;
import ai.zzt.okx.v5.entity.ws.response.pri.OrderArg;
import ai.zzt.okx.v5.entity.ws.response.pri.PositionsArg;
import ai.zzt.okx.v5.entity.ws.response.pub.FundingRateArg;
import ai.zzt.okx.v5.entity.ws.response.pub.MarkPriceArg;
import ai.zzt.okx.v5.ws.listener.WsMessageListener;
import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/4/29
 **/
@Service
@Slf4j
public class MyWsMessageListener extends WsMessageListener {

    @Resource
    private EventPublisher eventPublisher;

    @Autowired
    private MarketContextList marketContextList;

    @Resource
    private IAccountContext accountContext;

    public MyWsMessageListener() {
        super();
    }

    @Override
    public void onLoginSuccess() {
        log.info("onLoginSuccess");
    }

    public void onReceiveFundingRate(@NonNull WsSubscribeResponse<FundingRateArg, FundingRate> response) {

    }

    @Override
    public void onReceiveMarkPrice(@NonNull WsSubscribeResponse<MarkPriceArg, MarkPrice> response) {
//        super.onReceiveMarkPrice(response);
        handle(response);
    }

    private void handle(WsSubscribeResponse<? extends WsResponseArg, ? extends WsSubscribeEntity> response) {
        if (!(response.getArg() instanceof WsChannelResponseArg)) {
            return;
        }
        List<MarkPrice> data = (List<MarkPrice>) response.getData();
        MarkPriceEvent event = new MarkPriceEvent(data);
        eventPublisher.publish(event);
    }


    public void onReceivePositions(@NonNull WsSubscribeResponse<PositionsArg, Positions> response) {
        if (response.getArg() == null) {
            return;
        }
        List<Positions> data = response.getData();
        PositionContext positionContext = accountContext.getPositionContext();
        positionContext.addAll(data);
    }

    public void onReceiveAlgoOrders(@NonNull WsSubscribeResponse<AlgoOrderArg, AlgoOrder> response) {
        if (response.getArg() == null) {
            return;
        }
        List<AlgoOrder> data = response.getData();
        OrderContext orderContext = accountContext.getOrderContext();
        for (AlgoOrder ao : data) {
            orderContext.add(ao);
        }
    }

    @Override
    public void onReceiveOrders(@NonNull WsSubscribeResponse<OrderArg, Order> response) {
        if (response.getArg() == null) {
            return;
        }
        List<Order> data = response.getData();
        OrderContext orderContext = accountContext.getOrderContext();
        for (Order o : data) {
            orderContext.add(o);
        }
    }
}
