package ai.zzt.okx.calculate.indicator;

import ai.zzt.okx.common.utils.K;
import ai.zzt.okx.common.utils.SlideList;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2024/6/6
 **/
@Getter
public class KLine extends BaseIndicator<SlideList<K>> {

    private final SlideList<K> vals;

    public KLine(int period, Bar timeUnit) {
        super(period, timeUnit);
        this.vals = new SlideList<>(period);
    }

    @Override
    protected SlideList<K> doCalculate() {
        BigDecimal c = prices.getLast();
        if (vals.isEmpty()) {
            vals.add(new K(c, bar, lastTime));
        } else if (!vals.getLast().update(c, ts)) {
            vals.add(new K(c, bar, lastTime));
        }
        return vals;
    }

    @Override
    public SlideList<K> get() {
        return vals;
    }

    @Override
    protected void clear() {
        vals.clear();
    }

    public int size() {
        return prices.size();
    }

}
