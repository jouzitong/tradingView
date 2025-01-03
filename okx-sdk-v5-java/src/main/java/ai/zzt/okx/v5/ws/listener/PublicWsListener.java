package ai.zzt.okx.v5.ws.listener;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import ai.zzt.okx.v5.enumeration.ws.WsChannel;
import ai.zzt.okx.v5.ws.OkxWsApiService;

/**
 * public channel ws listener
 */
@Slf4j
public class PublicWsListener extends BaseWsListener {

    public PublicWsListener(@NotNull OkxWsApiService okxWsApiService) {
        super(WsChannel.PUBLIC, okxWsApiService);
    }

    @Override
    protected void afterConnected() {
        okxWsApiService.getSubscribeStateService().restoreSubscribed(WsChannel.PUBLIC);
    }

}
