package ai.zzt.okx.data.service.impl;

import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.dispatcher.service.OrderService;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmptyOrderServiceImpl implements OrderService {

    @Override
    public boolean placeOrder(TradeOrderRequest request) {
        return false;
    }

    @Override
    public boolean closeOrder(TradeOrderRequest request) {
        return false;
    }

    @Override
    public InstrumentType type() {
        return null;
    }
}
