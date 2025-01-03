package ai.zzt.okx.emulator.service.impl;

import ai.zzt.okx.emulator.utils.creator.OrderUtils;
import ai.zzt.okx.okx_client.context.OrderContext;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.dispatcher.service.OrderService;
import ai.zzt.okx.v5.entity.ws.pri.Order;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmulateOrderServiceImpl implements OrderService {

    @Override
    public boolean placeOrder(TradeOrderRequest request) {
        Order order = OrderUtils.buildOrderOfOpen(request);
        OrderContext orderContext = request.getAccountContext().getOrderContext();
        orderContext.add(order);
        return true;
    }

    @Override
    public boolean closeOrder(TradeOrderRequest request) {
        Order order = OrderUtils.buildOrderOfClose(request);
        OrderContext orderContext = request.getAccountContext().getOrderContext();
        orderContext.clear();
        orderContext.add(order);
//        contextList.getOrders(request.getInstId()).clear();
//        contextList.add(order);
        return true;
    }

    @Override
    public InstrumentType type() {
        return InstrumentType.SWAP;
    }
}
