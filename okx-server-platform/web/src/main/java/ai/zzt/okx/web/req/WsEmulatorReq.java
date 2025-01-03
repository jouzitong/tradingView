package ai.zzt.okx.web.req;

import ai.zzt.okx.v5.enumeration.Bar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * @author zhouzhitong
 * @since 2024/7/18
 **/
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsEmulatorReq implements Serializable {

    /**
     * 每个客户端唯一标识符，用于区分不同用户的K线数据
     */
    @NotNull
    private String uid;

    /**
     * 产品ID
     */
    @NotNull
    private String instId;

    /**
     * 时间周期. 默认, 1小时
     */
    private Bar bar = Bar.H_1;

    /**
     * 限制条数
     */
    private long limit = 1000;

    /**
     * 是否自动推送，默认true
     */
    private boolean autoPush = true;

    /**
     * 推送时间间隔, 单位: 毫秒. 默认为1000. 这个值只适用于 autoPush = true 的情况.
     */
    private int interval = 1000;

    /**
     * 配置上下文
     */
    private String settingsContext;

    /**
     * 开启时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;


    public String getInstId() {
        if (instId.split("-").length == 1) {
            return instId + "-USDT-SWAP";
        }
        return this.instId;
    }

    public void setBar(String barCode) {
        Bar bar = Bar.of(barCode);
        if (bar != null) {
            this.bar = bar;
        }
    }
}
