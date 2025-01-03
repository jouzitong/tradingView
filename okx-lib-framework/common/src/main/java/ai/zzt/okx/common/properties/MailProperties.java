package ai.zzt.okx.common.properties;

import lombok.Data;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/7/28
 **/
@Data
public class MailProperties {

    /**
     * 是否开启邮箱通知功能. 默认开启
     */
    private boolean enable = false;

    /**
     * 发送时间间隔, 单位 s. 默认 30 s
     */
    private int sendInterval = 30;

    /**
     * 邮箱接收用户
     */
    private List<String> receivers;

}
