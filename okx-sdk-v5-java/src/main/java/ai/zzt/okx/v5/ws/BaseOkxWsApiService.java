package ai.zzt.okx.v5.ws;

import ai.zzt.okx.v5.entity.ws.request.WsRequest;
import ai.zzt.okx.v5.entity.ws.request.WsRequestArg;
import ai.zzt.okx.v5.entity.ws.request.pri.LoginArg;
import ai.zzt.okx.v5.enumeration.ws.Operation;
import ai.zzt.okx.v5.enumeration.ws.WsChannel;
import ai.zzt.okx.v5.util.SignUtils;
import ai.zzt.okx.v5.ws.factory.OkxWsApiFactory;
import ai.zzt.okx.v5.ws.pri.AccountWsApi;
import ai.zzt.okx.v5.ws.pri.OrderWsApi;
import ai.zzt.okx.v5.ws.pub.OpenDataWsApi;
import com.alibaba.fastjson2.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.WebSocket;
import okio.ByteString;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouzhitong
 * @since 2024/12/30
 **/
@Slf4j
public abstract class BaseOkxWsApiService implements AccountWsApi, OpenDataWsApi, OrderWsApi {
    protected static final int PING_INTERVAL_SEC = 10;
    protected static final int MAX_RECONNECT_COUNT = 1000;
    protected static final int RECONNECT_INTERVAL = 5000;

    protected final OkHttpClient client;
    protected final boolean simulated;

    /**
     * ws 通道连接客户端
     */
    protected final Map<WsChannel, WebSocket> wsClientMap;
    /**
     * 重新连接次数
     */
    protected final Map<WsChannel, Integer> reconnectCountMap;

    /**
     * 客户端是否连接成功
     */
    protected final Map<WsChannel, Boolean> isConnectMap;

    @Setter
    @Getter
    protected volatile boolean hasLogin;

    public BaseOkxWsApiService() {
        this(false);
    }

    public BaseOkxWsApiService(boolean simulated) {
        this(OkxWsApiFactory.defaultClient(Duration.ofSeconds(PING_INTERVAL_SEC)), false);
    }

    public BaseOkxWsApiService(OkHttpClient client, boolean simulated) {
        this.client = client;
        this.simulated = simulated;
        this.wsClientMap = new HashMap<>();
        this.reconnectCountMap = new HashMap<>();
        this.isConnectMap = new HashMap<>();

        for (WsChannel c : WsChannel.values()) {
            this.reconnectCountMap.put(c, 0);
            this.isConnectMap.put(c, false);
        }
    }

    public abstract void connect(WsChannel wsChannel);

    /**
     * send string message
     *
     * @param wsChannel ws channel
     * @param message   message
     */
    public void send(WsChannel wsChannel, final String message) {
        log.debug("send message: {} {}", wsChannel, message);
        if (!isConnected(wsChannel)) {
            log.warn("websocket not connected: {}", wsChannel);
            return;
        }
        wsClientMap.get(wsChannel).send(message);
    }

    /**
     * send byte message
     *
     * @param wsChannel wx channel
     * @param message   message
     */
    public void send(WsChannel wsChannel, final ByteString message) {
        log.debug("send byteMessage: {} {}", wsChannel, message);
        if (!isConnected(wsChannel)) {
            return;
        }
        wsClientMap.get(wsChannel).send(message);
    }

    /**
     * reconnect
     *
     * @param wsChannel ws channel
     */
    @SneakyThrows
    public void reconnect(WsChannel wsChannel) {
        if (!isConnected(wsChannel)) {
            if (wsChannel == WsChannel.PRIVATE) {
                hasLogin = false;
            }
            log.debug("reconnect: {} count: {}", wsChannel, reconnectCountMap.get(wsChannel));
            if (reconnectCountMap.get(wsChannel) < MAX_RECONNECT_COUNT) {
                if (reconnectCountMap.get(wsChannel) > 0) { // 如果不是第一次, 就等待一段时间再重连
                    TimeUnit.MILLISECONDS.sleep(RECONNECT_INTERVAL);
                }
                if (!isConnected(wsChannel)) {
                    connect(wsChannel);
                    reconnectCountMap.put(wsChannel, reconnectCountMap.get(wsChannel) + 1);
                }
            } else {
                log.error("❌❌❌ Reconnect over {} times for channel {}, please check url or network!!!", MAX_RECONNECT_COUNT, wsChannel);
            }
        }
    }


    /**
     * 是否连接
     *
     * @param wsChannel channel
     * @return isConnect
     */
    public boolean isConnected(WsChannel wsChannel) {
        return wsClientMap.get(wsChannel) != null && isConnectMap.get(wsChannel);
    }

    /**
     * Manually close connection
     *
     * @param wsChannel ws channel
     * @param code      code
     * @param reason    reason
     */
    public void disconnect(WsChannel wsChannel, int code, String reason) {
        log.info("disconnect code: {} reason: {}", code, reason);
        if (!isConnected(wsChannel)) {
            return;
        }
        wsClientMap.get(wsChannel).close(code, reason);
    }

    //********************************** private channel
    public void login(LoginArg loginArg, String secretKey) {
        if (hasLogin) {
            log.warn("already login");
            return;
        }
        loginArg.setSign(SignUtils.signWebsocket(loginArg, secretKey));
        WsRequest<WsRequestArg> wsRequest = WsRequest.builder()
                .op(Operation.LOGIN)
                .args(List.of(loginArg)).build();
        send(WsChannel.PRIVATE, JSON.toJSONString(wsRequest));
    }


    /**
     * Set it when connected
     *
     * @param wsChannel ws channel
     * @param webSocket web socket
     */
    public void setWebSocket(WsChannel wsChannel, WebSocket webSocket) {
        wsClientMap.put(wsChannel, webSocket);
    }

    public void resetConnectCount(WsChannel wsChannel) {
        reconnectCountMap.put(wsChannel, 0);
    }

    public void setConnectState(WsChannel wsChannel, boolean state) {
        isConnectMap.put(wsChannel, state);
    }


}
