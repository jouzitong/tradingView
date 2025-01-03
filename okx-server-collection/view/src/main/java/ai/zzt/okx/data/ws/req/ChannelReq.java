package ai.zzt.okx.data.ws.req;

import lombok.Data;
import org.jetbrains.annotations.Nullable;
import ai.zzt.okx.v5.enumeration.Bar;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;

import java.util.Set;

/**
 * @author zhouzhitong
 * @since 2024/10/28
 **/
@Data
public class ChannelReq {

    /**
     * 管道名称
     */
    private String channel;

    /**
     * 限制条数
     */
    private int limit;

    /**
     * 推送间隔时间
     */
    private int interval;

    /**
     * 指定趋势类型. up、down
     */
    private String trend;

    /**
     * 产品ID
     */
    private String instId;

    /**
     * 产品类型
     */
    @Nullable
    private Set<InstrumentType> instType;

    /**
     * 推送周期
     */
    private Bar bar;

}
