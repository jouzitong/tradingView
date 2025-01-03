//package ai.zzt.okx.calculate.indicator.demo;
//
//import ai.zzt.okx.calculate.indicator.BaseIndicator;
//import ai.zzt.okx.calculate.indicator.val.KDJVal;
//import ai.zzt.okx.calculate.indicator.vo.Mp;
//import ai.zzt.okx.common.utils.SlideList;
//import ai.zzt.okx.v5.enumeration.Bar;
//import org.jetbrains.annotations.Nullable;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.ArrayList;
//import java.util.Deque;
//import java.util.LinkedList;
//import java.util.List;
//
///**
// * @author zhouzhitong
// * @since 2024/11/30
// **/
//public class KDJV2 extends BaseIndicator<KDJVal> {
//
//    private final SlideList<BigDecimal> highs;
//
//    private final SlideList<BigDecimal> lows;
//
//    protected Deque<Mp> maxDeque = new LinkedList<>();
//
//    protected Deque<Mp> minDeque = new LinkedList<>();
//
//    private final SlideList<KDJVal> vals;
//
//    private BigDecimal prevK = BigDecimal.valueOf(50.0);
//
//    private BigDecimal prevD = BigDecimal.valueOf(50.0);
//
//    /**
//     * 平滑系数
//     */
//    private final BigDecimal alpha;
//
//    public KDJV2(int period, Bar bar, BigDecimal alpha) {
//        super(period, bar);
//        this.highs = new SlideList<>(period);
//        this.lows = new SlideList<>(period);
//        this.vals = new SlideList<>(period);
//        this.alpha = alpha;
//    }
//
//    public List<KDJSignal> calculateWithSignals(List<BigDecimal> highs, List<BigDecimal> lows, List<BigDecimal> closes) {
//        List<KDJVal> kdjResults = calculate(highs, lows, closes);
//        List<KDJSignal> signals = new ArrayList<>();
//
//        for (int i = 1; i < kdjResults.size(); i++) {
//            KDJVal prev = kdjResults.get(i - 1);
//            KDJVal current = kdjResults.get(i);
//
//            String signal = "Hold"; // 默认无信号
//            if (current.k.compareTo(current.d) > 0 && prev.k.compareTo(prev.d) <= 0 && current.k.compareTo(BigDecimal.valueOf(20)) < 0) {
//                signal = "Buy"; // 黄金交叉且在超卖区
//            } else if (current.k.compareTo(current.d) < 0 && prev.k.compareTo(prev.d) >= 0 && current.k.compareTo(BigDecimal.valueOf(80)) > 0) {
//                signal = "Sell"; // 死亡交叉且在超买区
//            }
//
//            signals.add(new KDJSignal(signal, current));
//        }
//
//        return signals;
//    }
//
//    public List<KDJVal> calculate(List<BigDecimal> highs, List<BigDecimal> lows, List<BigDecimal> closes) {
//        List<KDJVal> results = new ArrayList<>();
//        for (int i = 0; i < closes.size(); i++) {
//            if (i < period - 1) {
//                results.add(new KDJVal(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
//                continue;
//            }
//
//            // 计算 Hn 和 Ln
//            BigDecimal hn = getHighest(highs, i - (period - 1), i);
//            BigDecimal ln = getLowest(lows, i - (period - 1), i);
//
//            // 计算 RSV
//            BigDecimal rsv = hn.subtract(ln).compareTo(BigDecimal.ZERO) == 0
//                    ? BigDecimal.ZERO
//                    : closes.get(i).subtract(ln).divide(hn.subtract(ln), 8, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
//
//            // 计算 K, D, J
//            BigDecimal k = alpha.multiply(rsv).add(BigDecimal.ONE.subtract(alpha).multiply(prevK));
//            BigDecimal d = alpha.multiply(k).add(BigDecimal.ONE.subtract(alpha).multiply(prevD));
//            BigDecimal j = k.multiply(BigDecimal.valueOf(3)).subtract(d.multiply(BigDecimal.valueOf(2)));
//
//            results.add(new KDJVal(k, d, j));
//
//            prevK = k;
//            prevD = d;
//        }
//
//        return results;
//    }
//
//    private BigDecimal getHighest(SlideList<BigDecimal> list, int start, int end) {
//        BigDecimal highest = BigDecimal.valueOf(Double.MIN_VALUE);
//        for (int i = start; i <= end; i++) {
//            highest = highest.max(list.get(i));
//        }
//        return highest;
//    }
//
//    private BigDecimal getLowest(SlideList<BigDecimal> list, int start, int end) {
//        BigDecimal lowest = BigDecimal.valueOf(Double.MAX_VALUE);
//        for (int i = start; i <= end; i++) {
//            lowest = lowest.min(list.get(i));
//        }
//        return lowest;
//    }
//
//    @Nullable
//    @Override
//    protected KDJVal doCalculate() {
//        BigDecimal last = prices.getLast();
//        long t = lastTime - (period + 1) * bar.toMillis();
//        // 淘汰过期的数据
//        if (!maxDeque.isEmpty() && maxDeque.peekFirst().ts < t) {
//            maxDeque.pollFirst();
//        }
//        if (!minDeque.isEmpty() && minDeque.peekFirst().ts < t) {
//            minDeque.pollFirst();
//        }
//        // 计算最新数据
//        Mp mp = new Mp();
//        mp.ts = t;
//        mp.val = last;
//
//        while (!maxDeque.isEmpty() && maxDeque.peekLast().val.compareTo(last) < 0) {
//            maxDeque.pollLast();
//        }
//        while (!minDeque.isEmpty() && minDeque.peekLast().val.compareTo(last) > 0) {
//            minDeque.pollLast();
//        }
//        maxDeque.offerLast(mp);
//        minDeque.offerLast(mp);
//
//
//        if (highs.getLast().compareTo(last) < 0) {
//            highs.setLast(last);
//        }
//        if (lows.getLast().compareTo(last) > 0) {
//            lows.setLast(last);
//        }
//        KDJVal val = new KDJVal();
//        if (vals.isEmpty()) {
//            vals.add(new KDJVal(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
//            return vals.getLast();
//        }
//        int i = prices.size() - 1;
//// 计算 Hn 和 Ln
//        BigDecimal hn = getHighest(highs, i - period + 1, i);
//        BigDecimal ln = getLowest(lows, i - period + 1, i);
//        if (update) {
//            vals.setLast(val);
//        } else {
//            vals.add(val);
//        }
//        return vals.getLast();
//    }
//
//    public static class KDJSignal {
//        public final String signal; // 买入或卖出信号
//        public final KDJVal kdj;
//
//        public KDJSignal(String signal, KDJVal kdj) {
//            this.signal = signal;
//            this.kdj = kdj;
//        }
//
//        @Override
//        public String toString() {
//            return String.format("Signal: %s, %s", signal, kdj);
//        }
//    }
//
//    @Override
//    protected void addBefore(BigDecimal price, long ts) {
//        super.addBefore(price, ts);
//        this.highs.add(price);
//        this.lows.add(price);
//    }
//
//    @Override
//    protected void clear() {
//        this.highs.clear();
//        this.lows.clear();
//        this.vals.clear();
//        this.prevD = BigDecimal.valueOf(50);
//        this.prevK = BigDecimal.valueOf(50);
//    }
//
//    /**
//     * 获取最后一个数据
//     *
//     * @return 最后一个数据
//     */
//    @Override
//    public KDJVal get() {
//        return vals.getLast();
//    }
//
//    /**
//     * 获取倒数第二个数据
//     *
//     * @return 倒数第二个数据
//     */
//    public KDJVal getSecLast() {
//        return vals.getSecLast();
//    }
//
//}
