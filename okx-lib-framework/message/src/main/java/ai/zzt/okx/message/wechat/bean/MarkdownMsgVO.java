package ai.zzt.okx.message.wechat.bean;

import ai.zzt.okx.message.wechat.type.WeChatMsgType;
import lombok.Getter;

/**
 * @author zhouzhitong
 * @since 2025/1/3
 **/
@Getter
public class MarkdownMsgVO implements IWeChatVO {

    private final Markdown markdown;

    public MarkdownMsgVO(String content) {
        this.markdown = new Markdown(content);
    }

    @Override
    public WeChatMsgType getMsgtype() {
        return WeChatMsgType.MARKDOWN;
    }

    public static class Markdown {
        private String content;

        public Markdown(String content) {
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
