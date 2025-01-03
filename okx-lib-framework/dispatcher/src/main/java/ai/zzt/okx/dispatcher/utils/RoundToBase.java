package ai.zzt.okx.dispatcher.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author zhouzhitong
 * @since 2024-08-29
 **/
public class RoundToBase {

    public static BigDecimal roundToBase(BigDecimal number, BigDecimal base) {
        // 计算取整后的结果
        BigDecimal result = number.divide(base, 0, RoundingMode.HALF_UP).multiply(base);
        // 如果结果大于原始数字，则取基准的倍数减去原始数字的最小值
        if (result.compareTo(number) > 0) {
            return result.subtract(base);
        }
        return result;
    }

}
