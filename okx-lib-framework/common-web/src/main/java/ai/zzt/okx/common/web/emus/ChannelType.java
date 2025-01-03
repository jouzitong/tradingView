package ai.zzt.okx.common.web.emus;

import ai.zzt.okx.common.base.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author zhouzhitong
 * @since 2024/10/31
 **/
@Getter
public enum ChannelType implements IEnum {

    /**
     * 实时数据推送
     */
    TICKERS(1, "tickers"),

    /**
     * 实时K线数据推送
     */
    K_LINE(2, "kline"),

    /**
     * 订单数据
     */
    ORDER(3, "order"),

    /**
     * 交易量
     */
    VOLUME(4, "volume"),


    /**
     * 模拟任务
     */
    emulator(99, "emulator"),

    ;

    private final int code;

    @JsonValue
    private final String name;

    ChannelType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ChannelType of(String name) {
        for (ChannelType c : values()) {
            if (c.name.equals(name)) {
                return c;
            }
        }
        return null;
    }

}
