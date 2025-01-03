package ai.zzt.okx.calculate.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author zhouzhitong
 * @since 2024/12/12
 **/
public class TUtils {

    public static BigDecimal get(BigDecimal h, BigDecimal l) {
        return h.subtract(l).divide(l, 4, RoundingMode.HALF_DOWN);
    }

}
