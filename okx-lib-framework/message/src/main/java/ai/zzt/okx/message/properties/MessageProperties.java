package ai.zzt.okx.message.properties;

import ai.zzt.okx.common.enums.NoticeType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhouzhitong
 * @since 2025/1/4
 **/
@Component
@Data
@ConfigurationProperties(prefix = "message")
public class MessageProperties {

    /**
     * 消息类型
     */
    private NoticeType type;

}
