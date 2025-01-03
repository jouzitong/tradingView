package ai.zzt.okx.calculate.indicator;

import ai.zzt.okx.calculate.indicator.vo.Mp;
import ai.zzt.okx.calculate.vo.BOLLVO;
import ai.zzt.okx.v5.enumeration.Bar;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.LinkedList;

/**
 * 布林线（Bollinger Bands）
 *
 * @author zhouzhitong
 * @since 2024-08-21
 **/
public class BOLL extends BaseIndicator<BOLLVO> {

    protected BOLLVO val = new BOLLVO();

    protected Deque<Mp> maxDeque = new LinkedList<>();

    protected Deque<Mp> minDeque = new LinkedList<>();

    public BOLL(int period, Bar bar) {
        super(period, bar);
    }

    @Nullable
    @Override
    protected BOLLVO doCalculate() {
        BigDecimal last = prices.getLast();
        long t = lastTime - (period + 1) * bar.toMillis();
        // 淘汰过期的数据
        if (!maxDeque.isEmpty() && maxDeque.peekFirst().ts < t) {
            maxDeque.pollFirst();
        }
        if (!minDeque.isEmpty() && minDeque.peekFirst().ts < t) {
            minDeque.pollFirst();
        }
        // 计算最新数据
        Mp val = new Mp();
        val.ts = t;
        val.val = last;

        while (!maxDeque.isEmpty() && maxDeque.peekLast().val.compareTo(last) < 0) {
            maxDeque.pollLast();
        }
        while (!minDeque.isEmpty() && minDeque.peekLast().val.compareTo(last) > 0) {
            minDeque.pollLast();
        }

        maxDeque.offerLast(val);
        minDeque.offerLast(val);
        this.val.setMax(maxDeque.peekFirst());
        this.val.setMin(minDeque.peekFirst());

        return this.val;
    }

    @Override
    protected void clear() {
        val = new BOLLVO();
    }

    @Override
    public BOLLVO get() {
        return this.val;
    }

}
