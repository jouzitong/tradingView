package ai.zzt.okx.settings.calculate.strategy.kdj;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 超买和超卖区域：
 * <p>
 * 超买（Overbought）：当K线和D线的数值大于80时，市场可能处于超买状态，意味着价格可能会回调或反转。
 * 超卖（Oversold）：当K线和D线的数值小于20时，市场可能处于超卖状态，意味着价格可能会反弹或反转。
 *
 * @author zhouzhitong
 * @since 2024/12/31
 **/
@Data
public class KDJOverStrategy {

    /**
     * 超买界限. 默认 70
     */
    private BigDecimal overbought = BigDecimal.valueOf(80);

    /**
     * 超卖界限. 默认 30
     */
    private BigDecimal oversold = BigDecimal.valueOf(20);

}
