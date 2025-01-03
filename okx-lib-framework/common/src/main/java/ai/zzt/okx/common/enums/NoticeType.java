package ai.zzt.okx.common.enums;

import lombok.Getter;

/**
 * @author zhouzhitong
 * @since 2024/5/5
 **/
@Getter
public enum NoticeType {

    email("email"),     // 邮件
    ;

    private final String type;

    NoticeType(String type) {
        this.type = type;
    }
}
