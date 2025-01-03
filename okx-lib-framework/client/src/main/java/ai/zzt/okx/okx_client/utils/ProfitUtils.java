package ai.zzt.okx.okx_client.utils;

import ai.zzt.okx.common.exception.TodoRuntimeException;
import ai.zzt.okx.common.utils.BigDecimalUtils;
import ai.zzt.okx.common.utils.json.JackJsonUtils;
import ai.zzt.okx.v5.entity.ws.pri.Positions;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 利润计算工具类
 *
 * @author zhouzhitong
 * @since 2024-08-28
 **/
@Slf4j
@Service
public class ProfitUtils {

    /**
     * 根据目标利润率, 计算目标价格. 暂时忽略手续费的情况
     *
     * @param positions        当前仓位
     * @param targetProfitRate 目标利润
     * @return 目标价格
     */
    public static BigDecimal getTargetPrice(Positions positions, BigDecimal targetProfitRate) {
        if (positions == null) {
            return BigDecimal.ZERO;
        }
        // 持仓方向
        PositionsSide posSide = positions.getPosSide();
        // 平均开仓价
        BigDecimal avgPx = positions.getAvgPx();
        if (posSide == PositionsSide.LONG) {
            // 计算目标价格 = 平均开仓价 * (1 + 目标利润率)
            return avgPx.multiply(BigDecimal.ONE.add(targetProfitRate));
        } else if (posSide == PositionsSide.SHORT) {
            // 计算目标价格 = 平均开仓价 * (1 - 目标利润率)
            return avgPx.multiply(BigDecimal.ONE.subtract(targetProfitRate));
        } else {
            throw new TodoRuntimeException(); // 无效值, 目前不支持
        }
    }

    /**
     * 计算收益
     *
     * @param positions   持仓信息
     * @param marketPrice 当前价格
     * @param feeRate     手续费
     * @return 收益
     */
    public static BigDecimal getProfit(Positions positions, BigDecimal marketPrice, BigDecimal feeRate) {
        // 交易币种数量
        BigDecimal sz = sz(positions);
        // 手续费
        BigDecimal fee = feeRate.multiply(positions.getAvgPx()).multiply(sz);

        BigDecimal diff = ProfitUtils.getDiffProfit(positions, marketPrice);
        return diff.multiply(sz).subtract(fee);
    }

    /**
     * 计算收益【不包含手续费】
     *
     * @param positions   持仓信息
     * @param marketPrice 市场价值
     * @return 收益率
     */
    public static BigDecimal getProfit(Positions positions, BigDecimal marketPrice) {
        // 交易币种数量
        BigDecimal sz = sz(positions);
        BigDecimal diff = ProfitUtils.getDiffProfit(positions, marketPrice);
        return diff.multiply(sz);
    }

    /**
     * 获取手续费
     *
     * @param positions 持仓信息
     * @param feeRate   手续费
     * @return 收益
     */
    public static BigDecimal getFee(Positions positions, BigDecimal feeRate) {
        // 交易币种数量
        BigDecimal sz = sz(positions);
        // 手续费
        return feeRate.multiply(positions.getAvgPx()).multiply(sz);
    }


    /**
     * 计算收益率【不包含手续费，和杠杆】
     *
     * @param positions   持仓信息
     * @param marketPrice 市场价值
     * @return 收益率
     */
    public static BigDecimal getProfitRate(Positions positions, BigDecimal marketPrice) {
        BigDecimal diff = getDiffProfit(positions, marketPrice);
        // 计算利润率 = 差价/平均开仓价
        return BigDecimalUtils.divide(diff, positions.getAvgPx()).setScale(4, RoundingMode.HALF_UP);
    }

    /**
     * 计算差价盈亏
     *
     * @param positions   持仓信息
     * @param marketPrice 市价
     * @return 差价盈亏
     */
    public static BigDecimal getDiffProfit(Positions positions, BigDecimal marketPrice) {
        // 持仓方向
        PositionsSide posSide = positions.getPosSide();
        // 平均开仓价
        BigDecimal avgPx = positions.getAvgPx();
        if (avgPx == null) {
            log.warn("平均开仓价为空, 无法计算盈亏. positions: {}", JackJsonUtils.toStr(positions));
            return BigDecimal.ZERO;
        }

        // 计算差价
        return switch (posSide) {
            case LONG -> marketPrice.subtract(avgPx);
            case SHORT -> avgPx.subtract(marketPrice);
            // 杠杆？？？
            case NET -> throw new TodoRuntimeException(); // 无效值, 目前不支持
        };
    }

    private static BigDecimal sz(Positions positions) {
        InstrumentType instType = positions.getInstType();
        BigDecimal sz;
        if (instType == InstrumentType.SWAP) {
            sz = SwapCoinUtils.ccy(positions.getInstId(), positions.getPos());
        } else {
            sz = positions.getAvailPos();
        }
        return sz;
    }

}
