package ai.zzt.okx.tradeOrder.serivce;

import ai.zzt.okx.common.exception.TodoRuntimeException;
import ai.zzt.okx.okx_client.context.InstrumentContext;
import ai.zzt.okx.okx_client.serivce.OkxWsApiServiceWrapper;
import ai.zzt.okx.tradeOrder.engine.TradeOrderEngine;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 自动下单服务
 *
 * @author zhouzhitong
 * @since 2024/9/30
 **/
@Service
@Slf4j
@AllArgsConstructor
public class TradingService {

    private final TradeOrderEngine tradeOrderEngine;

    private final OkxWsApiServiceWrapper okxWsApiService;

    public void update(String instId) {
        if (!InstrumentContext.isEnable(instId)) {
            throw new TodoRuntimeException("产品" + instId + " 当前不可交易");
        }
        remove(instId);
        add(instId);
    }

    public void add(String instId) {
        if (!InstrumentContext.isEnable(instId)) {
            throw new TodoRuntimeException("产品" + instId + " 当前不可交易");
        }
        try {
            tradeOrderEngine.init(instId);
            okxWsApiService.subscribeInstId(instId);
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    public void remove(String instId) {
        okxWsApiService.unsubscribeInstId(instId);
        tradeOrderEngine.delete(instId);
    }

}