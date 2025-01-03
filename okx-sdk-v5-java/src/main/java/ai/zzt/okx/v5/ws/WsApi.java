package ai.zzt.okx.v5.ws;

import ai.zzt.okx.v5.entity.ws.request.WsChannelRequestArg;
import ai.zzt.okx.v5.enumeration.ws.Operation;
import ai.zzt.okx.v5.enumeration.ws.WsChannel;

/**
 * @author Jou Ziton
 * @since 2024/5/18
 **/
public interface WsApi {

    /**
     * 操作ws 消息
     *
     * @param op         操作类型
     * @param wsChannel  ws通道
     * @param requestArg 请求参数
     * @return TODO 在那时没有用
     */
    default Object operate(Operation op, WsChannel wsChannel, WsChannelRequestArg requestArg) {
        switch (op) {
            case SUBSCRIBE:
                return subscribe(wsChannel, requestArg);
            case UNSUBSCRIBE:
                return unsubscribe(wsChannel, requestArg);
            default:
                throw new IllegalArgumentException("unsupported operation: " + op);
        }
    }


    /**
     * 订阅
     *
     * @param wsChannel  ws channel
     * @param requestArg 请求参数
     */
    Object subscribe(WsChannel wsChannel, WsChannelRequestArg requestArg);

    /**
     * 取消订阅
     *
     * @param wsChannel  ws channel
     * @param requestArg 请求参数
     */
    Object unsubscribe(WsChannel wsChannel, WsChannelRequestArg requestArg);

}
