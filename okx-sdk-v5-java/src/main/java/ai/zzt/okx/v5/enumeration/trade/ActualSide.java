package ai.zzt.okx.v5.enumeration.trade;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 实际触发方向
 *
 * 仅适用于单向止盈止损委托和双向止盈止损委托
 * @author liuxiaokun
 */
@Getter
public enum ActualSide {

    /**
     * 止盈
     */
    TP("tp"),

    /**
     * 止损
     */
    SL("sl"),
    ;


    private final String value;

    ActualSide(final String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

}
