package ai.zzt.okx.message.wechat.bean;

import ai.zzt.okx.message.wechat.type.WeChatMsgType;
import lombok.Getter;

/**
 * @author zhouzhitong
 * @since 2025/1/3
 **/
@Getter
public class TextMsgVO implements IWeChatVO {

    private final Text text;

    public TextMsgVO(String text) {
        this.text = new Text(text);
    }

    @Override
    public WeChatMsgType getMsgtype() {
        return WeChatMsgType.TEXT;
    }

    public static class Text {
        private String content;

        public Text(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}
