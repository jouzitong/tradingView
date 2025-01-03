package ai.zzt.okx.dispatcher.filter.close;

import ai.zzt.okx.common.utils.DateTimeUtils;
import ai.zzt.okx.okx_client.context.bean.PositionFace;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.settings.order.PlaceOrderSettings;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 持仓超时平仓拦截. 如果持仓太久, 则强制平仓
 *
 * @author zhouzhitong
 * @since 2024/6/28
 **/
@Service
@Slf4j
public class PositionMaxTimeCloseFilter extends BaseCloseOrderFilter {
    @Override
    public boolean doFilter(TradeOrderRequest request) {
        if (!check(request)) {
            return false;
        }
        // TODO
        return orderService.closeOrder(request);
    }

    private boolean check(TradeOrderRequest request) {
        SettingsContext settingsContext = request.getSettingsContext();
        PlaceOrderSettings placeOrderSettings = settingsContext.getPlaceOrderSettings();
        if (!placeOrderSettings.isSupportMaxTime()) {
            return false;
        }
        String instId = request.getInstId();
        PositionFace wrapper = request.getAccountContext().getPositionContext().getFace(instId);
        long positionMaxTime = placeOrderSettings.getPositionMaxTime();
        Long cTime = wrapper.lastOpenTime;
        Long currentTs = request.getMarkPrice().getTs();
        if (currentTs - cTime > positionMaxTime) {
            if (!request.isOverTime()) {
                request.setOverTime(true);
                // FIXME 日志需要优化
                log.warn("【持仓超时】仓位: {}, 开仓时间: {}. 当前时间: {}, 当前价格: {}", instId,
                        DateTimeUtils.ofTime(cTime), DateTimeUtils.ofTime(currentTs), request.getMarkPrice().getMarkPx());
            }

            return placeOrderSettings.isSupportMaxTime();
        }
        return false;
    }

    @Override
    public int order() {
        return 100;
    }
}
