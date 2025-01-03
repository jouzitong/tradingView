package ai.zzt.okx.common.constant;

import ai.zzt.okx.common.base.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * WebSocket 操作码.
 * <ol>
 *     <li>1: 开头表示请求</li>
 *     <li>2: 开头表示响应</li>
 *     <li>3: 开头表示推送数据(一般指后台给前端推送数据)</li>
 * </ol>
 *
 * @author zhouzhitong
 * @since 2024/7/30
 **/
@Getter
public enum WsOpCode implements IEnum {

    @Deprecated
    CREATE_REQUEST(1, "【请求】ws 创建请求"),
    CLOSE_REQUEST(-1, "【请求】ws 关闭请求"),

    K_LINE_REQUEST_ALL(1_002, "【请求】ws k线数据请求全量数据"),
    REQUEST_SUB(1_003, "【请求】ws 数据订阅"),
    REQUEST_PUSH(1_004, "【请求】推送数据"),


    K_LINE_RESPONSE_SUB(2_003, "【响应】ws k线数据订阅成功"),


    K_LINE_RESPONSE_PUSH(3_004, "【响应-推送】ws k线数据推送"),
    ;

    @JsonValue
    private final int code;

    private final String name;

    WsOpCode(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static WsOpCode of(int code) {
        for (WsOpCode e : WsOpCode.values()) {
            if (code == e.code) {
                return e;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return STR."\{code}-\{name}";
    }
}
