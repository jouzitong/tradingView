package ai.zzt.okx.dispatcher.filter.close;

import ai.zzt.okx.okx_client.context.bean.PositionsWrapper;
import ai.zzt.okx.dispatcher.filter.OrderFilter;
import ai.zzt.okx.dispatcher.request.OrderRequest;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import org.jetbrains.annotations.NotNull;
import ai.zzt.okx.v5.enumeration.PositionsSide;

/**
 * @author zhouzhitong
 * @since 2024/6/4
 **/
public interface CloseOrderFilter extends OrderFilter {

    @Override
    default boolean doFilter(TradeOrderRequest request) {
        String instId = request.getInstId();

        OrderRequest orderRequest = request.getRequest();

        PositionsSide positionsSide = orderRequest.getPositionsSide();
        PositionsWrapper wrapper = request.getAccountContext().getPositionContext().getFace(instId).getWrapper(positionsSide);
        if (wrapper == null) {
            return false;
        }
        return doFilter(request, wrapper);
    }

    /**
     * 预处理
     *
     * @param request request
     * @param wrapper 仓位包装类
     * @return 是否继续处理. false 表示继续处理, true 表示不继续处理
     */
    default boolean doFilter(TradeOrderRequest request, @NotNull PositionsWrapper wrapper) {
        return false;
    }


}
