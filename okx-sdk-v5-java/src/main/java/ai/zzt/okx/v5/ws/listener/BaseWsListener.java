package ai.zzt.okx.v5.ws.listener;

import ai.zzt.okx.v5.enumeration.ws.WsChannel;
import ai.zzt.okx.v5.handler.WsHandler;
import ai.zzt.okx.v5.handler.WsHandlerFactory;
import ai.zzt.okx.v5.util.ScheduledUtils;
import ai.zzt.okx.v5.ws.OkxWsApiService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

import static ai.zzt.okx.v5.constant.OkxConstants.*;

/**
 * bases ws listener
 */
@Slf4j
public abstract class BaseWsListener extends WebSocketListener {

    protected final OkxWsApiService okxWsApiService;
    private final WsChannel wsChannel;

    private volatile boolean startHeater = false;

    public BaseWsListener(WsChannel wsChannel, OkxWsApiService okxWsApiService) {
        this.okxWsApiService = okxWsApiService;
        this.wsChannel = wsChannel;
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        log.info("WebSocket opened {}", wsChannel);
        okxWsApiService.setWebSocket(wsChannel, webSocket);
        okxWsApiService.resetConnectCount(wsChannel);
        boolean isConnected = response.code() == 101;
        okxWsApiService.setConnectState(wsChannel, isConnected);
        if (isConnected) {
            log.info("WebSocket connect success");
            startHeartbeatThread();
            afterConnected();

        } else {
            okxWsApiService.reconnect(wsChannel);
        }
    }

    protected void afterConnected() {

    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String message) {
        log.debug("WebSocket message: {} {}", wsChannel, message);
        if (HEARTBEAT_RSP_MESSAGE.equals(message)) {
            log.debug("WebSocket heartbeat response {}", message);
        } else {
            WsHandler handler = WsHandlerFactory.getHandler(wsChannel, okxWsApiService, message);
            if (handler != null) {
                handler.handle(okxWsApiService);
            }
        }
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
        log.info("WebSocket bytes message: {} {}", wsChannel, bytes);
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        log.warn("onClosed: {} {} {} {}", wsChannel, webSocket, code, reason);
        okxWsApiService.setWebSocket(wsChannel, null);
        okxWsApiService.resetConnectCount(wsChannel);
        okxWsApiService.setConnectState(wsChannel, false);
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        log.warn("onClosing: {} {} {} {}", wsChannel, webSocket, code, reason);
        okxWsApiService.setWebSocket(wsChannel, null);
        okxWsApiService.resetConnectCount(wsChannel);
        okxWsApiService.setConnectState(wsChannel, false);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        log.warn("onFailure: {} {} {} {}", wsChannel, webSocket, t, response);
        okxWsApiService.setWebSocket(wsChannel, null);
        okxWsApiService.setConnectState(wsChannel, false);
        if (t.getMessage() != null && t.getMessage().equals("Socket closed")) {
            okxWsApiService.reconnect(wsChannel);
        }
    }

    /**
     * start heartbeat thread. 这个应该只能被执行一次
     */
    private void startHeartbeatThread() {
        if (!startHeater) {
            synchronized (this) {
                if (!startHeater) {
                    startHeater = true;
                } else {
                    return;
                }
            }
        } else {
            return;
        }
        ScheduledUtils.schedule(ScheduledUtils.HEART_KEY,
                () -> {
                    try {
                        if (!okxWsApiService.isConnected(wsChannel)) {
                            log.warn("wsChannel:{} is not connected and reconnect", wsChannel);
                            okxWsApiService.reconnect(wsChannel);
                            return;
                        }
                        okxWsApiService.send(wsChannel, HEARTBEAT_REQ_MESSAGE);
                    } catch (Exception e) {
                        log.warn("send HeartbeatThread exception. ", e);
                    }
                },
                HEARTBEAT_INTERVAL_SEC, HEARTBEAT_INTERVAL_SEC, TimeUnit.SECONDS);

    }

}
