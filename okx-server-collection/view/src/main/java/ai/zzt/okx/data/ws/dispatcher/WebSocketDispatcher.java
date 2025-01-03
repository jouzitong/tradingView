package ai.zzt.okx.data.ws.dispatcher;

import ai.zzt.okx.common.constant.WsOpCode;
import ai.zzt.okx.common.context.SystemContext;
import ai.zzt.okx.common.utils.json.JackJsonUtils;
import ai.zzt.okx.data.ws.context.WsClientContext;
import ai.zzt.okx.data.ws.request.PublicParam;
import ai.zzt.okx.data.ws.request.vo.ChannelParams;
import ai.zzt.okx.data.ws.resp.WsResp;
import ai.zzt.okx.data.ws.service.PublicWsService;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.annotation.Resource;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouzhitong
 * @since 2024/10/31
 **/
@Component
@Slf4j
public class WebSocketDispatcher {

    @Resource
    private List<PublicWsService> wsServices;

    private final Map<String, WsClientContext> wsContextMap = new ConcurrentHashMap<>();

    public void onOpen(String uuid, Session session) {
        WsClientContext wsContext;
        if (!wsContextMap.containsKey(uuid)) {
            log.trace("接收到新客户端: {}", uuid);
            wsContext = new WsClientContext();
            wsContext.setUuid(uuid);
            wsContext.setSession(session);
            wsContextMap.put(uuid, wsContext);
        } else {
            log.debug("客户端 {} 重新连接进来.", uuid);
            wsContext = wsContextMap.get(uuid);
            wsContext.setSession(session);
        }
        wsContext.setOpenTime(SystemContext.currentTimeMillis());
        wsContext.setCloseTime(null);
    }

    public void onClose(String uuid, Session session) {
        if (!wsContextMap.containsKey(uuid)) {
            log.warn("【BUG】找不到需要关闭的连接 {}", uuid);
        }
        WsClientContext wsContext = wsContextMap.get(uuid);
        wsContext.setCloseTime(SystemContext.currentTimeMillis());
    }

    public void onMessage(String uuid, String msg, Session session) {
        WsClientContext wsContext = null;
        try {
            if (!wsContextMap.containsKey(uuid)) {
                log.warn("【BUG】接受客户端[{}]消息时, 找不到原先的上下文对象. 现在重新创建一个新的", uuid);
                onOpen(uuid, session);
            }
            wsContext = wsContextMap.get(uuid);
            if (wsContext == null) {
                log.error("");
                return;
            }

            // 解析参数
            PublicParam<ChannelParams> args = JackJsonUtils.toBean(msg, new TypeReference<>() {
            });
            wsContext.setArgs(args);

            if (args == null || CollectionUtils.isEmpty(args.getArgs())) {
                return;
            }
            WsOpCode opCode = wsContext.getArgs().getOpCode();
            // 如果是关闭请求
            if (opCode == WsOpCode.CLOSE_REQUEST) {
                WsResp resp = new WsResp();
                resp.setMsg("收到关闭请求, 并同意.");
                sendMsg(resp, wsContext);
                wsContext.close();
                wsContextMap.remove(wsContext.getUuid());
                return;
            }
            WsResp resp = doService(wsContext);
            sendMsg(resp, wsContext);
        } catch (Exception e) {
            log.error("", e);
            WsResp resp = new WsResp();
            resp.setCode(1);
            resp.setMsg(STR."未知异常: \{e.getMessage()}");
            if (wsContext != null) {
                sendMsg(resp, wsContext);
            }
        }
    }

    /**
     * 处理业务逻辑
     *
     * @param wsContext 客户端上下文
     * @return
     */
    private WsResp doService(WsClientContext wsContext) {
        if (wsContext.isClose()) {
            return null;
        }
        WsResp resp = new WsResp();
        // 调用业务逻辑
        for (PublicWsService<?> wsService : wsServices) {
            Object data = wsService.onMessage(wsContext);
            if (data != null) {
                resp.add(data);
            }
        }

        return resp;
    }

    /**
     * 发送消息
     *
     * @param resp      响应体
     * @param wsContext 客户端上下文
     */
    protected void sendMsg(WsResp resp, WsClientContext wsContext) {
        if (resp == null || resp.isEmpty()) {
            return;
        }
        Session session = wsContext.getSession();
        if (!session.isOpen()) {
            log.warn("{} 的ws 客户端连接关闭.", wsContext.getUuid());
            return;
        }
        try {
            session.getBasicRemote().sendText(JackJsonUtils.toStr(resp));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 【定时任务】定时清理无效且过期的连接
     */
    @Scheduled(cron = "0 */5 * * * *")
    public synchronized void scheduledClean() {
        log.trace("[定时任务] 清理无效连接");
        List<String> peedDeleteClients = new ArrayList<>();
        wsContextMap.values().forEach(wcc -> {
            String uuid = wcc.getUuid();
            // 如果连接已经关闭, 并且关闭时间大于10分钟, 则删除
            if (wcc.isClose() && SystemContext.currentTimeMillis() - wcc.getCloseTime() > 1000L * 60 * 10) {
                peedDeleteClients.add(uuid);
            }

        });
        if (peedDeleteClients.isEmpty()) {
            return;
        }
        log.debug("即将清理客户端数据: {}", peedDeleteClients);
        for (String peedDeleteClient : peedDeleteClients) {
            wsContextMap.remove(peedDeleteClient);
        }
    }

    /**
     * 【定时任务】定时发送消息
     */
    @Scheduled(cron = "*/20 * * * * *")
    public synchronized void scheduledSendMsg() {
        log.trace("[定时任务] 发送消息");
        wsContextMap.values().forEach(wcc -> {
            WsResp resp = doService(wcc);
            sendMsg(resp, wcc);
        });
    }

}
