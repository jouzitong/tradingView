package ai.zzt.okx.v5.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 时间粒度
 *
 * @author Jou Ziton
 * @since 2024/6/5
 **/
public enum Bar {

    // 在存储实时K线数据时被需要
    MS_1("1ms", "分时", Bar.MILLI_SCALE),

    s1("1s", "1秒", Bar.SECOND_SCALE),

    // 1m/3m/5m/15m/30m/1H/2H/4H/1D
    M_1("1m", "1分", Bar.MINUTE_SCALE),
    M_3("3m", "3分", Bar.MINUTE_3_SCALE),
    M_5("5m", "5分", Bar.MINUTE_5_SCALE),
    M_15("15m", "15分", Bar.MINUTE_15_SCALE),
    M_30("30m", "30分", Bar.MINUTE_HALF_SCALE),
    H_1("1H", "1时", Bar.HOUR_SCALE),
    H_2("2H", "2时", Bar.HOUR_2_SCALE),
    H_4("4H", "4时", Bar.HOUR_4_SCALE),
    D_1("1D", "1日", Bar.DAY_SCALE),
    D_5("5D", "2日", Bar.DAY_SCALE * 5),
    W_1("1W", "1周", Bar.WEEK_DAY_SCALE),
    M1("1M", "1月", Bar.MONTH_SCALE),
    ;

    // Scales as constants
    private static final long MILLI_SCALE = 1L;
    private static final long SECOND_SCALE = 1000L * MILLI_SCALE;

    private static final long MINUTE_SCALE = 60L * SECOND_SCALE;
    private static final long MINUTE_3_SCALE = MINUTE_SCALE * 3;
    private static final long MINUTE_5_SCALE = MINUTE_SCALE * 5;
    private static final long MINUTE_15_SCALE = MINUTE_SCALE * 15;
    private static final long MINUTE_HALF_SCALE = MINUTE_SCALE * 30;

    private static final long HOUR_SCALE = 60L * MINUTE_SCALE;
    private static final long HOUR_2_SCALE = 2L * HOUR_SCALE;
    private static final long HOUR_4_SCALE = 4L * HOUR_SCALE;
    private static final long DAY_SCALE = 24L * HOUR_SCALE;
    private static final long WEEK_DAY_SCALE = 7L * DAY_SCALE;

    private static final long MONTH_SCALE = 30L * DAY_SCALE; // 目前简单认为是30天吧, 没有必要较真

    public static final List<Bar> BARS;

    static {
        BARS = new ArrayList<>();
        for (Bar bar : values()) {
            if (bar != MS_1) {
                BARS.add(bar);
            }
        }
    }

    @JsonValue
    @Getter
    private final String value;

    @Getter
    private final String name;

    @Getter
    private final long scale;

    Bar(String value, String name, long scale) {
        this.value = value;
        this.scale = scale;
        this.name = name;
    }

    public long toMillis() {
        return scale;
    }

    /**
     * 判断`ts`是否在指定时间`targetTime`区间内
     *
     * @param targetTime 指定时间
     * @param ts         目前时间
     * @return 是否在区间内
     */
    public boolean inner(long targetTime, long ts) {
        long diff = ts - targetTime;
        return toMillis() > diff;
    }

    /**
     * 获取当前时间, 去除低于当前时间单位的部分
     *
     * @return 当前时间
     */
    public long now() {
        return suffix(System.currentTimeMillis());
    }

    /**
     * 去除低于当前时间单位的部分
     *
     * @param ts 时间戳
     * @return 去除低于当前时间单位的部分
     */
    public long suffix(long ts) {
        return ts - (ts % toMillis());
    }

    /**
     * 对时间单位进行排序. 从大到小(scale)
     *
     * @param tus 时间单位列表
     */
    public static void sort(List<Bar> tus) {
        tus.sort((o1, o2) -> Long.compare(o2.scale, o1.scale));
    }

    public static Bar ofVal(String val) {
        for (Bar bar : values()) {
            if (val.equals(bar.value)) {
                return bar;
            }
        }
        return null;
    }

    //    @JsonCreator
    public static Bar of(String bar) {
        // 分时: 一天内是纯数字, 分钟级的.
        // 超过一天的, 则是 D

        // 判断是否是纯数字的字符串
        if (bar.matches("\\d+")) {
            int num = Integer.parseInt(bar);
            if (num == 1) {
                return M_1;
            } else if (num == 15) {
                return M_15;
            } else if (num == 30) {
                return M_30;
            } else if (num == 60) {
                return H_1;
            } else if (num == 120) {
                return H_2;
            } else if (num == 240) {
                return H_4;
            }
        }
        return switch (bar) {
            case "1D" -> D_1;
            case "5D" -> D_5;
            case "1W" -> W_1;
            case "1M" -> M1;
            case "1s" -> MS_1;
            default -> null;
        };
    }

}
