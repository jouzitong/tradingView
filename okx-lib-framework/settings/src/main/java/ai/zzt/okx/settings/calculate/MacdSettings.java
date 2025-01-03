package ai.zzt.okx.settings.calculate;

import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.common.enums.MacdTrendType;
import ai.zzt.okx.common.enums.TrendType;
import ai.zzt.okx.v5.enumeration.Bar;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * MACD 算法配置. 周期暂时不动.
 *
 * @author zhouzhitong
 * @since 2024/6/1
 **/
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class MacdSettings extends BaseCalculateSettings {

    /**
     * 短周期. 默认 12
     */
    private int shortPeriod = 12;
    /**
     * 长周期. 默认 26
     */
    private int longPeriod = 26;

    /**
     * 信号周期. 默认 9
     */
    private int signalPeriod = 9;

    /**
     * 是否金叉线购买
     */
    private boolean goldenCrossLine = false;

    /**
     * 末位计算周期. 默认 5
     */
    private int lastPeriod = 5;

    /**
     * 最小连续次数. 默认 1
     */
    private int minContinuityCount = 1;

    /**
     * 最大连续次数. 默认 4
     */
    private int maxContinuityCount = 4;

    private Map<MacdTrendType, TrendType> macdTrendTypeMap = new HashMap<>();

    public MacdSettings(String id, InstrumentType instType) {
        super(id, instType);
    }

    public MacdSettings(String id, InstrumentType instType, Bar bar) {
        super(id, instType);
    }

    @Override
    public IndicatorType calculateType() {
        return IndicatorType.MACD;
    }

    public Map<MacdTrendType, TrendType> getMacdTrendTypeMap() {
        if (MapUtils.isEmpty(macdTrendTypeMap)) {
            initDefault();
        }
        return macdTrendTypeMap;
    }

    private void initDefault() {
        if (macdTrendTypeMap == null) {
            macdTrendTypeMap = new HashMap<>();
        }
        macdTrendTypeMap.put(MacdTrendType.NONE, TrendType.FLUCTUATE);
//        macdTrendTypeMap.put(MacdTrendType.PLUS_UP, TrendType.UP);
//        macdTrendTypeMap.put(MacdTrendType.BURDEN_UP, TrendType.UP);
        macdTrendTypeMap.put(MacdTrendType.PLUS_UP, TrendType.FLUCTUATE);
        macdTrendTypeMap.put(MacdTrendType.BURDEN_DOWN, TrendType.FLUCTUATE);
        macdTrendTypeMap.put(MacdTrendType.PLUS_DOWN, TrendType.DOWN);
        macdTrendTypeMap.put(MacdTrendType.BURDEN_UP, TrendType.UP);
    }

}
