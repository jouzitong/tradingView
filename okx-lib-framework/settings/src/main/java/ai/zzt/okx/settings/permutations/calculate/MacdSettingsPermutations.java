package ai.zzt.okx.settings.permutations.calculate;

import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.common.enums.MacdTrendType;
import ai.zzt.okx.common.enums.TrendType;
import ai.zzt.okx.settings.calculate.MacdSettings;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 排列组合: macd算法配置
 *
 * @author zhouzhitong
 * @since 2024/7/1
 **/
@Data
public class MacdSettingsPermutations extends BaseCalculateSettingsPermutations<MacdSettings> {

    private List<Boolean> goldenCrossLines = Arrays.asList(true, false);

    /**
     * TODO 最大连续次数
     */
    private List<Integer> maxContinuityCounts = Arrays.asList(4);

    /**
     * 最小连续次数
     */
    private List<Integer> minContinuityCounts = Arrays.asList(1, 2, 3);

    /**
     * 末位连续次数
     */
    private List<Integer> lastPeriods = Arrays.asList(2, 3);

    @Override
    public long size() {
        return (long) minContinuityCounts.size() * lastPeriods.size() * maxContinuityCounts.size() + 1;
    }

    @Override
    public List<MacdSettings> toSettings(String instId) {
        List<MacdSettings> res = new LinkedList<>();
        for (Integer weight : getWeights()) {
            for (Boolean goldenCrossLine : goldenCrossLines) {
                if (goldenCrossLine) {
                    MacdSettings macdSettings = new MacdSettings(instId, null);
                    macdSettings.setWeights(weight);
                    macdSettings.setGoldenCrossLine(true);
                    Map<MacdTrendType, TrendType> macdTrendTypeMap = new HashMap<>();
                    macdTrendTypeMap.put(MacdTrendType.NONE, TrendType.FLUCTUATE);
                    macdTrendTypeMap.put(MacdTrendType.PLUS_UP, TrendType.UP);
                    macdTrendTypeMap.put(MacdTrendType.BURDEN_UP, TrendType.UP);
                    macdTrendTypeMap.put(MacdTrendType.BURDEN_DOWN, TrendType.DOWN);
                    macdTrendTypeMap.put(MacdTrendType.PLUS_DOWN, TrendType.DOWN);
                    macdSettings.setMacdTrendTypeMap(macdTrendTypeMap);
                    res.add(macdSettings);
                    continue;
                }
                for (Integer minContinuityCount : minContinuityCounts) {
                    for (Integer lastPositionCount : lastPeriods) {
                        for (Integer maxContinuityCount : maxContinuityCounts) {
                            MacdSettings macdSettings = new MacdSettings(instId, null);
                            macdSettings.setWeights(weight);
                            macdSettings.setMinContinuityCount(minContinuityCount);
                            macdSettings.setLastPeriod(lastPositionCount);
                            macdSettings.setGoldenCrossLine(goldenCrossLine);
                            macdSettings.setMaxContinuityCount(maxContinuityCount);
                            res.add(macdSettings);
                        }

                    }
                }
            }
        }
        return res;
    }


    @Override
    public IndicatorType type() {
        return IndicatorType.MACD;
    }
}
