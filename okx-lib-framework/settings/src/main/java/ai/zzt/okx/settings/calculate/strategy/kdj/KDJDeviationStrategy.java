package ai.zzt.okx.settings.calculate.strategy.kdj;

import lombok.Data;

/**
 * 背离现象：
 * <p>
 * 价格与KDJ指标的背离：如果价格创出新高，但KDJ没有同步创出新高，这可能表明市场走势的力量减弱，反转可能即将发生。同样，如果价格创出新低，而KDJ没有同步创出新低，则可能表明市场跌势减缓，反弹的机会增大。
 *
 * @author zhouzhitong
 * @since 2024/12/31
 **/
@Data
public class KDJDeviationStrategy {

    /**
     * 是否开启背离策略
     */
    private boolean enable = false;

    /**
     * 背离的阈值. 默认 0
     */
    private double deviationThreshold = 0;

}
