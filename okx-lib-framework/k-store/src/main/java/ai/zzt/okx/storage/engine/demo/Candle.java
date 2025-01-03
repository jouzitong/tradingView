package ai.zzt.okx.storage.engine.demo;

import lombok.Data;

/**
 * @author zhouzhitong
 * @since 2024/11/2
 **/
@Data
public class Candle {
    private long timestamp; // 时间戳
    private double open;    // 开盘价
    private double close;   // 收盘价
    private double high;    // 最高价
    private double low;     // 最低价

    public Candle(long timestamp, double open, double close, double high, double low) {
        this.timestamp = timestamp;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
    }


    // 构造函数、getter和setter
}
