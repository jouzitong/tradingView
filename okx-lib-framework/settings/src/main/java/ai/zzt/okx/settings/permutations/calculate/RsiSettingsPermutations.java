package ai.zzt.okx.settings.permutations.calculate;

import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.settings.calculate.RsiSettings;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 排列组合: rsi 算法配置
 *
 * @author zhouzhitong
 * @since 2024/7/1
 **/
@Data
public class RsiSettingsPermutations extends BaseCalculateSettingsPermutations<RsiSettings> {

    /**
     * 最小连续次数
     */
    private List<Integer> periods = Arrays.asList(12, 16, 24);

    /**
     * 末位连续次数
     */
    private List<BigDecimal> overboughtList = Arrays.asList(BigDecimal.valueOf(70), BigDecimal.valueOf(75), BigDecimal.valueOf(80));

    private List<BigDecimal> oversoldList = Arrays.asList(BigDecimal.valueOf(30), BigDecimal.valueOf(25), BigDecimal.valueOf(20));

    @Override
    public long size() {
        return (long) periods.size() * overboughtList.size() * oversoldList.size();
    }

    @Override
    public List<RsiSettings> toSettings(String instId) {
        List<RsiSettings> res = new LinkedList<>();
        for (Integer period : periods) {
            for (BigDecimal os : oversoldList) {
                for (BigDecimal ob : overboughtList) {
                    for (Integer weight : getWeights()) {
                        RsiSettings rsiSettings = new RsiSettings(instId, null);
                        rsiSettings.setPeriod(period);
                        rsiSettings.setOversold(os);
                        rsiSettings.setOverbought(ob);
                        rsiSettings.setWeights(weight);
                        res.add(rsiSettings);
                    }
                }
            }
        }
        return res;
    }

    @Override
    public IndicatorType type() {
        return IndicatorType.RSI;
    }
}
