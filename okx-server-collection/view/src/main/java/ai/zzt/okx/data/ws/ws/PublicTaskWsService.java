package ai.zzt.okx.data.ws.ws;

import ai.zzt.okx.common.constant.Code;
import ai.zzt.okx.common.utils.ApplicationContextUtils;
import ai.zzt.okx.common.utils.json.JackJsonUtils;
import ai.zzt.okx.common.vo.RW;
import ai.zzt.okx.data.ws.dispatcher.WebSocketDispatcher;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 公共数据 websocket service api
 *
 * @author zhouzhitong
 * @since 2024/10/28
 **/
@Service
@Slf4j
@ServerEndpoint("/ws/v1/public/{uuid}")
public class PublicTaskWsService {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static final AtomicInteger onlineCount = new AtomicInteger(0);

    private static WebSocketDispatcher webSocketDispatcher;

    /**
     * 连接建立成功调用的方法
     *
     * @param session session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("uuid") String uuid) throws IOException {
        addOnlineCount(); // 在线数加1
        log.info("新客户端连接: {}. 第 {} 个客户", uuid, getOnlineCount());
        try {
            if (webSocketDispatcher == null) {
                webSocketDispatcher = ApplicationContextUtils.getBean(WebSocketDispatcher.class, true);
            }
            webSocketDispatcher.onOpen(uuid, session);
        } catch (Exception e) {
            log.error("", e);
            sendMsg(session, RW.of(Code.FAIL.code(), e.getMessage()));
            session.close();
        }
    }

    /**
     * 收到客户端消息后调用的方法
     * 客户端发送过来的消息
     *
     * @param message 客户端发送过来的消息
     * @param session session
     */
    @OnMessage
    public void onMessage(@PathParam("uuid") String uuid, String message, Session session) {
        log.debug("收到来自窗口的信息: {}", message);
        if ("ping".equals(message)) {
            sendMsg(session, "pong");
            return;
        }
        try {
            webSocketDispatcher.onMessage(uuid, message, session);
        } catch (Exception e) {
            log.error("onMessage error. msg: {}", message, e);
            sendMsg(session, RW.of(Code.FAIL.code(), e.getMessage()));
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("uuid") String uuid) {
        subOnlineCount(); //在线数减1
        log.info("有一连接关闭！当前在线人数为: {}", getOnlineCount());
        webSocketDispatcher.onClose(uuid, session);
    }


    /**
     * @param session session
     * @param error   异常信息
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误: ", error);
    }

    public static void sendMsg(Session session, Object obj) {
        String msg = JackJsonUtils.toStr(obj);
        sendMsg(session, msg);
    }

    public static void sendMsg(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized void addOnlineCount() {
        PublicTaskWsService.onlineCount.incrementAndGet();
    }

    public static synchronized void subOnlineCount() {
        PublicTaskWsService.onlineCount.decrementAndGet();
    }

    public static synchronized int getOnlineCount() {
        return onlineCount.get();
    }


}
