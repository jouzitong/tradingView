package ai.zzt.okx.settings.permutations.order;

import ai.zzt.okx.settings.order.profit.GreedyOcoSettings;
import ai.zzt.okx.settings.order.profit.ProfitSettings;
import ai.zzt.okx.settings.permutations.Permutations;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/10/2
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfitSettingsPermutations implements Permutations<ProfitSettings> {

    /**
     * 预计收益率(不包括杠杆). 默认收益率为 2.3%
     * <p>
     * 描述: 预计收益指的是, 开仓到平仓的纯利润(手续费已经计算在内了, 实际收益会大于这个值, 用于抵消手续费), 但不包括杠杆.
     */
    @Deprecated
    private List<BigDecimal> profitRates = Arrays.asList(BigDecimal.valueOf(0.01), BigDecimal.valueOf(0.023));

    /**
     * 最大收益率(不包括杠杆). 默认收益率为 50%
     * <p>
     * 描述: 当达到 "最大收益率"时, 就会立即平仓.
     */
    private List<BigDecimal> maxProfitRates = Arrays.asList(BigDecimal.valueOf(0.5));

    /**
     * [贪心]止盈止损配置
     */
    private GreedyOcoSettingsPermutations greedyOcos = new GreedyOcoSettingsPermutations();

    /**
     * 预期最小收益率(不包括杠杆). 默认收益率为 0.3%
     * <p>
     * 描述:
     */
    private List<BigDecimal> minProfitRates = Arrays.asList(BigDecimal.valueOf(0.003));

    @Override
    public long size() {
        return (long) profitRates.size() * maxProfitRates.size() * greedyOcos.size() * minProfitRates.size();
    }

    @Override
    public List<ProfitSettings> toSettings(String instId) {
        List<ProfitSettings> res = new ArrayList<>();
        for (BigDecimal profitRate : profitRates) {
            for (BigDecimal maxProfitRate : maxProfitRates) {
                for (GreedyOcoSettings greedyOco : greedyOcos.toSettings(instId)) {
                    for (BigDecimal minProfitRate : minProfitRates) {
                        ProfitSettings settings = new ProfitSettings();
//                        settings.setProfitRate(profitRate);
                        settings.setMaxProfitRate(maxProfitRate);
                        settings.setGreedyOco(greedyOco);
                        settings.setMinProfitRate(minProfitRate);
                        res.add(settings);
                    }
                }
            }
        }
        return res;
    }

    @Data
    public static class GreedyOcoSettingsPermutations implements Permutations<GreedyOcoSettings> {

        /**
         * (贪心)止盈止损收益率(不包括杠杆). 默认收益率为 0.005%
         * <p>
         * 描述: 当收益率达到预期收益{@link ProfitSettings#getProfitRate() ProfitRate}时, 会进行止盈止损,
         * 止盈止损的收益率是[当前收益率 - (贪心)止盈止损收益率]（并切保证收益率大于最小收益率）
         */
        private List<BigDecimal> greedyOcoRates = Arrays.asList(BigDecimal.valueOf(0.005));

        /**
         * (贪心)止盈止损收益率(不包括杠杆) 触发条件值. 默认 0.002%
         * <p>
         * 描述: 在第一次触发(贪心)止盈止损之后, 每当收益率涨幅大于【此值】时, 就会重新触发(贪心)止盈止损. 不会出现上次比这次收益小的情况
         */
        private List<BigDecimal> greedyOcoRateTriggers = Arrays.asList(BigDecimal.valueOf(0.002));

        /**
         * 最多触发次数. 默认 4 次.
         * <p>
         * 描述: 触发(贪心)止盈止损的次数, 超过此次数后, 则不会再触发. 注意第一次触发不算在这之内
         */
        private List<Integer> maxTriggerCount = Arrays.asList(4);

        @Override
        public long size() {
            return 1 + (long) greedyOcoRates.size() * greedyOcoRateTriggers.size() * maxTriggerCount.size();
        }

        @Override
        public List<GreedyOcoSettings> toSettings(String instId) {
            List<GreedyOcoSettings> res = new ArrayList<>();

            // 默认添加一个不支持的配置
            GreedyOcoSettings settings = new GreedyOcoSettings();
            settings.setEnable(false);
            res.add(settings);

            for (BigDecimal greedyOcoRate : greedyOcoRates) {
                for (BigDecimal greedyOcoRateTrigger : greedyOcoRateTriggers) {
                    for (Integer count : maxTriggerCount) {
                        settings = new GreedyOcoSettings();
                        settings.setEnable(true);
                        settings.setGreedyOcoRate(greedyOcoRate);
                        settings.setGreedyOcoRateTrigger(greedyOcoRateTrigger);
                        settings.setMaxTriggerCount(count);
                        res.add(settings);
                    }
                }
            }
            return res;
        }
    }

}
