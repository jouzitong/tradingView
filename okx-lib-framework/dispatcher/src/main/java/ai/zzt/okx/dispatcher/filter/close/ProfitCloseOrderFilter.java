//package ai.zzt.okx.trade_order.filter.close;
//
//import ai.zzt.okx.common.enums.TrendType;
//import ai.zzt.okx.common.utils.DateTimeUtils;
//import ai.zzt.okx.okx_client.context.bean.PositionsWrapper;
//import ai.zzt.okx.settings.context.SettingsContext;
//import ai.zzt.okx.settings.order.PlaceOrderSettings;
//import ai.zzt.okx.trade_order.request.TradeOrderRequest;
//import ai.zzt.okx.trade_order.utils.PositionUtils;
//import ai.zzt.okx.v5.entity.ws.pri.Positions;
//import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
//import lombok.extern.slf4j.Slf4j;
//import org.jetbrains.annotations.NotNull;
//
//import java.math.BigDecimal;
//
///**
// * 利润平仓拦截
// *
// * @author zhouzhitong
// * @since 2024/6/3
// **/
//@Slf4j
//@Deprecated
////@Service
//public class ProfitCloseOrderFilter extends BaseCloseOrderFilter {
//
//    @Override
//    public boolean doFilter(TradeOrderRequest request, @NotNull PositionsWrapper wrapper) {
//        if (!check(request, wrapper)) {
//            return false;
//        }
//        return instantlyClosePend(request, wrapper.positions);
//    }
//
//    private boolean check(TradeOrderRequest request, @NotNull PositionsWrapper wrapper) {
//        Positions positions = wrapper.getPositions();
//        TrendType localTrend = request.getAnalyzeContext().getLocalTrend();
//        if (!PositionUtils.canClosePosition(positions, localTrend)) {
//            return false;
//        }
//
//        String instId = request.getInstId();
//        // 最新价格
//        MarkPrice markPrice = request.getMarkPrice();
//        // 获取配置
//        SettingsContext settings = request.getSettingsContext();
//        PlaceOrderSettings placeOrderSettings = settings.getPlaceOrderSettings();
//
//        // 获取预期收益率
//        BigDecimal wishProfitRate = placeOrderSettings.getProfit().getProfitRate();
//
//        // 获取实际收益率
//        BigDecimal realProfitRate = wrapper.getProfitRate();
//        //
//        if (placeOrderSettings.getProfit().isLeverProfit()) {
//            realProfitRate = realProfitRate.multiply(positions.getLever());
//        }
//        if (realProfitRate.compareTo(wishProfitRate) >= 0) {
//            log.debug("【止盈】仓位: {}【方向{}】, 开仓时间: {}, 开仓价格: {}. 当前时间: {}, 当前价格: {}, 预期: {}, 实际: {}",
//                    instId, positions.getPosSide(), DateTimeUtils.ofTime(positions.getCTime()), positions.getAvgPx(),
//                    DateTimeUtils.ofTime(markPrice.getTs()), markPrice.getMarkPx(), wishProfitRate, realProfitRate);
//            return true;
//        }
//        BigDecimal stopLossRate = placeOrderSettings.getStopLossRate();
//        // 止损平仓. 是否支持止损 & 实际收益率 < 0 & 实际收益率绝对值 >= 止损率绝对值
//        if (placeOrderSettings.isSupportStopLoss()
//                && realProfitRate.compareTo(BigDecimal.ZERO) < 0 &&
//                realProfitRate.abs().compareTo(stopLossRate.abs()) >= 0) {
//            log.debug("【止损】仓位: {}【方向{}】, 开仓时间: {}, 开仓价格: {}. 当前时间: {}, 当前价格: {}, 预期: {}, 实际: {}",
//                    instId, positions.getPosSide(), DateTimeUtils.ofTime(positions.getCTime()), positions.getAvgPx(),
//                    DateTimeUtils.ofTime(markPrice.getTs()), markPrice.getMarkPx(), stopLossRate, realProfitRate);
//            return true;
//        }
//        return false;
//    }
//
//}
