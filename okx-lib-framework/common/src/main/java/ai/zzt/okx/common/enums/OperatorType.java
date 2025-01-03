package ai.zzt.okx.common.enums;

import ai.zzt.okx.common.base.IEnum;
import lombok.Getter;

/**
 * @author zhouzhitong
 * @since 2024/12/12
 **/
@Getter
public enum OperatorType implements IEnum {

    AND(1, "与"),
    OR(2, "或"),
    NOT(3, "非"),;

    private final int code;

    private final String name;

    OperatorType(int code, String name) {
        this.code = code;
        this.name = name;
    }

}
