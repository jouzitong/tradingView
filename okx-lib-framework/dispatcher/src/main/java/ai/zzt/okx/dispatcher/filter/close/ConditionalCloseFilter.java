package ai.zzt.okx.dispatcher.filter.close;

import ai.zzt.okx.common.base.system.Ordered;
import ai.zzt.okx.common.utils.DateTimeUtils;
import ai.zzt.okx.okx_client.context.bean.PositionsWrapper;
import ai.zzt.okx.okx_client.utils.ProfitUtils;
import ai.zzt.okx.settings.order.profit.GreedyOcoSettings;
import ai.zzt.okx.settings.order.profit.ProfitSettings;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.v5.entity.ws.pri.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 止盈平仓过滤器
 * <p>
 * TODO 待优化: 应该本地存储利润和目标价格, 等真达到目标价格时再进行真正的下单操作, 避免频繁下单, 产生未知异常
 *
 * @author zhouzhitong
 * @since 2024-08-29
 **/
@Service
@Slf4j
public class ConditionalCloseFilter extends BaseCloseOrderFilter {

    @Override
    public boolean doFilter(TradeOrderRequest request, @NotNull PositionsWrapper positions) {
        ProfitSettings profitSettings = request.getSettingsContext().getPlaceOrderSettings().getProfit();
        GreedyOcoSettings greedyOco = profitSettings.getGreedyOco();
        if (greedyOco == null || !greedyOco.isEnable()) {
            return false;
        }
        long ts = request.getMarkPrice().getTs();
        if (ts == DateTimeUtils.ofSecond("2021-03-24T01:05:30")){
            log.info("xx");
        }
        // 获取当前收益率
        BigDecimal realProfitRate = positions.getProfitRate();
        if (profitSettings.isLeverProfit()) {
            realProfitRate = realProfitRate.multiply(positions.getPositions().getLever());
        }

        // 小于最先收益率, 不考虑平仓
        if (realProfitRate.compareTo(profitSettings.getMinProfitRate()) < 0) {
            return false;
        }

        // 获取目标利润率
        BigDecimal targetProfitRate;
        // 当前收益率如果高于最小收益, 则 目标收益率 = 当前收益率 - 贪婪收益率
        BigDecimal tmp = realProfitRate.subtract(greedyOco.getGreedyOcoRate());
        if (tmp.compareTo(profitSettings.getMinProfitRate()) <= 0) {
            targetProfitRate = profitSettings.getMinProfitRate();
        } else {
            // 当前止盈平仓收益率
            BigDecimal curOcoProfitRate = positions.conditionalCloseProfitRate;
            BigDecimal nextOcoProfitRate = curOcoProfitRate.add(greedyOco.getGreedyOcoRateTrigger());
            if (tmp.compareTo(nextOcoProfitRate) <= 0) {
                return false;
            }
            targetProfitRate = tmp;
        }

        if (positions.getPositions() == null) {
            return false;
        }

        if (realProfitRate.compareTo(targetProfitRate) < 0) {
            return false;
        }


        BigDecimal targetPrice = ProfitUtils.getTargetPrice(positions.getPositions(), targetProfitRate);

        // 确保止盈平仓价格高于当前止盈价格
        List<Order> orders = request.getAccountContext().getOrderContext().getOrders(request.getInstId());
        if (CollectionUtils.isNotEmpty(orders)) {
            List<Order.AttachAloOrder> attachAloOrders = orders.getLast().getAttachAlgoOrds();
            if (attachAloOrders != null) {
                BigDecimal tpOrdPx = attachAloOrders.getFirst().getTpOrdPx();
                if (tpOrdPx.compareTo(targetPrice) >= 0) {
                    return false;
                }
            }
        }
        // 条件单平仓
        if (conditionalPend(request, positions.getPositions(), targetPrice)) {
            positions.conditionalCloseProfitRate = targetProfitRate;
            return true;
        }
        return false;
    }

    @Override
    public int order() {
        return Ordered.LOW;
    }
}
