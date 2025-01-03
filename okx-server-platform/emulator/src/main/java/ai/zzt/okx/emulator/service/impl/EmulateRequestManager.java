package ai.zzt.okx.emulator.service.impl;

import ai.zzt.okx.emulator.engine.BaseEmulateEngine;
import ai.zzt.okx.dispatcher.manage.RequestManager;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@Component
public class EmulateRequestManager implements RequestManager {

    @Override
    public TradeOrderRequest buildRequest(MarkPrice mp) {
        TradeOrderRequest request = BaseEmulateEngine.getRequest();
        request.clear();
        request.setMarkPrice(mp);
        return request;
    }

    @Override
    public boolean remove(String instId) {
        return false;
    }
}
