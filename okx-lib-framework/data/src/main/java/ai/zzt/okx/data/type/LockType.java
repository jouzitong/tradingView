package ai.zzt.okx.data.type;

import ai.zzt.okx.common.base.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author zhouzhitong
 * @since 2024/10/20
 **/
@Getter
public enum LockType implements IEnum {

    TASK_GROUP(10, "task_group"),
    TASK(10, "task"),

    ;

    @JsonValue
    private final int code;

    private final String name;

    LockType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
