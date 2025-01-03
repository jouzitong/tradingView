package ai.zzt.okx.calculate.indicator.demo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class OptimizedRSIBigDecimal {

    private final int period; // RSI 的周期
    private BigDecimal avgGain; // 平均涨幅
    private BigDecimal avgLoss; // 平均跌幅
    private BigDecimal lastPrice; // 上一个价格
    private int dataCount; // 记录输入的价格数量

    /**
     * 初始化 RSI 计算器
     *
     * @param period RSI 计算周期
     */
    public OptimizedRSIBigDecimal(int period) {
        this.period = period;
        this.avgGain = BigDecimal.ZERO;
        this.avgLoss = BigDecimal.ZERO;
        this.lastPrice = null;
        this.dataCount = 0;
    }

    /**
     * 输入新的价格并计算 RSI
     *
     * @param currentPrice 当前价格
     * @return 当前 RSI 值，如果数据不足则返回 null
     */
    public BigDecimal addPrice(BigDecimal currentPrice) {
        if (lastPrice == null) {
            // 初始化：第一个价格没有涨跌幅
            lastPrice = currentPrice;
            return null;
        }

        // 计算当前涨跌幅
        BigDecimal change = currentPrice.subtract(lastPrice);
        lastPrice = currentPrice; // 更新最后价格

        // 分别计算涨幅和跌幅
        BigDecimal gain = change.max(BigDecimal.ZERO);
        BigDecimal loss = change.negate().max(BigDecimal.ZERO);

        // 累计数据计数
        dataCount++;

        if (dataCount <= period) {
            // 前 N 个周期：初始化平均值（简单累加法）
            avgGain = avgGain.add(gain.divide(BigDecimal.valueOf(period), RoundingMode.HALF_UP));
            avgLoss = avgLoss.add(loss.divide(BigDecimal.valueOf(period), RoundingMode.HALF_UP));

            // 如果周期还没达到，无法计算 RSI
            if (dataCount < period) {
                return null;
            }
        } else {
            // 后续周期：使用平滑更新公式
            avgGain = avgGain.multiply(BigDecimal.valueOf(period - 1))
                    .add(gain)
                    .divide(BigDecimal.valueOf(period), RoundingMode.HALF_UP);
            avgLoss = avgLoss.multiply(BigDecimal.valueOf(period - 1))
                    .add(loss)
                    .divide(BigDecimal.valueOf(period), RoundingMode.HALF_UP);
        }

        // 计算 RSI
        return calculateRSI(avgGain, avgLoss);
    }

    /**
     * 根据平均涨跌幅计算 RSI
     *
     * @param avgGain 平均涨幅
     * @param avgLoss 平均跌幅
     * @return RSI 值
     */
    private BigDecimal calculateRSI(BigDecimal avgGain, BigDecimal avgLoss) {
        if (avgLoss.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(100); // 如果没有跌幅，RSI 为 100
        }
        BigDecimal rs = avgGain.divide(avgLoss, RoundingMode.HALF_UP);
        return BigDecimal.valueOf(100).subtract(
                BigDecimal.valueOf(100).divide(BigDecimal.ONE.add(rs), RoundingMode.HALF_UP)
        );
    }

    public static void main(String[] args) {
        OptimizedRSIBigDecimal rsiCalculator = new OptimizedRSIBigDecimal(14); // 使用 14 周期

        // 示例数据流
        BigDecimal[] prices = {
                new BigDecimal("44.34"), new BigDecimal("44.09"), new BigDecimal("44.15"),
                new BigDecimal("43.61"), new BigDecimal("44.33"), new BigDecimal("44.83"),
                new BigDecimal("45.10"), new BigDecimal("45.42"), new BigDecimal("45.84"),
                new BigDecimal("46.08"), new BigDecimal("45.89"), new BigDecimal("46.03"),
                new BigDecimal("45.61"), new BigDecimal("46.28"), new BigDecimal("46.28"),
                new BigDecimal("46.00"), new BigDecimal("46.03"), new BigDecimal("46.41"),
                new BigDecimal("46.22"), new BigDecimal("45.64")
        };

        System.out.println("逐条计算 RSI：");
        for (BigDecimal price : prices) {
            BigDecimal rsi = rsiCalculator.addPrice(price);
            if (rsi != null) {
                System.out.printf("当前价格: %s, RSI: %.2f%n", price.toPlainString(), rsi);
            } else {
                System.out.printf("当前价格: %s, 数据不足，无法计算 RSI%n", price.toPlainString());
            }
        }
    }
}