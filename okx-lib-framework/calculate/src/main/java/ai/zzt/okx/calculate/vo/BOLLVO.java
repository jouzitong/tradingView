package ai.zzt.okx.calculate.vo;

import ai.zzt.okx.calculate.indicator.vo.Mp;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2024-08-30
 **/
@Data
public class BOLLVO {

    /**
     * 最大值
     */
    private Mp max;

    /**
     * 最小值
     */
    private Mp min;

    /**
     * 获取最新的最值
     *
     * @return 最新的最值
     */
    public Mp getLast() {
        return max.ts < min.ts ? min : max;
    }

    public BigDecimal getDiff() {
        return max.val.subtract(min.val);
    }

}
