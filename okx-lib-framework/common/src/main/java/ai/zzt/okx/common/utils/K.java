package ai.zzt.okx.common.utils;

import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 在某个时间单位中, 价格区间
 *
 * @author zhouzhitong
 * @since 2024/5/6
 **/
@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class K {

    /**
     * 当前值. 最后也是收盘价
     */
    private BigDecimal c;

    /**
     * 开始值
     */
    private BigDecimal o;

    /**
     * 最小值
     */
    private BigDecimal l;

    /**
     * 最大值
     */
    private BigDecimal h;

    /**
     * 时间单位区间
     */
    private Bar bar;

    /**
     * 开盘时间. (过滤掉多余时间的)
     */
    private long ts;

    /**
     * 当前时间.
     */
    private long curTs;

    /**
     * 最低最高价时间
     */
    private long lTs;

    /**
     * 最高价时间
     */
    private long hTs;

    /**
     * 成交量
     */
    private long confirm;

    @Deprecated // 这个字段没有用
    private boolean end = false;

    public K(BigDecimal c, Bar bar, long ts) {
        this.curTs = ts;
        this.lTs = hTs = ts;
        this.c = this.l = this.h = this.o = c;
        this.bar = bar;
        this.ts = bar.suffix(ts); // 已经同步了
    }

    public synchronized boolean update(BigDecimal val, long ts) {
        this.curTs = ts;
        if (ts - this.ts > bar.toMillis()) {
            end = true;
            return false;
        }
        if (c == null) {
            c = l = h = val;
        } else {
            this.c = val;
            // val 是否小于 minVal
            if (l.compareTo(val) > 0) {
                l = val;
                lTs = ts;
            } else if (h.compareTo(val) < 0) {
                h = val;
                hTs = ts;
            }
        }
        return true;
    }

    public BigDecimal get() {
        return c;
    }

    public void setTs(long ts) {
        this.curTs = this.ts = ts;
    }

    public List<MarkPrice> toMp(String instId, Bar bar) {
        // 实现将 K 线数据转换为 MarkPrice 的逻辑
        long scale = bar.getScale();
        long splitScale = scale / 4;
        long ts = bar.suffix(this.ts);
        boolean up = o.compareTo(c) < 0;
        List<MarkPrice> res = new ArrayList<>();
        res.add(new MarkPrice(null, instId, o, ts));

        // 判断 l == h
        if (l.compareTo(h) == 0) {
            // 如果 l == h 说明当时没有发生变化
            return res;
        }

        if (up) {
            ts += splitScale;
            res.add(new MarkPrice(null, instId, l, ts));
            ts += splitScale;
            res.add(new MarkPrice(null, instId, h, ts));
        } else {
            ts += splitScale;
            res.add(new MarkPrice(null, instId, h, ts));
            ts += splitScale;
            res.add(new MarkPrice(null, instId, l, ts));
        }
        ts += splitScale;
        res.add(new MarkPrice(null, instId, c, ts));
        return res;
    }

    public long getOpenTs() {
        return ts;
    }

    public long getTs() {
        return curTs;
    }

    public static K parse(List<String> data) {
        K k = new K();
        k.setTs(Long.parseLong(data.get(0)));
        k.setO(new BigDecimal(data.get(1)));
        k.setH(new BigDecimal(data.get(2)));
        k.setL(new BigDecimal(data.get(3)));
        k.setC(new BigDecimal(data.get(4)));
        k.setConfirm(Long.parseLong(data.get(5)));
        return k;
    }

    @Override
    public String toString() {
        /*
            开盘时间: {ts} ({涨跌幅度})
            开盘价: {o}
            收盘价: {c}
            最低价: {l} ( {lTs} )
            最高价: {h} ( {lTs} )
         */
        StringBuffer sb = new StringBuffer();
        sb.append("开盘时间: ").append(DateTimeUtils.ofTime(ts))
                .append("(")
                .append(c.subtract(o).multiply(BigDecimal.valueOf(100)).divide(o, 2, RoundingMode.DOWN))
                .append("%")
                .append(")")
                .append("\n");
        sb.append("开盘价: ").append(o).append("\n");
        sb.append("收盘价: ").append(c).append("\n");
        sb.append("最低价: ").append(l)
                .append("( ").append(DateTimeUtils.ofTime(lTs)).append(" )")
                .append("\n");
        sb.append("最高价: ").append(h)
                .append("( ").append(DateTimeUtils.ofTime(hTs)).append(" )")
                .append("\n");
        return sb.toString();
    }

    public static void main(String[] args) {
        // 获取当前时间分钟
        long currentTime = System.currentTimeMillis();
    }

}
