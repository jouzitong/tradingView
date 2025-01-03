package ai.zzt.okx.calculate.indicator;

import ai.zzt.okx.common.utils.DateTimeUtils;
import ai.zzt.okx.common.utils.SlideList;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/6/10
 **/
@Slf4j
public abstract class BaseIndicator<R> implements Indicator<R> {

    protected final SlideList<BigDecimal> prices;

    protected final Bar bar;

    /**
     * 上次时间. 周期的开始时间
     */
    protected long lastTime = 0L;

    /**
     * 最新时间
     */
    protected long ts;

    /**
     * 计算周期
     */
    protected final int period;

    /**
     * 精度. 默认 12
     * <p>
     * TODO 这个精度应该在初始化时, 根据实际的价格和情况配置
     */
    private Integer scale;

    /**
     * 是否是更新
     */
    protected boolean update = true;

    public BaseIndicator(int period, Bar bar) {
        this.prices = new SlideList<>(period);
        this.bar = bar;
        this.period = period;
    }

    @Override
    public void init(List<MarkPrice> mps) {
        if (CollectionUtils.isEmpty(mps)) {
            return;
        }
        MarkPrice first = mps.getFirst();
        int scale = first.getMarkPx().scale();
        this.scale = scale + 2;
        for (MarkPrice mp : mps) {
            add(mp.getMarkPx(), mp.getTs());
        }
    }

    @Override
    public void init(MarkPrice mp) {
        int scale = mp.getMarkPx().scale();
        this.scale = scale + 2;
        add(mp.getMarkPx(), mp.getTs());
    }

    @Override
    public synchronized final R add(BigDecimal price, long ts) {
        long diff = ts - lastTime;
        if (bar.toMillis() * 2 < diff) {
            if (lastTime != 0L) {
                log.warn("time interval is too small, diff: {} 【bar: {}】, now time: {}, lastTime: {}",
                        diff, bar, DateTimeUtils.ofTime(ts), DateTimeUtils.ofTime(lastTime));
                prices.clear();
                clear();
            }
            int scale = price.scale();
            this.scale = scale + 2;
            addBefore(price, ts);
            prices.add(price);
            lastTime = bar.suffix(ts);
        } else if (bar.toMillis() <= diff) {
            addBefore(price, ts);
            prices.add(price);
            lastTime = bar.suffix(ts);
        } else {
            this.update = true;
            prices.setLast(price);
        }
        this.ts = ts;
        return doCalculate();
    }

    @Override
    public BigDecimal getPrice() {
        return prices.getLast();
    }

//    /**
//     * 获取指标值
//     *
//     * @return 指标值
//     */
//    public R get() {
//        return doCalculate();
//    }

    /**
     * 计算指标
     */
    @Nullable
    protected abstract R doCalculate();

    protected void addBefore(BigDecimal price, long ts) {
        // do no thing. 子类可重写, 注意一定要调用父类实现
        update = false;
    }

    /**
     * 清空数据. 主要是用于时间间隔过大时, 清空数据, 重新计算
     */
    protected abstract void clear();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        BigDecimal init = prices.get(i++);
        sb.append(init);

        for (; i < prices.size(); i++) {
            BigDecimal next = prices.get(i);
            BigDecimal diff = next.subtract(init);
            sb.append(", ").append(diff)
                    .append(diff.compareTo(BigDecimal.ZERO) > 0 ? "↑" : "↓");
            init = next;
        }

        return sb.toString();
    }

    protected Integer getScale() {
        if (scale == null) {
            int scale = this.prices.getFirst().scale();
            this.scale = scale + 2;
        }
        return scale;
    }
}
