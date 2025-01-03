package ai.zzt.okx.calculate.indicator;

import ai.zzt.okx.calculate.indicator.val.RSIVal;
import ai.zzt.okx.common.utils.K;
import ai.zzt.okx.common.utils.SlideList;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * RSI
 * <pre>
 *     RSI（Relative Strength Index）是一种用于分析股票价格走势的技术指标，属于超买超卖类指标。
 *     RSI指标是通过比较一段时间内的平均收盘涨幅和平均收盘跌幅的大小，来评估市场买卖力量的强弱，从而判断股票价格的走势。
 *     RSI指标的取值范围是0-100，通常情况下，RSI指标的取值在70以上时，说明市场处于超买状态，股票价格可能会下跌；
 *     RSI指标的取值在30以下时，说明市场处于超卖状态，股票价格可能会上涨。
 *     RSI指标的计算公式如下：
 *     RSI = 100 - 100 / (1 + RS)
 * </pre>
 *
 * @author zhouzhitong
 * @since 2024/4/30
 **/
@Slf4j
public class RSI extends BaseIndicator<BigDecimal> {

    private final BigDecimal OVER = BigDecimal.valueOf(100);
    private final BigDecimal LOSS = BigDecimal.ZERO;

    private final SlideList<RSIVal> vals;

    @Getter
    private K lastK;

    public RSI(int period, Bar bar) {
        super(period, bar);
        vals = new SlideList<>(period);
    }

    @Override
    protected BigDecimal doCalculate() {
        BigDecimal price = prices.getLast();
        long ts = this.ts;
        if (lastK == null) {
            lastK = new K(price, bar, ts);
        } else {
            if (!lastK.update(price, ts)) {
                lastK = new K(price, bar, ts);
            }
        }
        BigDecimal rsi = calculateRSI();
        if (update) {
            vals.getLast().setVal(rsi);
        } else {
            vals.add(new RSIVal(rsi));
        }
        return rsi;
    }

    @Override
    protected void clear() {
        lastK = null;
        vals.clear();
    }

    @Override
    public BigDecimal get() {
        return vals.getLast().getVal();
    }

    /**
     * 计算RSI
     *
     * @return RSI指标. 注意返回值为null时, 代表时间间隔不足, 无法计算
     */
    public BigDecimal calculateRSI() {
        if (prices.size() < period) {
            return null;
        }
        BigDecimal sumOfGains = BigDecimal.ZERO;
        BigDecimal sumOfLosses = BigDecimal.ZERO;
        for (int i = 1; i < period; i++) {
            BigDecimal priceDiff = prices.get(i).subtract(prices.get(i - 1));
            if (priceDiff.compareTo(BigDecimal.ZERO) >= 0) {
                sumOfGains = sumOfGains.add(priceDiff);
            } else {
                sumOfLosses = sumOfLosses.add(priceDiff.abs());
            }
        }
        // 计算平均上涨日和平均下跌日的价格变动总和
        BigDecimal avgGain = sumOfGains.divide(BigDecimal.valueOf(period), getScale(), RoundingMode.UP);
        BigDecimal avgLoss = sumOfLosses.divide(BigDecimal.valueOf(period), getScale(), RoundingMode.UP);
        if (avgLoss.compareTo(BigDecimal.ZERO) == 0 && avgGain.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(50);
        }
        // 没有下跌情况
        if (avgLoss.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(95);
        }
        // 没有上涨情况
        if (avgGain.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(5);
        }
        try {
            // 计算相对强度RS
            BigDecimal rs = avgGain.divide(avgLoss, getScale(), RoundingMode.UP);
            // 计算RSI = 100 - 100 / (1 + RS)
            return BigDecimal.valueOf(100)
                    .subtract(BigDecimal.valueOf(100).divide(BigDecimal.ONE.add(rs), 2, RoundingMode.UP));
        } catch (Exception e) {
            log.error("RSI计算异常 prices {}, period {}", prices, period, e);
        }
        return null;
    }

}
