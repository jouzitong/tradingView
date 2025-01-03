package ai.zzt.okx.dispatcher.filter;

import ai.zzt.okx.dispatcher.request.TradeOrderRequest;

/**
 * @author zhouzhitong
 * @since 2024-08-29
 **/
public class OrderFilterChain {

    private final OrderFilter[] filters;

    private final ThreadLocal<Integer> nextThreadLocal = new ThreadLocal<>();

    public OrderFilterChain(OrderFilter... filters) {
        this.filters = filters;
    }

    public boolean doFilter(TradeOrderRequest request) {
        Integer index = nextThreadLocal.get();
        if (index == null) {
            index = 0;
            nextThreadLocal.set(0);
        }

        return true;
    }


}
