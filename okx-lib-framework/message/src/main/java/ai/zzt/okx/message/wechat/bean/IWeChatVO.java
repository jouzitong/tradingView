package ai.zzt.okx.message.wechat.bean;


import ai.zzt.okx.message.base.Message;
import ai.zzt.okx.message.wechat.type.WeChatMsgType;

/**
 * @author zhouzhitong
 * @since 2025/1/3
 **/
public interface IWeChatVO extends Message {

    /**
     * 消息类型
     *
     * @return
     */
    WeChatMsgType getMsgtype();

}
