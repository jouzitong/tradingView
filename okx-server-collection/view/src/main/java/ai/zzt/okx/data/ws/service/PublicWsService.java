package ai.zzt.okx.data.ws.service;

import ai.zzt.okx.data.ws.context.WsClientContext;

/**
 * @author zhouzhitong
 * @since 2024/10/31
 **/
public interface PublicWsService<T> {

    T onMessage(WsClientContext wsContext);

}
