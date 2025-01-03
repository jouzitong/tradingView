package ai.zzt.okx.common.utils;

import ai.zzt.okx.common.enums.MacdTrendType;

import java.math.BigDecimal;
import java.util.Iterator;

/**
 * @author zhouzhitong
 * @since 2024/5/29
 **/
public class TrendUtils {

    /**
     * 连续性判断, 并获取最后一个趋势类型
     *
     * @param trends 趋势列表
     * @param count  连续的次数
     * @return 连续的趋势类型.
     * 如果连续的返回最终类型;
     * 如果不连续返回 {@link MacdTrendType#NONE}
     */
    @Deprecated
    public static MacdTrendType checkContinuityAndGetLast(SlideList<MacdTrendType> trends, int count) {
        if (trends.size() < count) {
            return MacdTrendType.NONE;
        }
        int size = trends.size();
        int index = size - count;
        MacdTrendType start = trends.get(index);
        for (int i = index + 1; i < size; i++) {
            if (!start.hasNext(trends.get(i))) {
                return MacdTrendType.NONE;
            }
            start = trends.get(i);
        }
        return start;
    }

    /**
     * 分析 macd 转换
     *
     * @param macdLines macd趋势分析
     */
    public static SlideList<MacdTrendType> convertTrendType(SlideList<BigDecimal> macdLines) {
        if (macdLines == null || macdLines.size() < 2) {
            return SlideList.empty();
        }
        // 分析 MACD指标
        // 1. 分析趋势， macd 线段是否在上升趋势，还是下降趋势
        Iterator<BigDecimal> it = macdLines.iterator();
        SlideList<MacdTrendType> trends = new SlideList<>(macdLines.size() - 1);
        BigDecimal start = it.next();
        BigDecimal next;


        while (it.hasNext()) {
            next = it.next();
            MacdTrendType tmpTrend = MacdTrendType.fromValue(start, next);
            trends.add(tmpTrend);
            start = next;
        }
        return trends;
    }

}
