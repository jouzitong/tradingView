package ai.zzt.okx.dispatcher.manage;

import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;

/**
 * 请求管理器
 *
 * @author zhouzhitong
 * @since 2024/6/6
 **/
public interface RequestManager {

    TradeOrderRequest buildRequest(MarkPrice mp);

    default TradeOrderRequest get(String instId) {
        return null;
    }

    boolean remove(String instId);

}
