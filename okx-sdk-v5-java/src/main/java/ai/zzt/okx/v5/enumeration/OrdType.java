package ai.zzt.okx.v5.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <a href="https://www.okx.com/docs-v5/zh/?shell#order-book-trading-algo-trading-post-place-algo-order">策略下单类型</a>
 *
 * @author zhouzhitong
 * @since 2024/5/9
 **/
public enum OrdType {

    /**
     * 单向止盈止损
     */
    CONDITIONAL("conditional"),
    /**
     * 双向止盈止损
     */
    OCO("oco"),

    /**
     * 计划委托
     */
    TRIGGER("trigger"),

    /**
     * 移动止盈止损
     */
    MOVE_ORDER_STOP("move_order_stop"),

    /**
     * 时间加权委托
     */
    TWAP("twap"),

    /**
     * 限价单
     */
    LIMIT("limit"),

    ;

    private final String value;

    OrdType(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

}
