package ai.zzt.okx.calculate.indicator.demo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class KDJCalculator2 {

    private final int period; // 周期
    private final BigDecimal alpha = BigDecimal.valueOf(1.0 / 3); // 平滑系数

    private BigDecimal prevK = BigDecimal.valueOf(50.0); // 上一周期的 K 值
    private BigDecimal prevD = BigDecimal.valueOf(50.0); // 上一周期的 D 值

    public KDJCalculator2(int period) {
        this.period = period;
    }

    public List<KDJSignal> calculateWithSignals(List<BigDecimal> highs, List<BigDecimal> lows, List<BigDecimal> closes) {
        List<KDJResult> kdjResults = calculate(highs, lows, closes);
        List<KDJSignal> signals = new ArrayList<>();

        for (int i = 1; i < kdjResults.size(); i++) {
            KDJResult prev = kdjResults.get(i - 1);
            KDJResult current = kdjResults.get(i);

            String signal = "Hold"; // 默认无信号
            if (current.k.compareTo(current.d) > 0 && prev.k.compareTo(prev.d) <= 0 && current.k.compareTo(BigDecimal.valueOf(20)) < 0) {
                signal = "Buy"; // 黄金交叉且在超卖区
            } else if (current.k.compareTo(current.d) < 0 && prev.k.compareTo(prev.d) >= 0 && current.k.compareTo(BigDecimal.valueOf(80)) > 0) {
                signal = "Sell"; // 死亡交叉且在超买区
            }

            signals.add(new KDJSignal(signal, current));
        }

        return signals;
    }

    public List<KDJResult> calculate(List<BigDecimal> highs, List<BigDecimal> lows, List<BigDecimal> closes) {
        List<KDJResult> results = new ArrayList<>();

        for (int i = 0; i < closes.size(); i++) {
            if (i < period - 1) {
                results.add(new KDJResult(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
                continue;
            }

            // 计算 Hn 和 Ln
            BigDecimal hn = getHighest(highs, i - period + 1, i);
            BigDecimal ln = getLowest(lows, i - period + 1, i);

            // 计算 RSV
            BigDecimal rsv = hn.subtract(ln).compareTo(BigDecimal.ZERO) == 0
                    ? BigDecimal.ZERO
                    : closes.get(i).subtract(ln).divide(hn.subtract(ln), 8, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

            // 计算 K, D, J
            BigDecimal k = alpha.multiply(rsv).add(BigDecimal.ONE.subtract(alpha).multiply(prevK));
            BigDecimal d = alpha.multiply(k).add(BigDecimal.ONE.subtract(alpha).multiply(prevD));
            BigDecimal j = k.multiply(BigDecimal.valueOf(3)).subtract(d.multiply(BigDecimal.valueOf(2)));

            results.add(new KDJResult(k, d, j));

            prevK = k;
            prevD = d;
        }

        return results;
    }

    private BigDecimal getHighest(List<BigDecimal> list, int start, int end) {
        BigDecimal highest = BigDecimal.valueOf(Double.MIN_VALUE);
        for (int i = start; i <= end; i++) {
            highest = highest.max(list.get(i));
        }
        return highest;
    }

    private BigDecimal getLowest(List<BigDecimal> list, int start, int end) {
        BigDecimal lowest = BigDecimal.valueOf(Double.MAX_VALUE);
        for (int i = start; i <= end; i++) {
            lowest = lowest.min(list.get(i));
        }
        return lowest;
    }

    public static class KDJResult {
        public final BigDecimal k;
        public final BigDecimal d;
        public final BigDecimal j;

        public KDJResult(BigDecimal k, BigDecimal d, BigDecimal j) {
            this.k = k.setScale(2, RoundingMode.HALF_UP);
            this.d = d.setScale(2, RoundingMode.HALF_UP);
            this.j = j.setScale(2, RoundingMode.HALF_UP);
        }

        @Override
        public String toString() {
            return String.format("K: %s, D: %s, J: %s", k, d, j);
        }
    }

    public static class KDJSignal {
        public final String signal; // 买入或卖出信号
        public final KDJResult kdj;

        public KDJSignal(String signal, KDJResult kdj) {
            this.signal = signal;
            this.kdj = kdj;
        }

        @Override
        public String toString() {
            return String.format("Signal: %s, %s", signal, kdj);
        }
    }

    public static void main(String[] args) {
        List<BigDecimal> highs = List.of(new BigDecimal("10.0"), new BigDecimal("11.0"), new BigDecimal("12.0"),
                new BigDecimal("13.0"), new BigDecimal("14.0"), new BigDecimal("15.0"), new BigDecimal("16.0"));
        List<BigDecimal> lows = List.of(new BigDecimal("8.0"), new BigDecimal("7.0"), new BigDecimal("6.0"),
                new BigDecimal("6.0"), new BigDecimal("7.0"), new BigDecimal("8.0"), new BigDecimal("9.0"));
        List<BigDecimal> closes = List.of(new BigDecimal("9.0"), new BigDecimal("8.0"), new BigDecimal("10.0"),
                new BigDecimal("12.0"), new BigDecimal("13.0"), new BigDecimal("14.0"), new BigDecimal("15.0"));

        KDJCalculator2 calculator = new KDJCalculator2(3);
        List<KDJSignal> signals = calculator.calculateWithSignals(highs, lows, closes);

        for (KDJSignal signal : signals) {
            System.out.println(signal);
        }
    }
}