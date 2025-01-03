package ai.zzt.okx.calculate.indicator.val;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2024/10/17
 **/
@Data
@NoArgsConstructor
public class RSIVal implements IndicatorVal {

    private BigDecimal val;

    public RSIVal(BigDecimal val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val.toString();
    }
}
