package ai.zzt.okx.calculate.indicator.demo;

public class OptimizedRSI {

    private final int period; // RSI 的周期
    private double avgGain; // 平均涨幅
    private double avgLoss; // 平均跌幅
    private Double lastPrice; // 上一个价格
    private int dataCount; // 记录输入的价格数量

    /**
     * 初始化 RSI 计算器
     *
     * @param period RSI 计算周期
     */
    public OptimizedRSI(int period) {
        this.period = period;
        this.avgGain = 0.0;
        this.avgLoss = 0.0;
        this.lastPrice = null; // 开始时没有历史价格
        this.dataCount = 0; // 数据计数初始化为 0
    }

    /**
     * 输入新的价格并计算 RSI
     *
     * @param currentPrice 当前价格
     * @return 当前 RSI 值，如果数据不足则返回 null
     */
    public Double addPrice(double currentPrice) {
        if (lastPrice == null) {
            // 初始化：第一个价格没有涨跌幅
            lastPrice = currentPrice;
            return null; // 返回 null 表示无法计算 RSI
        }

        // 计算当前涨跌幅
        double change = currentPrice - lastPrice;
        lastPrice = currentPrice; // 更新最后价格

        // 分别计算涨幅和跌幅
        double gain = Math.max(change, 0);
        double loss = Math.max(-change, 0);

        // 累计数据计数
        dataCount++;

        if (dataCount <= period) {
            // 前 N 个周期：初始化平均值（简单累加法）
            avgGain += gain / period;
            avgLoss += loss / period;

            // 如果周期还没达到，无法计算 RSI
            if (dataCount < period) {
                return null;
            }
        } else {
            // 后续周期：使用平滑更新公式
            avgGain = (avgGain * (period - 1) + gain) / period;
            avgLoss = (avgLoss * (period - 1) + loss) / period;
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
    private Double calculateRSI(double avgGain, double avgLoss) {
        if (avgLoss == 0) {
            return 100.0; // 如果没有跌幅，RSI 为 100
        }
        double rs = avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }

    public static void main(String[] args) {
        long s = System.currentTimeMillis();
        for (int i = 0; i < 10_0000; i++) {
            OptimizedRSI rsiCalculator = new OptimizedRSI(14); // 使用 14 周期

            // 示例数据流
            double[] prices = {44.34, 44.09, 44.15, 43.61, 44.33, 44.83, 45.10, 45.42,
                    45.84, 46.08, 45.89, 46.03, 45.61, 46.28, 46.28, 46.00, 46.03, 46.41, 46.22, 45.64};

//            System.out.println("逐条计算 RSI：");
            for (double price : prices) {
                Double rsi = rsiCalculator.addPrice(price);
//                if (rsi != null) {
//                    System.out.printf("当前价格: %.2f, RSI: %.2f%n", price, rsi);
//                } else {
//                    System.out.printf("当前价格: %.2f, 数据不足，无法计算 RSI%n", price);
//                }
            }
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - s) + "ms");
    }
}