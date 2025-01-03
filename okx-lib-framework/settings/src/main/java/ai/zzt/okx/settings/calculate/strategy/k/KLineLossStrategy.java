package ai.zzt.okx.settings.calculate.strategy.k;

import lombok.Data;

import java.math.BigDecimal;

/**
 * K线超低价策略
 * <p>
 * 描述: 当某个{@link #timePeriod 时间范围}内, 变化幅度超过一定{@link #minDiffRate 阈值}时, 认为是低价, 并且有一定的回调, 就说明开始反弹了.
 * <p>
 *
 * @author zhouzhitong
 * @since 2024/12/13
 **/
@Data
public class KLineLossStrategy {

    /**
     * TODO 时间区间. 默认 1 分钟
     */
    private long timePeriod = 1000L * 60;

    /**
     * 最小变更幅度. 默认 10%
     */
    private BigDecimal minDiffRate = BigDecimal.valueOf(0.1);

    /**
     * TODO 回弹幅度. 默认 02%
     */
    private BigDecimal reboundRate = BigDecimal.valueOf(0.02);

}
