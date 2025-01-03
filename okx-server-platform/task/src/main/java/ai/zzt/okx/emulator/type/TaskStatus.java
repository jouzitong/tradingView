package ai.zzt.okx.emulator.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author zhouzhitong
 * @since 2024/7/9
 **/
@Getter
public enum TaskStatus {

    // 任务状态: 0-新建, 1-执行中, 2-执行成功, 3-执行失败, 4-已取消
    NEW(0, "待执行"),
    RUNNING(1, "执行中"),
    SUCCESS(2, "执行成功"),
    FAILED(3, "执行失败"),
    CANCELLED(4, "已取消"),
    ;

    @JsonValue
    private final int code;

    private final String name;

    TaskStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @JsonCreator
    public static TaskStatus of(int code){
        for (TaskStatus value : values()) {
            if (value.code==code){
                return value;
            }
        }
        return null;
    }

}
