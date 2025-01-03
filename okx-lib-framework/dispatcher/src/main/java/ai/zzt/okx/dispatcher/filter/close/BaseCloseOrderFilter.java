package ai.zzt.okx.dispatcher.filter.close;

import ai.zzt.okx.dispatcher.request.OrderRequest;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.dispatcher.service.OrderService;
import ai.zzt.okx.v5.entity.ws.pri.Positions;
import ai.zzt.okx.v5.enumeration.OrdType;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @author zhouzhitong
 * @since 2024-08-29
 **/
@Slf4j
public abstract class BaseCloseOrderFilter implements CloseOrderFilter {

    @Resource
    protected OrderService orderService;

    /**
     * 填充止盈限价平仓参数
     *
     * @param request     上下文对象
     * @param positions   仓位信息
     * @param targetPrice 目标价格
     */
    protected final boolean conditionalPend(TradeOrderRequest request, Positions positions, BigDecimal targetPrice) {
        OrderRequest orderRequest = request.getOrderRequest();
        PositionsSide posSide = positions.getPosSide();
        orderRequest.setOrdType(OrdType.CONDITIONAL);
        orderRequest.setPositionsSide(posSide);
        orderRequest.setOperatePrice(targetPrice);
        return orderService.closeOrder(request);
    }

    /**
     * 填充立即平仓参数
     *
     * @param request   上下文对象
     * @param positions 仓位信息
     */
    protected final boolean instantlyClosePend(TradeOrderRequest request, Positions positions) {
        OrderRequest orderRequest = request.getOrderRequest();
        PositionsSide posSide = positions.getPosSide();
        orderRequest.setPositionsSide(posSide);
        return orderService.closeOrder(request);
    }

}
