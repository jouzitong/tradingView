package ai.zzt.okx.calculate.indicator.demo;

import java.util.ArrayList;
import java.util.List;

public class RSIIndicator1 {

    /**
     * 计算 RSI 指标
     *
     * @param prices 价格列表（按时间顺序排列）
     * @param period RSI 计算周期（例如 14）
     * @return RSI 值列表
     */
    public static List<Double> calculateRSI(List<Double> prices, int period) {
        // 验证输入
        if (prices == null || prices.size() < period + 1) {
            throw new IllegalArgumentException("价格数据不足以计算 RSI");
        }

        List<Double> rsiList = new ArrayList<>();
        double gainSum = 0.0;
        double lossSum = 0.0;

        // 第一次计算平均涨跌幅
        for (int i = 1; i <= period; i++) {
            double change = prices.get(i) - prices.get(i - 1);
            if (change > 0) {
                gainSum += change;
            } else {
                lossSum -= change; // 取正值
            }
        }

        double avgGain = gainSum / period;
        double avgLoss = lossSum / period;
        rsiList.add(calculateRSIValue(avgGain, avgLoss));

        // 逐步计算后续 RSI
        for (int i = period + 1; i < prices.size(); i++) {
            double change = prices.get(i) - prices.get(i - 1);
            double gain = Math.max(change, 0); // 涨幅
            double loss = Math.max(-change, 0); // 跌幅

            // 平滑计算平均涨跌幅
            avgGain = (avgGain * (period - 1) + gain) / period;
            avgLoss = (avgLoss * (period - 1) + loss) / period;

            // 计算当前 RSI
            rsiList.add(calculateRSIValue(avgGain, avgLoss));
        }

        return rsiList;
    }

    /**
     * 根据平均涨跌幅计算 RSI 值
     *
     * @param avgGain 平均涨幅
     * @param avgLoss 平均跌幅
     * @return RSI 值
     */
    private static double calculateRSIValue(double avgGain, double avgLoss) {
        if (avgLoss == 0) {
            return 100.0; // 如果没有跌幅，RSI 为 100
        }
        double rs = avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }

    public static void main(String[] args) {
        long s = System.currentTimeMillis();
        for (int j = 0; j < 10_0000; j++) {
            // 示例价格数据
            List<Double> prices = List.of(
                    44.34, 44.09, 44.15, 43.61, 44.33,
                    44.83, 45.10, 45.42, 45.84, 46.08,
                    45.89, 46.03, 45.61, 46.28, 46.28,
                    46.00, 46.03, 46.41, 46.22, 45.64
            );

            int period = 14; // RSI 周期
            List<Double> rsiValues = calculateRSI(prices, period);

            // 打印 RSI 值
//            System.out.println("RSI 指标值：");
//            for (int i = 0; i < rsiValues.size(); i++) {
//                rsiValues.get(i);
//            }
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - s) + "ms");
    }
}