package ai.zzt.okx.dispatcher.utils;

import ai.zzt.okx.common.enums.TrendType;
import org.jetbrains.annotations.NotNull;
import ai.zzt.okx.v5.entity.ws.pri.Positions;
import ai.zzt.okx.v5.enumeration.PositionsSide;

/**
 * @author zhouzhitong
 * @since 2024/7/28
 **/
public class PositionUtils {


    /**
     * 是否可以平仓
     *
     * @param position   当前持仓
     * @param trendTypes 趋势类型
     * @return true
     */
    public static boolean canClosePosition(@NotNull Positions position, @NotNull TrendType... trendTypes) {
        if (trendTypes.length == 1) {
            return trendTypes[0] == TrendType.FLUCTUATE;
        }

        PositionsSide posSide = position.getPosSide();
        switch (posSide) {
            case LONG -> {
                for (TrendType trendType : trendTypes) {
                    if (trendType == TrendType.DOWN) {
                        return true;
                    }
                }
            }
            case SHORT -> {
                for (TrendType trendType : trendTypes) {
                    if (trendType == TrendType.UP) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
