package ai.zzt.okx.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * 趋势分析类型
 *
 * @author zhouzhitong
 * @since 2024/5/25
 **/
@Getter
public enum TrendType implements Serializable {

    UP("UP", "看涨"),
    DOWN("DOWN", "看跌"),
    FLUCTUATE("FLUCTUATE", "震荡/不确定"),
    ;

    private final String name;
    @JsonValue
    private final String desc;

    TrendType(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    /**
     * 判断是否有下一个趋势
     *
     * @param tr 下一个趋势
     * @return true
     */
    public boolean hasNext(TrendType tr) {
        if (this == tr) {
            return true;
        }
//        return false;
        return (this != UP || tr != DOWN) &&
                (this != DOWN || tr != UP);
    }

    /**
     * 根据当前值和下一个值, 计算趋势类型
     *
     * @param start 起始值
     * @param next  下一个值
     * @return 趋势类型
     */
    @NotNull
    public static TrendType add(BigDecimal start, BigDecimal next) {
        boolean flag = start.compareTo(next) >= 0;
        if (flag) {
            return UP;
        } else {
            return DOWN;
        }
    }

    public static void main(String[] args) {
        System.out.println(add(UP, UP, UP));
        System.out.println(add(UP, UP, FLUCTUATE));
        System.out.println(add(UP, DOWN, FLUCTUATE));
        System.out.println(add(DOWN, FLUCTUATE, DOWN));
    }

    @NotNull
    public static TrendType add(@NotNull TrendType... trendTypes) {
        if (trendTypes.length == 0) {
            return FLUCTUATE;
        }
        if (trendTypes.length == 1) {
            return trendTypes[0];
        }
        trendTypes = Arrays.stream(trendTypes)
                .filter(t -> t != FLUCTUATE)
                .toArray(TrendType[]::new);
        if (trendTypes.length == 0) {
            return FLUCTUATE;
        }
        int i = 0;
        TrendType f = trendTypes[i++];
        for (; i < trendTypes.length; i++) {
            if (f != trendTypes[i]) {
                break;
            }
        }
        if (i == trendTypes.length) {
            // 全部相同
            return f;
        }
        i = 1;
        for (; i < trendTypes.length; i++) {
            f = add(f, trendTypes[i]);
            if (f == FLUCTUATE) {
                return FLUCTUATE;
            }
        }

        return f;
    }

    @NotNull
    public static TrendType add(TrendType t1, TrendType t2) {
        if (t1 == t2) {
            return t1;
        }
        return switch (t1) {
            case UP:
                if (t2 == DOWN) {
                    yield FLUCTUATE;
                } else {
                    yield UP;
                }
            case DOWN:
                if (t2 == UP) {
                    yield FLUCTUATE;
                } else {
                    yield DOWN;
                }
            case FLUCTUATE:
                yield t2;
        };
    }
}
