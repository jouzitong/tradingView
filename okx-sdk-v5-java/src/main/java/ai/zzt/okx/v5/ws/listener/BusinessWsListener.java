package ai.zzt.okx.v5.ws.listener;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import ai.zzt.okx.v5.enumeration.ws.WsChannel;
import ai.zzt.okx.v5.ws.OkxWsApiService;

/**
 * business channel ws listener
 */
@Slf4j
public class BusinessWsListener extends BaseWsListener {

    public BusinessWsListener(@NotNull OkxWsApiService okxWsApiService) {
        super(WsChannel.BUSINESS, okxWsApiService);
    }

    @Override
    protected void afterConnected() {
        okxWsApiService.getSubscribeStateService().restoreSubscribed(WsChannel.BUSINESS);
    }
}
