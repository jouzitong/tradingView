package ai.zzt.okx.data.ws.service;

import ai.zzt.okx.data.ws.context.WsClientContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouzhitong
 * @since 2024/11/2
 **/
@Slf4j
public abstract class BasePublicWsService<T> implements PublicWsService<T> {

    @Override
    public T onMessage(WsClientContext wsContext) {
        if (wsContext.isClose()) {
            return null;
        }
        return doService(wsContext);
    }

    protected abstract T doService(WsClientContext wsContext);

}
