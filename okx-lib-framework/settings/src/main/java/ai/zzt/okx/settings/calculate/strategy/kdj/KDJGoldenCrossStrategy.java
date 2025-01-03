package ai.zzt.okx.settings.calculate.strategy.kdj;

import lombok.Data;

/**
 * 交叉信号：
 * <p>
 * K线和D线的金叉：当K线从下向上突破D线时，通常被认为是买入信号，意味着市场可能会开始上涨。
 * K线和D线的死叉：当K线从上向下跌破D线时，通常被认为是卖出信号，意味着市场可能会开始下跌。
 *
 * @author zhouzhitong
 * @since 2024/12/31
 **/
@Data
public class KDJGoldenCrossStrategy {

    /**
     * 是否开启交叉信号
     */
    private boolean enable = true;

    /**
     * 金叉的阈值. 默认 0
     */
    private double goldenCrossThreshold = 0;

    /**
     * 死叉的阈值. 默认 0
     */
    private double deathCrossThreshold = 0;

}
