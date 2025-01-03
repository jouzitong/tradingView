package ai.zzt.okx.okx_client.utils;

import org.jetbrains.annotations.NotNull;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.Side;

/**
 * okx 下单工具类
 *
 * @author zhouzhitong
 * @since 2024/6/16
 **/
public class OkxOrderUtils {

    /**
     * 判断是否是开仓
     *
     * @param positionsSide 持仓方向
     * @param side          下单方向
     * @return true 开仓; false 平仓
     */
    public static boolean isOpenPositions(@NotNull PositionsSide positionsSide, @NotNull Side side) {
        return (positionsSide == PositionsSide.LONG && side == Side.BUY) ||
                (positionsSide == PositionsSide.SHORT && side == Side.SELL);
    }

}
