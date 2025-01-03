package ai.zzt.okx.dispatcher.filter.close;

import ai.zzt.okx.common.base.system.Ordered;
import ai.zzt.okx.okx_client.context.bean.PositionsWrapper;
import ai.zzt.okx.settings.order.profit.ProfitSettings;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 最大利润止盈平仓过滤器
 *
 * @author zhouzhitong
 * @since 2024-08-29
 **/
@Service
@Slf4j
public class MaxProfitCloseFilter extends BaseCloseOrderFilter {

    @Override
    public boolean doFilter(TradeOrderRequest request, @NotNull PositionsWrapper wrapper) {
        ProfitSettings profitSettings = request.getSettingsContext().getPlaceOrderSettings().getProfit();
        BigDecimal profitRate = wrapper.getProfitRate();
        if (profitSettings.isLeverProfit()) {
            profitRate = profitRate.multiply(wrapper.getPositions().getLever());
        }
        if (profitSettings.getMaxProfitRate().compareTo(profitRate) <= 0) {
            // 大约最大收益立即平仓
            return instantlyClosePend(request, wrapper.positions);
        }
        return false;
    }

    @Override
    public int order() {
        return Ordered.HIGH;
    }
}
