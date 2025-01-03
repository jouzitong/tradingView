package ai.zzt.okx.trading.dto;

import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.common.enums.TrendType;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.Data;

import java.util.Map;

/**
 * @author zhouzhitong
 * @since 2024/12/18
 **/
@Data
public class InstDetailDTO {

    /**
     * 产品ID
     */
    private String instId;

    /**
     * 整体趋势
     */
    private TrendType trend;

    /**
     * 每个技术指标的趋势
     */
    private Map<Bar, Map<IndicatorType, TrendType>> indicatorTrendMap;


}
