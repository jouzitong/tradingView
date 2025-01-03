package ai.zzt.okx.web.web.ws;

import ai.zzt.okx.common.constant.Code;
import ai.zzt.okx.common.utils.ApplicationContextUtils;
import ai.zzt.okx.common.utils.json.JackJsonUtils;
import ai.zzt.okx.common.vo.RW;
import ai.zzt.okx.web.service.EmulatorTaskWsServiceApi;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhouzhitong
 * @since 2024/6/21
 **/
@ServerEndpoint("/emulatorTask")
@Service
@Slf4j
public class EmulatorTaskWsService {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static final AtomicInteger onlineCount = new AtomicInteger(0);

    private EmulatorTaskWsServiceApi emulatorTaskWsService;

    /**
     * 连接建立成功调用的方法
     *
     * @param session session
     */
    @OnOpen
    public void onOpen(Session session) {
        log.info("有新窗口开始监听: " + session.getId());
        addOnlineCount(); //在线数加1
        if (emulatorTaskWsService == null) {
            emulatorTaskWsService = ApplicationContextUtils.getBean(EmulatorTaskWsServiceApi.class);
        }
        emulatorTaskWsService.onOpen(session);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        subOnlineCount(); //在线数减1
        log.info("有一连接关闭！当前在线人数为: {}", getOnlineCount());
        if (emulatorTaskWsService == null) {
            emulatorTaskWsService = ApplicationContextUtils.getBean(EmulatorTaskWsServiceApi.class);
        }
        emulatorTaskWsService.onClose(session);
    }

    /**
     * 收到客户端消息后调用的方法
     * 客户端发送过来的消息
     *
     * @param message 客户端发送过来的消息
     * @param session session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.trace("收到来自窗口的信息: {}", message);
        if (message.contains("ping")) {
            sendMsg(session, "pong");
            return;
        }
        if (emulatorTaskWsService == null) {
            emulatorTaskWsService = ApplicationContextUtils.getBean(EmulatorTaskWsServiceApi.class);
        }

        try {
            emulatorTaskWsService.onMessage(message, session);
        } catch (Exception e) {
            log.error("onMessage error. msg: {}", message, e);
            sendMsg(session, RW.of(Code.FAIL.code(), e.getMessage()));
        }
    }

    /**
     * @param session session
     * @param error   异常信息
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
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

    public static synchronized int getOnlineCount() {
        return onlineCount.get();
    }

    public static synchronized void addOnlineCount() {
        EmulatorTaskWsService.onlineCount.incrementAndGet();
    }

    public static synchronized void subOnlineCount() {
        EmulatorTaskWsService.onlineCount.decrementAndGet();
    }

}
