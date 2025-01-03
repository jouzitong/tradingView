package ai.zzt.okx.settings.calculate.strategy.kdj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * J线的作用：
 * <p/>
 * <ul>
 *     <il>xxx</il>
 *     <il>xxx</il>
 * </ol>
 * J线的极端值：J线通常在超买和超卖区域之间波动。J线的数值如果过高（例如，超过100）可能意味着市场已经过度买入，而如果数值过低（例如，低于0）可能意味着市场已经过度卖出。J线的快速波动有时也可以帮助捕捉市场的反转信号。
 * J线向上突破：J线向上突破K线和D线的金叉信号可能表示市场强势上涨的开始。
 * J线向下跌破：J线向下跌破K线和D线的死叉信号可能表示市场可能面临下跌压力。
 *
 * @author zhouzhitong
 * @since 2024/12/31
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KDJJStrategy implements Serializable {

    @Serial
    private static final long serialVersionUID = 2L;

    /**
     * 是否开启J线策略
     */
    private boolean enable = true;

    /**
     * J线的超买界限. 默认 100
     */
    private BigDecimal overbought = BigDecimal.valueOf(90);

    /**
     * J线的超卖界限. 默认 0
     */
    private BigDecimal oversold = BigDecimal.valueOf(10);

}
