package ai.zzt.okx.storage.engine.utis;

import ai.zzt.okx.common.utils.K;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.TreeMap;

/**
 * @author zhouzhitong
 * @since 2024/11/2
 **/
@Getter
public class KList {

    private final TreeMap<Long, K> candleMap;

    private final Bar bar;

    private final int period;

    public KList(int period, Bar bar) {
        this.period = period;
        this.bar = bar;
        this.candleMap = new TreeMap<>();
    }

    /**
     * 新增/更新K线数据
     *
     * @param mp 市场价格
     * @return 新增成功 返回之前一个K. 如果是更新, 返回 null
     */
    public K add(MarkPrice mp) {
        BigDecimal markPx = mp.getMarkPx();
        Long ts = mp.getTs();
        K k = null;
        if (candleMap.isEmpty()) {
            candleMap.put(ts, new K(markPx, bar, ts));
        } else {
            if (!candleMap.get(candleMap.lastKey()).update(markPx, ts)) {
                k = candleMap.get(candleMap.lastKey());
                candleMap.put(ts, new K(markPx, bar, ts));
                if (candleMap.size() > period) {
                    // 弹出最旧的K线数据
                    candleMap.pollFirstEntry();
                }
            }
        }
        return k;
    }

    /**
     * 获取最新K线数据
     *
     * @return 最新K线数据
     */
    public K getLast() {
        return candleMap.get(candleMap.lastKey());
    }

    public int size() {
        return candleMap.size();
    }

}
