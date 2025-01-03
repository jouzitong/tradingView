package ai.zzt.okx.web.handler;

import ai.zzt.okx.common.constant.WsOpCode;
import ai.zzt.okx.web.dto.EmulatorWsTask;
import ai.zzt.okx.web.req.WsEmulatorReq;

/**
 * @author zhouzhitong
 * @since 2024/11/17
 **/
public interface WsEmulatorHandler {

    /**
     * 操作 code
     *
     * @return 操作 code
     */
    WsOpCode opCode();

    /**
     * 处理消息
     *
     * @param context 上下文
     * @param req     请求参数
     */
    void doProcess(EmulatorWsTask context, WsEmulatorReq req);

}
