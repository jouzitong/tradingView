package ai.zzt.okx.web.req;

import ai.zzt.okx.v5.enumeration.Bar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/7/18
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private List<KLineVO> dataList;

    /**
     * 时间周期
     */
    private Bar bar;

}
