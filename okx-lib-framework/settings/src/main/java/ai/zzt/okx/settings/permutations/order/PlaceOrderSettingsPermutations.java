package ai.zzt.okx.settings.permutations.order;

import ai.zzt.okx.settings.order.PlaceOrderSettings;
import ai.zzt.okx.settings.order.profit.ProfitSettings;
import ai.zzt.okx.settings.permutations.Permutations;
import lombok.Data;
import ai.zzt.okx.v5.enumeration.OrderType;
import ai.zzt.okx.v5.enumeration.PositionsSide;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 下单配置的排列组合
 *
 * @author zhouzhitong
 * @since 2024/7/1
 **/
@Data
public class PlaceOrderSettingsPermutations implements Permutations<PlaceOrderSettings> {

    /**
     * 开仓订单类型
     */
    private List<OrderType> openOrderTypes = Arrays.asList(OrderType.MARKET);

    /**
     * 平仓订单类型
     */
    private List<OrderType> closeOrderTypes = Arrays.asList(OrderType.MARKET);

    /**
     * TODO 回撤率(百分比)
     */
    private List<BigDecimal> retraceRates = Arrays.asList(BigDecimal.valueOf(0.5));

    /**
     * 杠杆倍数
     */
    private List<BigDecimal> levers = Arrays.asList(BigDecimal.ONE);

    /**
     * 下单限制
     */
    private PlaceOrderLimitPermutations limits = new PlaceOrderLimitPermutations();

    /**
     * 利润配置
     */
    private ProfitSettingsPermutations profits = new ProfitSettingsPermutations();

    /**
     * 止损率(包括杠杠倍数)
     */
    private List<BigDecimal> stopLossRates = Arrays.asList(BigDecimal.ZERO, BigDecimal.valueOf(-0.2),
            BigDecimal.valueOf(-0.3));

    @Override
    public long size() {
        long size = 1L;
        size *= openOrderTypes.size();
        size *= closeOrderTypes.size();
        size *= retraceRates.size();
        size *= levers.size();
        size *= stopLossRates.size();
        return size;
    }

    @Override
    public List<PlaceOrderSettings> toSettings(String instId) {
        List<PlaceOrderSettings> res = new ArrayList<>();
        List<PlaceOrderSettings.PlaceOrderLimit> limits = this.limits.toSettings(instId);
        List<ProfitSettings> profits = this.profits.toSettings(instId);
        for (OrderType openOrderType : openOrderTypes) {
            for (OrderType closeOrderType : closeOrderTypes) {
                for (BigDecimal retraceRate : retraceRates) {
                    for (BigDecimal lever : levers) {
                        for (PlaceOrderSettings.PlaceOrderLimit limit : limits) {
                            for (ProfitSettings profitSettings : profits) {
                                for (BigDecimal stopLossRate : stopLossRates) {
                                    PlaceOrderSettings settings = new PlaceOrderSettings(instId);
                                    settings.setOpenOrderType(openOrderType);
                                    settings.setCloseOrderType(closeOrderType);
                                    settings.setRetraceRate(retraceRate);
                                    settings.setLever(lever);
                                    settings.setLimit(limit);
                                    settings.setProfit(profitSettings);
                                    settings.setStopLossRate(stopLossRate);
                                    res.add(settings);
                                }
                            }

                        }

                    }
                }
            }
        }
        return res;
    }

    @Data
    public static class PlaceOrderLimitPermutations implements Permutations<PlaceOrderSettings.PlaceOrderLimit> {
        /**
         * 下单最大数量. 默认 1
         */
        private List<Integer> maxCounts = Arrays.asList(1, 2);

        /**
         * 多次开仓时, 最少间隔时间, 单位 毫秒(ms). 默认 120分钟(m)
         */
        private List<Long> minIntervalTimes = Arrays.asList(7L * 24 * 60 * 60 * 1000);

        /**
         * 支持的购买方向. 默认支持买空和买多
         */
        private Set<PositionsSide> sides = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(PositionsSide.LONG, PositionsSide.SHORT)));

        /**
         * 下单冷静期. 单位: 毫秒(ms). 默认 无冷静期
         * <p>
         * 描述: 就是在系统认定趋势后, 支持持续了这么久, 然后再下单, 防止系统误判.
         */
        private List<Long> coolingOffTimes = Arrays.asList(0L);

        /**
         * 开仓时间间隔。 单位: 毫秒(ms)
         */
        private List<Long> openIntervalTimes = Arrays.asList(60L * 60 * 1000, 24L * 60 * 60 * 1000);

        /**
         * TODO 开多仓最高价
         */
        private List<BigDecimal> maxPrices = Arrays.asList();

        /**
         * TODO 开空仓最低价
         */
        private List<BigDecimal> minPrices = Arrays.asList();

        @Override
        public long size() {
            return (long) maxCounts.size() * minIntervalTimes.size() * coolingOffTimes.size()
                    * openIntervalTimes.size() ;
        }

        @Override
        public List<PlaceOrderSettings.PlaceOrderLimit> toSettings(String instId) {
            List<PlaceOrderSettings.PlaceOrderLimit> res = new ArrayList<>();
            for (Integer maxCount : maxCounts) {
                for (Long minIntervalTime : minIntervalTimes) {
                    for (Long coolingOffTime : coolingOffTimes) {
                        for (Long openIntervalTime : openIntervalTimes) {
//                            for (BigDecimal maxPrice : maxPrices) {
//                                for (BigDecimal minPrice : minPrices) {
                                    PlaceOrderSettings.PlaceOrderLimit limit = new PlaceOrderSettings.PlaceOrderLimit();
                                    limit.setMaxCount(maxCount);
                                    limit.setMinIntervalTime(minIntervalTime);
                                    limit.setCoolingOffTime(coolingOffTime);
                                    limit.setOpenIntervalTime(openIntervalTime);
//                                    limit.setMaxPrice(maxPrice);
//                                    limit.setMinPrice(minPrice);
                                    limit.setSides(sides);
                                    res.add(limit);
//                                }
//                            }
                        }
                    }
                }
            }
            return res;
        }
    }

}
