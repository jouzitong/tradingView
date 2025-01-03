package ai.zzt.okx.v5.enumeration.trade;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 委托数量的类型
 * base_ccy: 交易货币 ；quote_ccy：计价货币
 * 仅适用于币币单向止盈止损市价买单
 * todo 默认买为计价货币，卖为交易货币
 */
@Getter
public enum TgtCcyEnum {
    BASE_CCY("base_ccy", "交易货币"),
    QUOTE_CCY("quote_ccy", "计价货币"),
    ;

    private final String value;
    private final String name;

    TgtCcyEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public String value() {
        return value;
    }

    public static TgtCcyEnum fromValue(final String value) {
        for (TgtCcyEnum tgtCcyEnum : TgtCcyEnum.values()) {
            if (tgtCcyEnum.value.equals(value)) {
                return tgtCcyEnum;
            }
        }
        return null;
    }
}
