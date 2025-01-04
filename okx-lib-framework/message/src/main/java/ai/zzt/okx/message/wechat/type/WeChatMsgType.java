package ai.zzt.okx.message.wechat.type;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author zhouzhitong
 * @since 2025/1/3
 **/
@Getter
public enum WeChatMsgType {

    TEXT("text"),
    MARKDOWN("markdown"),
    ;

    @JsonValue
    private final String name;

    WeChatMsgType(String name) {
        this.name = name;
    }
}
