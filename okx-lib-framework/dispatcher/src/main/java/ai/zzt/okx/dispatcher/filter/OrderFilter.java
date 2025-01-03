package ai.zzt.okx.dispatcher.filter;

import ai.zzt.okx.common.base.system.Ordered;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;

/**
 * @author zhouzhitong
 * @since 2024/6/3
 **/
public interface OrderFilter extends Ordered {

    /**
     * 预处理
     *
     * @param request request
     * @return 是否继续处理. false 表示继续处理, true 表示不继续处理
     */
    default boolean doFilter(TradeOrderRequest request) {
        return false;
    }

}
