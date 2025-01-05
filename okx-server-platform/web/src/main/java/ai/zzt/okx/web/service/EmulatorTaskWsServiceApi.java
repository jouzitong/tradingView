package ai.zzt.okx.web.service;

import ai.zzt.okx.common.constant.Code;
import ai.zzt.okx.common.constant.WsOpCode;
import ai.zzt.okx.common.utils.DateTimeUtils;
import ai.zzt.okx.common.utils.ThreadUtils;
import ai.zzt.okx.common.utils.json.JackJsonUtils;
import ai.zzt.okx.common.vo.RW;
import ai.zzt.okx.emulator.callBack.CustomerTaskDTO;
import ai.zzt.okx.web.dto.EmulatorWsTask;
import ai.zzt.okx.web.engine.WsEmulateEngine;
import ai.zzt.okx.web.req.KLineResp;
import ai.zzt.okx.web.req.KLineVO;
import ai.zzt.okx.web.req.WsEmulatorReq;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author zhouzhitong
 * @since 2024/8/3
 **/
@Service
@Slf4j
public class EmulatorTaskWsServiceApi {

    // TODO 应该要有一个定时任务, 定时清理过期的连接
    private final Map<String, EmulatorWsTask> emulatorWsTaskMap = new ConcurrentHashMap<>();

    public void onOpen(Session session) {

    }

    public void onMessage(String msg, Session session) {
        log.trace("收到客户端消息: {}", msg);
        RW<WsEmulatorReq> rw = JackJsonUtils.toBean(msg, new TypeReference<RW<WsEmulatorReq>>() {
        });
        if (rw.getCode() == Code.PING.code()) {
            // 发送pong消息
            sendMsg(session, RW.of(Code.PONG.code()));
        }
        WsOpCode wsOpCode = rw.getOpCode();
        if (wsOpCode == null) {
            log.error("opcode为空");
            // TODO 发送消息
            return;
        }
        String uid = rw.getData().getUid();
        EmulatorWsTask emulatorWsTask = emulatorWsTaskMap.get(uid);
        if (emulatorWsTask != null) {
            // 更新 session
            emulatorWsTask.setSession(session);
        }

        switch (wsOpCode) {
            case REQUEST_SUB: { // 请求订阅数据
                WsEmulatorReq req = rw.getData();
                CustomerTaskDTO task = new CustomerTaskDTO(req.getInstId(), session.getId());
                if (req.getStartTime() != null && !req.getStartTime().isBlank()) {
                    long st = DateTimeUtils.ofSecond(req.getStartTime());
                    task.setStartTime(st);
                }
                if (req.getEndTime() != null && !req.getEndTime().isBlank()) {
                    long et = DateTimeUtils.ofSecond(req.getEndTime());
                    task.setEndTime(et);
                }
                WsEmulateEngine tradeTask = new WsEmulateEngine(session, req, task);
                emulatorWsTask = new EmulatorWsTask();
                emulatorWsTask.setUid(req.getUid());
                emulatorWsTask.setInstId(req.getInstId());
                emulatorWsTask.setSession(session);
                emulatorWsTask.setReq(req);
                emulatorWsTask.setTradeTask(tradeTask);
                emulatorWsTaskMap.put(req.getUid(), emulatorWsTask);
                ThreadUtils.runTask(tradeTask);
                sendMsg(session, rw);
                break;
            }
            case REQUEST_PUSH: { // 请求推送数据
                WsEmulatorReq req = rw.getData();
                emulatorWsTask = emulatorWsTaskMap.get(uid);
                if (emulatorWsTask.getReq().isAutoPush()) {
                    log.warn("自动推送数据, 无需请求推送数据");
                    return;
                }
                List<KLineVO> dataList = emulatorWsTask.take();

                KLineResp resp = new KLineResp();
                resp.setBar(req.getBar());
                resp.setInstId(req.getInstId());
                resp.setDataList(dataList);
                RW<KLineResp> res = RW.ok(WsOpCode.K_LINE_RESPONSE_PUSH, resp);
                sendMsg(session, res);
                break;
            }
            case CLOSE_REQUEST: { // 请求关闭连接
                emulatorWsTask = emulatorWsTaskMap.get(uid);
                if (emulatorWsTask == null) {
                    log.warn("【关闭】未找到对应的uid: {}", uid);
                    return;
                }
                emulatorWsTask.close();
                emulatorWsTaskMap.remove(uid);
                break;
            }
        }
    }

    public void onClose(Session session) {
        String key = null;
        for (String k : emulatorWsTaskMap.keySet()) {
            EmulatorWsTask emulatorWsTask = emulatorWsTaskMap.get(k);
            if (session.getId().equals(emulatorWsTask.getSession().getId())) {
                key = k;
                break;
            }
        }
        if (key == null) {
            log.warn("未找到对应的session");
            return;
        }
        EmulatorWsTask emulatorWsTask = emulatorWsTaskMap.get(key);
        emulatorWsTask.close();
        emulatorWsTaskMap.remove(key);
    }

    public static void sendMsg(Session session, Object obj) {
        session.getAsyncRemote().sendText(JackJsonUtils.toStr(obj));
    }

}
