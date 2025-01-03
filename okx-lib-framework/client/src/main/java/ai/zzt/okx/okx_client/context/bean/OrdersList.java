package ai.zzt.okx.okx_client.context.bean;

import ai.zzt.okx.common.context.SystemContext;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author zhouzhitong
 * @since 2024/11/1
 **/
@Data
@NoArgsConstructor
public class OrdersList<T> {

    private final List<T> orders = new CopyOnWriteArrayList<>();

    /**
     * 最新更新时间
     */
    private long ts;


    public synchronized void addAll(List<T> orders) {
        this.orders.clear();
        this.orders.addAll(orders);
        this.ts = SystemContext.currentTimeMillis();
    }

    public synchronized void add(T order) {
        this.orders.add(order);
        this.ts = SystemContext.currentTimeMillis();
    }

    public boolean isEmpty() {
        return orders.isEmpty();
    }

    public synchronized void clear() {
        orders.clear();
    }

}
