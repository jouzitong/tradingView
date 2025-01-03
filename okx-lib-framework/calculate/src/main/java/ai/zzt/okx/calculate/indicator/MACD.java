package ai.zzt.okx.calculate.indicator;

import ai.zzt.okx.common.utils.SlideList;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <pre>
 *     MACD（移动平均收敛/散度，Moving Average Convergence Divergence）指标。MACD是一种广泛使用的技术分析指标，用于衡量股票或其他证券的动量变化，并识别买入或卖出的信号。MACD由三部分组成：MACD线（即差离值）、信号线和MACD柱状图。
 *
 * MACD的计算步骤：
 * 计算短期EMA（通常为12天）和长期EMA（通常为26天）：
 *
 * EMA（指数移动平均）计算公式：
 *       EMA(today) = (Price(today) * a) + (EMA(yesterday) * (1-a))
 *      a = 2/(n+1) ,n为周期长度。
 * 计算MACD线：
 *      MACD线 = 短期EMA - 长期EMA
 * 计算信号线（通常为9天的MACD线的EMA）：
 *      信号线 = MACD线的9天EMA
 *
 * 计算MACD柱状图：
 *      MACD柱状图 = MACD线 - 信号线
 *
 * 具体算法步骤：
 * 短期EMA（12天）：
 *
 * 计算前12天的简单移动平均值（SMA）作为第12天的初始EMA。
 * 从第13天开始，使用前一天的EMA和当前价格计算新的EMA。
 * 长期EMA（26天）：
 *
 * 计算前26天的SMA作为第26天的初始EMA。
 * 从第27天开始，使用前一天的EMA和当前价格计算新的EMA。
 * MACD线：
 *
 * 从第26天开始，MACD线 = 短期EMA - 长期EMA
 * 信号线（9天）：
 *
 * 从第26天开始，计算MACD线的9天EMA。
 * MACD柱状图：
 *
 * MACD柱状图 = MACD线 - 信号线
 *
 * </pre>
 *
 * @author zhouzhitong
 * @since 2024/5/4
 **/
@Slf4j
public class MACD extends BaseIndicator<SlideList<BigDecimal>> {
    private final int shortEMA; // 短期EMA的周期
    private final int longEMA; // 长期EMA的周期
    private final int signalDEA; // 信号线DEA的周期

    // 下面不需要 SlideList 结构, 可以使用 List
    private final SlideList<BigDecimal> shortEMAValues;

    private final SlideList<BigDecimal> longEMAValues;

    /**
     * DIF (差离值)
     * <p>
     * DIFF是快速移动平均线（EMA）与慢速移动平均线的差值。通常，快速EMA的周期为12天，慢速EMA的周期为26天。计算方法如下：
     * <p>
     * MACD = EMA(12) - EMA(26)
     */
    @Getter
    private final SlideList<BigDecimal> difLine;

    /**
     * 信号线, 也称为 DEA（差离平均值，也称为Signal Line或MACD线）
     * <pre>
     * DEA是DIFF的9天的EMA（通常是9天，但也可以根据需要调整）。计算公式如下：
     * DEA = (2/(9+1)) * (DIF(today) - DEA(yesterday)) + DEA(yesterday)
     * 这条线用于平滑DIFF线，提供一个更稳定的信号。
     * DEA(init) = SUM(DIF(1-9))/9
     * </pre>
     */
    @Getter
    private final SlideList<BigDecimal> dea;

    /**
     * 柱状图
     */
    @Getter
    private final SlideList<BigDecimal> stickLine;

    public MACD(int shortEMA, int longEMA, int signalDEA, Bar timeUnit) {
        super(longEMA, timeUnit);
        this.shortEMA = shortEMA;
        this.longEMA = longEMA;
        this.signalDEA = signalDEA;

        this.shortEMAValues = new SlideList<>(longEMA);
        this.difLine = new SlideList<>(longEMA);
        this.dea = new SlideList<>(longEMA);
        this.longEMAValues = new SlideList<>(longEMA);
        this.stickLine = new SlideList<>(longEMA);
    }

    @Override
    protected SlideList<BigDecimal> doCalculate() {
        calculateMACD();
        return stickLine;
    }

    @Override
    public SlideList<BigDecimal> get() {
        return stickLine;
    }

    @Override
    protected void clear() {
        shortEMAValues.clear();
        longEMAValues.clear();
        difLine.clear();
        dea.clear();
        stickLine.clear();
    }

    private void calculateMACD() {
//        if (prices.size() < longEMA) {
//            return;
//        }
        // 计算 EMA
        calculateEMA(shortEMA, shortEMAValues, prices);
        calculateEMA(longEMA, longEMAValues, prices);

        // 计算 DIF
        calculateDIF();
        // 计算 DEA (Signal Line)
        calculateEMA(signalDEA, dea, difLine);

        calculateStick();
    }

    private void calculateEMA(int period, SlideList<BigDecimal> emaValues, SlideList<BigDecimal> prices) {
        if (prices.size() == 1) {
            BigDecimal ema = calculateSMA(prices, period);
            if (emaValues.isEmpty()) {
                emaValues.add(ema);
            } else {
                emaValues.setLast(ema);
            }
            return;
        }
        // 最新价格
        BigDecimal price = prices.getLast();
        // 参数 n
        BigDecimal multiplier = BigDecimal.valueOf(2.0 / (period + 1));
        if (prices.count() > emaValues.count()) {
            // 计算新的EMA
            BigDecimal lastEma = emaValues.getLast();
            // 计算新的EMA= (Price(today) * a) + (EMA(yesterday) * (1-a)) 【a=2/(period+1)】
            BigDecimal curEMA = price.subtract(lastEma).multiply(multiplier).add(lastEma);
            emaValues.add(curEMA.setScale(getScale(), RoundingMode.HALF_UP));
        } else {
            // 计算新的EMA
            int emaSize = emaValues.size();
            BigDecimal lastEma = emaValues.get(emaSize - 2);
            BigDecimal curEMA = price.subtract(lastEma).multiply(multiplier).add(lastEma);
            emaValues.setLast(curEMA.setScale(getScale(), RoundingMode.HALF_UP));
        }
    }

    // 计算 柱状图
    private void calculateStick() {
        if (stickLine.count() < difLine.count()) {
            stickLine.add(difLine.getLast().subtract(dea.getLast()).multiply(BigDecimal.valueOf(2)));
        } else {
            stickLine.setLast(difLine.getLast().subtract(dea.getLast()).multiply(BigDecimal.valueOf(2)));
        }
    }

    private void calculateDIF() {
        // 计算MACD线
        if (difLine.count() < longEMAValues.count()) {
            difLine.add(shortEMAValues.getLast().subtract(longEMAValues.getLast()));
        } else {
            difLine.setLast(shortEMAValues.getLast().subtract(longEMAValues.getLast()));
        }
    }

    /**
     * 计算平均值
     *
     * @param prices 价格列表
     * @param period 周期
     * @return 平均值
     */
    private BigDecimal calculateSMA(SlideList<BigDecimal> prices, int period) {
        return prices.getFirst();
//        BigDecimal sum = BigDecimal.ZERO;
//        int s = prices.size() - period;
//        for (int i = s; i < period; i++) {
//            sum = sum.add(prices.get(i));
//        }
//        return sum.divide(BigDecimal.valueOf(period), 2, RoundingMode.HALF_UP);
    }
}

