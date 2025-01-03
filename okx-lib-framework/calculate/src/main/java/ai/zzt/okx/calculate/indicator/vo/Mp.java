package ai.zzt.okx.calculate.indicator.vo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2024/12/2
 **/
@NoArgsConstructor
@AllArgsConstructor
public class Mp {

    /**
     * 当前时间
     */
    public long ts;

    /**
     * 价格
     */
    public BigDecimal val;

}
