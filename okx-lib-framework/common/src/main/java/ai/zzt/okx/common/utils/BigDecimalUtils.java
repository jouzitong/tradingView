package ai.zzt.okx.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author zhouzhitong
 * @since 2024/7/30
 **/
public class BigDecimalUtils {

    public static BigDecimal scale(BigDecimal target, int scale) {
        return target.setScale(scale, RoundingMode.HALF_UP);
    }

    /**
     * 收缩精度(四舍五入)
     *
     * @param target 目标值
     * @param source 原值
     * @return 收缩精度后的结果
     */
    public static BigDecimal round(BigDecimal target, BigDecimal source) {
        int scale = source.scale();
        return target.setScale(scale + 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }

    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return a.subtract(b);
    }

    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }

    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        int scale = a.scale();
        int t = b.scale();
        if (t > scale) {
            scale = t;
        }
        return a.divide(b, scale + 2, RoundingMode.HALF_UP);
    }

}
