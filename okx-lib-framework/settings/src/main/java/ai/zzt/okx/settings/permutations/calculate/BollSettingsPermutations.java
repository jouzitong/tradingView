package ai.zzt.okx.settings.permutations.calculate;

import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.settings.calculate.BollSettings;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 排列组合: boll 算法配置
 *
 * @author zhouzhitong
 * @since 2024/7/1
 **/
@Data
public class BollSettingsPermutations extends BaseCalculateSettingsPermutations<BollSettings> {

    /**
     * 最小连续次数
     */
    private List<Integer> periods = Arrays.asList(12, 16, 24);

    /**
     * 末位连续次数
     */
    private List<Integer> diffMinCountList = Arrays.asList(1, 3, 5);

    private List<BigDecimal> minDiffRateList = Arrays.asList(BigDecimal.valueOf(0.023), BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.1));

    private List<BigDecimal> retraceRateList = Arrays.asList(BigDecimal.valueOf(30), BigDecimal.valueOf(25), BigDecimal.valueOf(20));

    @Override
    public long size() {
        return (long) periods.size() * diffMinCountList.size() * diffMinCountList.size() * retraceRateList.size();
    }

    @Override
    public List<BollSettings> toSettings(String instId) {
        List<BollSettings> res = new LinkedList<>();
        for (Integer period : periods) {
            for (Integer count : diffMinCountList) {
                for (BigDecimal mdr : minDiffRateList) {
                    for (BigDecimal rr : retraceRateList) {
                        for (Integer weight : getWeights()) {
                            BollSettings rsiSettings = new BollSettings(instId, null);
                            rsiSettings.setWeights(weight);
                            rsiSettings.setPeriod(period);
                            rsiSettings.setDiffMinCount(count);
                            rsiSettings.setMinDiffRate(mdr);
                            rsiSettings.setRetraceRate(rr);
                            res.add(rsiSettings);
                        }
                    }
                }
            }
        }
        return res;
    }

    @Override
    public IndicatorType type() {
        return IndicatorType.BOLL;
    }
}
