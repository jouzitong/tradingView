package ai.zzt.okx.okx_client.context;

import ai.zzt.okx.common.base.Context;
import ai.zzt.okx.common.exception.TodoRuntimeException;
import ai.zzt.okx.common.utils.InstUtils;
import ai.zzt.okx.okx_client.context.bean.OrdersList;
import ai.zzt.okx.v5.entity.ws.biz.AlgoOrder;
import ai.zzt.okx.v5.entity.ws.pri.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/5/5
 **/
@Slf4j
public class OrderContext implements Context {

    private final OrdersList<AlgoOrder> algoOrderFace = new OrdersList<>();

    private final OrdersList<Order> orderFace = new OrdersList<>();

    public OrderContext() {
    }

    public synchronized void add(Object order) {
        switch (order) {
            case null -> {
            }
            case AlgoOrder ao -> algoOrderFace.add(ao);
            case Order o -> orderFace.add(o);
            default -> {
                log.error("未知订单类型: {}. 订单详情: {}", order.getClass(), order);
                throw new TodoRuntimeException();
            }
        }
    }

    /**
     * 全量同步, 如果数组是空的, 应该都清空原先缓存的相关订单
     *
     * @param orders 全量订单列表
     */
    public synchronized void addAlgoOrders(List<AlgoOrder> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return;
        }
        algoOrderFace.addAll(orders);
    }

    public synchronized void addOrders(List<Order> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return;
        }
        orderFace.addAll(orders);
    }

    public synchronized List<AlgoOrder> getAlgoOrders(String instId) {
        String baseCCy = InstUtils.getBase(instId);
        return algoOrderFace.getOrders().stream()
                .filter(or -> InstUtils.equals(baseCCy, or.getInstId()))
                .toList();
    }

    public synchronized List<Order> getOrders(String instId) {
        String baseCCy = InstUtils.getBase(instId);
        return orderFace.getOrders().stream()
                .filter(o -> InstUtils.equals(baseCCy, o.getInstId()))
                .toList();
    }

    public synchronized boolean remove(Object data) {
        if (data instanceof AlgoOrder ao) {
            return remove(ao);
        } else if (data instanceof Order o) {
            return remove(o);
        }
        return false;
    }

    public boolean remove(AlgoOrder algoOrder) {
        return algoOrderFace.getOrders().remove(algoOrder);
    }

    public boolean remove(Order order) {
        return orderFace.getOrders().remove(order);
    }

    public void clear() {
        algoOrderFace.clear();
        orderFace.clear();
    }

}
