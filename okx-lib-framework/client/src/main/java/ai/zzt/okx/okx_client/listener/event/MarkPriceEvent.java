package ai.zzt.okx.okx_client.listener.event;

import ai.zzt.okx.common.base.event.OkxEvent;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/5/19
 **/
public record MarkPriceEvent(List<MarkPrice> markPrices) implements OkxEvent {

}
