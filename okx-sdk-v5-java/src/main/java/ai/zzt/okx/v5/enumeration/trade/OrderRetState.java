package ai.zzt.okx.v5.enumeration.trade;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 策略交易的返回的订单枚举
 *
 * @author liuxiaokun
 */
@Getter
public enum OrderRetState {

    /**
     * 待生效
     */
    LIVE("live"),

    /**
     * 暂停生效
     */
    PAUSE("pause"),
    /**
     * 部分生效
     */
    PARTIALLY_EFFECTIVE("partially_effective"),
    /**
     * 已生效
     */
    EFFECTIVE("effective"),
    /**
     * 已撤销
     */
    CANCELED("canceled"),
    /**
     * 委托失败
     */
    ORDER_FAILED("order_failed"),
    /**
     * 部分生效
     */
    PARTIALLY_FAILED("partially_failed"),
    ;


    private final String value;

    OrderRetState(final String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

}
