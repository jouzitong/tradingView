package ai.zzt.okx.trading.dto;

import ai.zzt.okx.common.enums.TrendType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhouzhitong
 * @since 2024/12/16
 **/
@Data
@NoArgsConstructor
public class InstInfoDTO {

    /**
     * 产品ID
     */
    private String instId;

    /**
     * 是否可以下单
     */
    private boolean enablePlaceOrder;

    /**
     * 整体趋势
     */
    private TrendType trend;
    public String getTrend() {
        if (trend != null) {
            return trend.getDesc();
        }
        return null;
    }

}
