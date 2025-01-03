package ai.zzt.okx.v5.enumeration.trade;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 *
 * 是否启用开仓价止损
 * 仅适用于分批止盈的止损订单
 * 0：不开启，默认值
 * 1：开启
 *
 * @author liuxiaokun
 */
@Getter
public enum AmendPxOnTriggerType {

    /**
     * 不开启，默认值
     */
    TP("0"),

    /**
     * 开启
     */
    SL("1"),
    ;


    private final String value;

    AmendPxOnTriggerType(final String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

}
