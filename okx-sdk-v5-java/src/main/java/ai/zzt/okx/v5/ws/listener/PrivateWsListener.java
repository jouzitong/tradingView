package ai.zzt.okx.v5.ws.listener;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import ai.zzt.okx.v5.enumeration.ws.WsChannel;
import ai.zzt.okx.v5.ws.OkxWsApiService;

/**
 * private channel ws listener
 */
@Slf4j
public class PrivateWsListener extends BaseWsListener {

    public PrivateWsListener(@NotNull OkxWsApiService okxWsApiService) {
        super(WsChannel.PRIVATE, okxWsApiService);
    }

    @Override
    protected void afterConnected() {
        okxWsApiService.getSubscribeStateService().tryLogin();
    }
}
