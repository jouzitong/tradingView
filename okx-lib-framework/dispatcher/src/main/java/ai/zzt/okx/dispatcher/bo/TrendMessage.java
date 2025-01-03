package ai.zzt.okx.dispatcher.bo;

import ai.zzt.okx.common.message.bean.Message;

/**
 * @author zhouzhitong
 * @since 2024/11/5
 **/
public class TrendMessage implements Message {
    public TrendMessage() {
    }

    @Override
    public String id() {
        return "Okx 币种涨跌";
    }

    @Override
    public String personal() {
        return "Okx 币种涨跌通知1";
    }

    @Override
    public String subject() {
        return "Okx 币种涨跌通知2";
    }

    @Override
    public String content() {
        return "Okx 币种涨跌通知3</p> <strong> %s</strong>！";
    }

    @Override
    public boolean isHtml() {
        return Message.super.isHtml();
    }
}
