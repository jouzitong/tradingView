package ai.zzt.okx.dispatcher.engine.impl;

import ai.zzt.okx.common.base.system.Ordered;
import ai.zzt.okx.okx_client.context.PositionContext;
import ai.zzt.okx.okx_client.context.bean.PositionFace;
import ai.zzt.okx.okx_client.context.bean.PositionsWrapper;
import ai.zzt.okx.dispatcher.engine.OrderEngine;
import ai.zzt.okx.dispatcher.filter.close.CloseOrderFilter;
import ai.zzt.okx.dispatcher.filter.open.OpenOrderFilter;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;

/**
 * 下单引擎
 *
 * @author zhouzhitong
 * @since 2024/6/5
 **/
@Slf4j
public abstract class BaseOrderEngine implements OrderEngine {

    @Resource
    private List<CloseOrderFilter> closeOrderFilters;

    @Resource
    private List<OpenOrderFilter> openOrderFilters;

    @PostConstruct
    public void init() {
        if (closeOrderFilters != null) {
            closeOrderFilters.sort(Comparator.comparingInt(Ordered::order));
        }
        if (openOrderFilters != null) {
            openOrderFilters.sort(Comparator.comparingInt(Ordered::order));
        }
    }

    @Override
    public void placeOrder(TradeOrderRequest request) {
        updateProfit(request);
        String instId = request.getInstId();
        PositionContext positionContext = request.getAccountContext().getPositionContext();
        PositionFace positionWrapper = positionContext.getFace(instId);

        for (OpenOrderFilter openOrderFilter : openOrderFilters) {
            openOrderFilter.doFilter(request);
        }
        // 如果有持仓, 则检查是否需要平仓
        if (!positionWrapper.isEmpty()) {
            List<PositionsWrapper> positions = request.getPositionsWrapper(request.getInstId());
            for (PositionsWrapper position : positions) {
                for (CloseOrderFilter closeOrderFilter : closeOrderFilters) {
                    if (closeOrderFilter.doFilter(request, position)) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * 更新仓位信息利润
     *
     * @param request 自动下单请求
     */
    protected void updateProfit(TradeOrderRequest request) {
        String instId = request.getInstId();
        MarkPrice markPrice = request.getMarkPrice();
        PositionContext positionContext = request.getAccountContext().getPositionContext();
        PositionFace positionFace = positionContext.getFace(instId);
        positionFace.updateProfit(markPrice.getMarkPx(), markPrice.getTs());
    }

}
