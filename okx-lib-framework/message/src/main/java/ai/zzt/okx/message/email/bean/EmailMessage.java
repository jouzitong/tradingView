package ai.zzt.okx.message.email.bean;

import ai.zzt.okx.message.base.Message;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author zhouzhitong
 * @since 2025/1/4
 **/
public interface EmailMessage extends Message {

    /**
     * 消息的唯一性标识
     *
     * @return 消息的唯一性标识
     */
    String id();

    /**
     * 邮箱名称
     *
     * @return 邮箱名称
     */
    default String personal() {
        return null;
    }

    /**
     * 邮箱标题
     *
     * @return 邮箱标题
     */
    default String subject() {
        return null;
    }

    /**
     * 发送内容
     *
     * @return 发送内容
     */
    String content();

    /**
     * 是否是html 文档格式
     *
     * @return 是否是html 文档格式. 默认 true
     */
    @JsonIgnore
    default boolean isHtml() {
        return true;
    }

}
