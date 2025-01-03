package ai.zzt.okx.web.req;

import ai.zzt.okx.common.utils.DateTimeUtils;
import ai.zzt.okx.v5.entity.ws.pri.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/7/31
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class KLineVO {

    /**
     * K 线数据
     * <pre>
     * 0. 开盘时间
     * 1. 开盘价
     * 2. 收盘价
     * 3. 最低价
     * 4. 最高价
     *
     * 5. 成交量
     * 6. 成交额
     *
     * 7. macd
     * 8. dif
     * 9. dea
     * 10. RSI
     * </pre>
     */
    private Object[] data;

    /**
     * 订单数据
     */
    private List<Order> orders;

    public void addOrders(List<Order> orders) {
        if (this.orders == null) {
            this.orders = new ArrayList<>();
        }
        for (Order order : orders) {
            if (order.getCTime() < 1000000000000L) {
                continue;
            }
            // 由毫秒转换成秒级时间戳
            long ts = order.getCTime() / 1000;
            if (ts == 0) {
                log.error("订单时间戳为0, 忽略此订单: {}", order);
            }
            order.setCTime(ts);
            this.orders.add(order);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("K LineData: ")
                .append(" 开盘时间: ").append(DateTimeUtils.ofTime((long) data[0]))
                .append(", 开盘价: ").append(data[1])
                .append(", 收盘价: ").append(data[2])
                .append(", 最低价: ").append(data[3])
                .append(", 最高价: ").append(data[4])
                .append(", 成交量: ").append(data[5])
                .append(", 成交额: ").append(data[6])
                .append(", macd: ").append(data[7])
                .append(" dif: ").append(data[8])
                .append(" dea: ").append(data[9])
                .append(" RSI: ").append(data[10]);

        if (CollectionUtils.isNotEmpty(orders)) {
            sb.append("\n下单数据:");
            for (int i = 0; i < orders.size(); i++) {
                sb.append("\n订单: ").append((i + 1)).append("\t").append(orders.get(i).getSide()).append("\t")
                        .append("下单时间: ").append(DateTimeUtils.ofTime(orders.get(i).getCTime() * 1000)).append("\t")
                        .append("开仓价格: ").append(orders.get(i).getPx());
            }
        }
        return sb.toString();
    }

}
