package ai.zzt.okx.data.vo;

import lombok.Data;
import ai.zzt.okx.v5.enumeration.Bar;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/7/18
 **/
@Data
public class KLineResp implements Serializable {

    /**
     * 产品ID
     */
    private String instId;

    /**
     * <pre>
     * 0. 开盘时间
     * 1. 开盘价
     * 2. 收盘价
     * 3. 最低价
     * 4. 最高价
     *
     * 5. 成交量
     * 6. 成交额
     *
     * 7. macd
     * 8. dif
     * 9. dea
     * 10. RSI
     * </pre>
     */
    private List<Object[]> dataList;

    /**
     * 时间周期
     */
    private Bar bar;

    public long getScale() {
        return bar.getScale();
    }

}
