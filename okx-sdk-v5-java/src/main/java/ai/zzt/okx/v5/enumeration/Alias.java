package ai.zzt.okx.v5.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 仅适用于交割
 * 不建议使用，用户应通过 expTime 字段获取合约的交割日期
 */
@Getter
public enum Alias {

    /**
     * 本周
     */
    THIS_WEEK("this_week"),
    /**
     * 次周
     */
    NEXT_WEEK("next_week"),
    /**
     * 本月
     */
    THIS_MONTH("this_month"),
    /**
     * 次月
     */
    NEXT_MONTH("next_month"),
    /**
     * 季度
     */
    QUARTER("quarter"),
    /**
     * 次季度
     */
    NEXT_QUARTER("next_quarter");


    private final String value;

    Alias(final String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

}
