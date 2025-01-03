package ai.zzt.okx.v5.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum CtType {

    /**
     * 正向合约
     */
    LINEAR("linear"),
    /**
     * 反向合约
     */
    INVERSE("inverse");

    private final String value;

    CtType(final String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

}
