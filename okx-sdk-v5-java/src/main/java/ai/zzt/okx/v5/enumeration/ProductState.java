package ai.zzt.okx.v5.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ProductState {

    /**
     * 交易中
     */
    LIVE("live"),
    /**
     * 暂停中
     */
    SUSPEND("suspend"),
    /**
     * 预上线，如：交割和期权的新合约在 live 之前，会有 preopen 状态
     */
    PREOPEN("preopen"),
    /**
     * 测试中（测试产品，不可交易）
     */
    TEST("test");

    private final String value;

    ProductState(final String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

}
