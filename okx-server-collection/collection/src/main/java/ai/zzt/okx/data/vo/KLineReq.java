package ai.zzt.okx.data.vo;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import ai.zzt.okx.v5.enumeration.Bar;

import java.io.Serializable;

/**
 * @author zhouzhitong
 * @since 2024/7/18
 **/
@Data
public class KLineReq implements Serializable {

    /**
     * 每个客户端唯一标识符，用于区分不同用户的K线数据
     */
    @NotNull
    private String uid;

    /**
     * 产品ID
     */
    private String instId;

    /**
     * 时间周期
     */
    private Bar bar;

    /**
     * 最近更新时间.
     * <p>
     * 1. 开始订阅的时间, 申请订阅时, 这个参数必传
     * <p>
     * 2. 请求全量数据时的起始时间, 这个参数可选, 若不传, 则默认取当前时间
     */
    private Long time;

    private long limit = 1000;

    public long getScale() {
        return bar.getScale();
    }

}
