package ai.zzt.okx.web.dto;

import ai.zzt.okx.common.exception.TodoRuntimeException;
import ai.zzt.okx.common.utils.json.JackJsonUtils;
import ai.zzt.okx.web.engine.WsEmulateEngine;
import ai.zzt.okx.web.req.KLineVO;
import ai.zzt.okx.web.req.WsEmulatorReq;
import jakarta.websocket.Session;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/8/3
 **/
@Setter
@Getter
@Slf4j
public class EmulatorWsTask {

    /**
     * 客户端唯一编码
     */
    private String uid;

    /**
     * 产品ID
     */
    private String instId;

    /**
     * ws 会话连接
     */
    private Session session;

    /**
     * 请求参数, 也可以理解为配置参数
     */
    private WsEmulatorReq req;

    private WsEmulateEngine tradeTask;

    public List<KLineVO> take() {
        try {
            return tradeTask.take();
        } catch (Exception e) {
            log.error("take error", e);
            throw new TodoRuntimeException();
        }
    }

    public void close() {
//        try {
//            this.session.close();
//        } catch (IOException e) {
//            log.warn("", e);
//        }
        tradeTask.cancel();
    }

    public void sendMsg(Object obj) {
        if (this.session == null || !this.session.isOpen()) {
            return;
        }
        this.session.getAsyncRemote().sendText(JackJsonUtils.toStr(obj));
    }


}
