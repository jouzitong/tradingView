package ai.zzt.okx.message.service;

import ai.zzt.okx.common.enums.NoticeType;
import ai.zzt.okx.message.base.Message;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2025/1/4
 **/
public interface INoticeService {

    /**
     * 服务类型
     *
     * @return 通知类型
     */
    NoticeType type();

    void sendMessage(Message message, String receiver);

    default void sendMessage(Message message, List<String> receivers) {
        for (String receiver : receivers) {
            sendMessage(message, receiver);
        }
    }

    /**
     * 发送消息. 防止一时间多次发送重复消息
     *
     * @param id       唯一ID
     * @param message  消息
     * @param receiver 接收人
     * @param interval 间隔时间
     */
    void sendMessage(String id, Message message, String receiver, long interval);

}
