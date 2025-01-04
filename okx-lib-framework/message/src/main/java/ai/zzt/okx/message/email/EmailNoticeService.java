package ai.zzt.okx.message.email;

import ai.zzt.okx.common.bean.Throttler;
import ai.zzt.okx.common.enums.NoticeType;
import ai.zzt.okx.common.exception.TodoRuntimeException;
import ai.zzt.okx.message.base.Message;
import ai.zzt.okx.message.email.bean.EmailMessage;
import ai.zzt.okx.message.service.INoticeService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * 邮件的service
 */
@Service
@Slf4j
public class EmailNoticeService implements INoticeService {

    @Autowired(required = false)
    private JavaMailSender javaMailSender;

    // TODO 配置应该统一管理, 暂时放这里
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public NoticeType type() {
        return NoticeType.EMAIL;
    }

    @Override
    public void sendMessage(Message message, String receiver) {
        if (message instanceof EmailMessage emailMessage) {
            sendMessage(emailMessage, receiver);
        } else {
            throw new TodoRuntimeException("message type is not EmailMessage");
        }
    }


    private void sendMessage(EmailMessage message, String receiver) {
        // 创建一个邮件消息
        MimeMessage mess = javaMailSender.createMimeMessage();

        // 创建 MimeMessageHelper
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mess, false);

            // 发件人邮箱和名称
            helper.setFrom(from, message.personal());
            // 收件人邮箱
            helper.setTo(receiver);
            // 邮件标题
            helper.setSubject(message.subject());
            // 邮件正文，第二个参数表示是否是HTML正文
            helper.setText(message.content(), message.isHtml());
            // 发送
            javaMailSender.send(mess);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final Throttler throttler = new Throttler();

    @Override
    public void sendMessage(String id, Message message, String receiver, long interval) {
        if (throttler.check(id, interval)) {
            sendMessage(message, receiver);
        }
    }
}
