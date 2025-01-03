package ai.zzt.okx.okx_client.context.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 利润VO
 *
 * @author zhouzhitong
 * @since 2024/10/18
 **/
@Data
public class ProfitVO {

    /**
     * 最大金额
     */
    protected BigDecimal profit = BigDecimal.ZERO;

    /**
     * 时间点
     */
    protected long ts = 0;

    public void clear() {
        this.profit = BigDecimal.ZERO;
        this.ts = 0;
    }

    public BigDecimal getProfitPercentage() {
        return profit.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.UP);
    }

}
