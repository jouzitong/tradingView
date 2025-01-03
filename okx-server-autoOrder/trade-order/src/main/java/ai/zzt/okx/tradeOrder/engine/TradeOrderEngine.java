package ai.zzt.okx.tradeOrder.engine;

import ai.zzt.okx.storage.engine.IKLineEngine;
import ai.zzt.okx.okx_client.listener.event.MarkPriceEvent;
import ai.zzt.okx.dispatcher.engine.BaseTradeOrderEngine;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/6/4
 **/
@Service
@Slf4j
public class TradeOrderEngine extends BaseTradeOrderEngine {

    @Resource
    private IKLineEngine ikLineEngine;

    /**
     * 监听市场数据
     *
     * @param event 市场数据事件
     */
    @EventListener
    public final void handle(MarkPriceEvent event) {
        List<MarkPrice> mps = event.markPrices();
        for (MarkPrice mp : mps) {
            ikLineEngine.addMp(mp);
        }
        dispatch(mps);
    }

}
