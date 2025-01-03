package ai.zzt.okx.common.event;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 事件类型
 *
 * @author zhouzhitong
 * @since 2024/5/4
 **/
@Getter
public enum EventType {

    // 系统启动
    SYSTEM_START(0, "系统启动"),
    // 数据存储
    DATA_STORE(1, "数据存储"),
    // 数据处理
    DATA_PROCESS(2, "数据处理"),

    PLACE_ORDER(3, "开仓"),
    CLOSE_ORDER(4, "平仓"),

    ;

    private final Integer code;

    @JsonValue
    private final String desc;

    EventType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String toString() {
        return desc;
    }

}
