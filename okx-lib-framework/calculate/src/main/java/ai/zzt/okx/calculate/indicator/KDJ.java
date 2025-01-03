package ai.zzt.okx.calculate.indicator;

import ai.zzt.okx.calculate.indicator.val.KDJVal;
import ai.zzt.okx.common.utils.SlideList;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * KDJ
 *
 * @author zhouzhitong
 * @since 2024/11/30
 **/
// TODO 待优化. 重复计算太多
public class KDJ extends BaseIndicator<KDJVal> {

    private final SlideList<BigDecimal> highs;

    private final SlideList<BigDecimal> lows;

    @Getter
    private SlideList<KDJVal> vals;

    /**
     * 平滑系数
     */
    private final BigDecimal alpha;

    public KDJ(int period, Bar bar, BigDecimal alpha) {
        super(period, bar);
//        period = 100;
        this.highs = new SlideList<>(period);
        this.lows = new SlideList<>(period);
        this.vals = new SlideList<>(period);
        this.alpha = alpha;
    }

    @Nullable
    @Override
    protected KDJVal doCalculate() {
        BigDecimal last = prices.getLast();

        if (highs.getLast().compareTo(last) < 0) {
            highs.setLast(last);
        }
        if (lows.getLast().compareTo(last) > 0) {
            lows.setLast(last);
        }
        this.vals = calculate(highs, lows, prices);
        return vals.getLast();
    }

    public static class KDJSignal {
        public final String signal; // 买入或卖出信号
        public final KDJVal kdj;

        public KDJSignal(String signal, KDJVal kdj) {
            this.signal = signal;
            this.kdj = kdj;
        }

        @Override
        public String toString() {
            return String.format("Signal: %s, %s", signal, kdj);
        }
    }

    @Override
    protected void addBefore(BigDecimal price, long ts) {
        super.addBefore(price, ts);
        this.highs.add(price);
        this.lows.add(price);
    }

    @Override
    protected void clear() {
        this.highs.clear();
        this.lows.clear();
        this.vals.clear();
    }

    /**
     * 获取最后一个数据
     *
     * @return 最后一个数据
     */
    @Override
    public KDJVal get() {
        return vals.getLast();
    }

    /**
     * 获取倒数第二个数据
     *
     * @return 倒数第二个数据
     */
    public KDJVal getSecLast() {
        return vals.getSecLast();
    }

    public SlideList<KDJVal> calculate(SlideList<BigDecimal> highs, SlideList<BigDecimal> lows, SlideList<BigDecimal> closes) {
        SlideList<KDJVal> results = new SlideList<>(closes.size());
        BigDecimal prevK = BigDecimal.valueOf(50.0);
        BigDecimal prevD = BigDecimal.valueOf(50.0);
        for (int i = 0; i < closes.size(); i++) {
            if (i < period - 1) {
                results.add(new KDJVal(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
                continue;
            }

            // 计算 Hn 和 Ln
            BigDecimal hn = getHighest(highs, i - (period - 1), i);
            BigDecimal ln = getLowest(lows, i - (period - 1), i);

            // 计算 RSV
            BigDecimal rsv = hn.subtract(ln).compareTo(BigDecimal.ZERO) == 0
                    ? BigDecimal.ZERO
                    : closes.get(i).subtract(ln).divide(hn.subtract(ln), 8, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

            // 计算 K, D, J
            BigDecimal k = alpha.multiply(rsv).add(BigDecimal.ONE.subtract(alpha).multiply(prevK));
            BigDecimal d = alpha.multiply(k).add(BigDecimal.ONE.subtract(alpha).multiply(prevD));
            BigDecimal j = k.multiply(BigDecimal.valueOf(3)).subtract(d.multiply(BigDecimal.valueOf(2)));

            results.add(new KDJVal(k, d, j));

            prevK = k;
            prevD = d;
        }

        return results;
    }


    private BigDecimal getHighest(SlideList<BigDecimal> list, int start, int end) {
        BigDecimal highest = BigDecimal.valueOf(Double.MIN_VALUE);
        for (int i = start; i <= end; i++) {
            highest = highest.max(list.get(i));
        }
        return highest;
    }

    private BigDecimal getLowest(SlideList<BigDecimal> list, int start, int end) {
        BigDecimal lowest = BigDecimal.valueOf(Double.MAX_VALUE);
        for (int i = start; i <= end; i++) {
            lowest = lowest.min(list.get(i));
        }
        return lowest;
    }


}
