package ai.zzt.okx.common.message.bean;

import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zhouzhitong
 * @since 2024/11/5
 **/
@Setter
@NoArgsConstructor
public class DefaultMessage implements Message {

    private String id;

    private String personal;

    private String subject;

    private String context;

    public DefaultMessage(String id, String personal, String subject, String context) {
        this.id = id;
        this.personal = personal;
        this.subject = subject;
        this.context = context;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String personal() {
        return personal;
    }

    @Override
    public String subject() {
        return subject;
    }

    @Override
    public String content() {
        return context;
    }

}
