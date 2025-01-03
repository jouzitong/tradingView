package ai.zzt.okx.data.ws.context;

import ai.zzt.okx.common.context.SystemContext;
import ai.zzt.okx.common.web.base.ws.IParam;
import ai.zzt.okx.data.ws.request.vo.ChannelParams;
import jakarta.websocket.Session;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author zhouzhitong
 * @since 2024/10/31
 **/
@Slf4j
@Data
public class WsClientContext {

    /**
     * 客户端 唯一ID
     */
    private String uuid;

    /**
     * websocket 通信 session
     */
    private Session session;

    /**
     * 建立连接时间
     */
    private long openTime;

    /**
     * 断开连接时间
     */
    private Long closeTime;

    /**
     * 请求参数 --- 临时
     */
    private IParam<ChannelParams> args;

    public boolean isClose() {
        return closeTime != null;
    }

    public boolean close() {
        try {
            if (this.session.isOpen()) {
                this.session.close();
            }
            return true;
        } catch (IOException e) {
            log.error("关闭失败, ", e);
            throw new RuntimeException(e);
        } finally {
            this.closeTime = SystemContext.currentTimeMillis();
        }
    }

}
