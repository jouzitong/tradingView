package ai.zzt.okx.common.enums;

import ai.zzt.okx.common.utils.SlideList;
import ai.zzt.okx.common.utils.print.Str;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * macd 趋势分析类型
 * <pre>
 *      1 -
 *      -   - 2         -
 *     --------------------------
 *              -    -
 *              3 -  4
 *  1 正数上升
 *  2 正数下降
 *  3 负数下降
 *  4 负数上升
 * </pre>
 *
 * @author zhouzhitong
 * @since 2024/5/25
 **/
@Getter
public enum MacdTrendType implements Str {

    NONE("无法判断"),

    PLUS_UP("正数上升"),
    PLUS_DOWN("正数下降"),
    BURDEN_DOWN("负数下降"),
    BURDEN_UP("负数上升"),
    ;

    private final String name;

    MacdTrendType(String name) {
        this.name = name;
    }

    public boolean hasNext(MacdTrendType next) {
        if (next == null) {
            return false;
        }
        if (this == next) {
            return true;
        }
        return switch (this) {
            case PLUS_UP -> next == PLUS_DOWN;
            case PLUS_DOWN -> next == BURDEN_DOWN;
            case BURDEN_UP -> next == PLUS_UP;
            case BURDEN_DOWN -> next == BURDEN_UP;
            case NONE -> false;
        };
    }

    public static MacdTrendType fromValue(BigDecimal start, BigDecimal next) {
        boolean plus = next.compareTo(BigDecimal.ZERO) >= 0;
        // start 与 next 比较大小. flag 为 true 时, start 大于 next.
        boolean up = start.compareTo(next) <= 0;
        if (plus) {
            if (up) {
                return PLUS_UP;
            } else {
                return PLUS_DOWN;
            }
        } else {
            if (up) {
                return BURDEN_UP;
            } else {
                return BURDEN_DOWN;
            }
        }
    }

    /**
     * 获取macd趋势. 只判断是否是金叉线
     *
     * @param values 趋势值
     * @return 趋势类型
     */
    public static MacdTrendType fromValue(SlideList<MacdTrendType> values) {
        if (values.size() < 10) {
            return MacdTrendType.NONE;
        }
        MacdTrendType secLast = values.getSecLast();
        MacdTrendType last = values.getLast();
        if (secLast == MacdTrendType.BURDEN_UP || secLast == MacdTrendType.BURDEN_DOWN) {
            if (last == PLUS_DOWN || last == PLUS_UP) {
                return MacdTrendType.PLUS_UP;
            }
        }
        if (secLast == MacdTrendType.PLUS_UP || secLast == MacdTrendType.PLUS_DOWN) {
            if (last == BURDEN_DOWN || last == BURDEN_UP) {
                return MacdTrendType.BURDEN_DOWN;
            }
        }
        return MacdTrendType.NONE;
    }

    /**
     * 连续性判断, 并获取最后一个趋势类型
     *
     * @param trends   趋势列表
     * @param period   计算个数(周期)
     * @param minCount 末位最小连续次数【包含】
     * @param maxCount 末位最大连续次数【包含】
     * @return 连续的趋势类型.
     * 如果连续的返回最终类型;
     * 如果不连续返回 {@link MacdTrendType#NONE}
     */
    public static MacdTrendType checkContinuityAndGetLast(SlideList<MacdTrendType> trends, int minCount, int maxCount, int period) {
        if (trends.size() < 10) {
            return MacdTrendType.NONE;
        }
        int size = trends.size();
        int index = size - period;
        int c = 1;
        MacdTrendType start = trends.get(index);
        for (int i = index + 1; i < size; i++) {
            MacdTrendType next = trends.get(i);
            if (!start.hasNext(next)) {
                return MacdTrendType.NONE;
            }
            if (start == next) {
                c++;
            } else {
                c = 1;
                start = next;
            }
        }
        return minCount <= c && c <= maxCount ? start : NONE;
    }

    @Override
    public String toStr() {
        return switch (this) {
            case PLUS_UP -> "+↑";
            case PLUS_DOWN -> "+↓";
            case BURDEN_DOWN -> "-↓";
            case BURDEN_UP -> "-↑";
            default -> "-";
        };
    }

}
