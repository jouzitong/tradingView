package ai.zzt.okx.message.wechat.service;

import ai.zzt.okx.common.enums.NoticeType;
import ai.zzt.okx.common.exception.TodoRuntimeException;
import ai.zzt.okx.message.base.Message;
import ai.zzt.okx.message.service.INoticeService;
import ai.zzt.okx.message.wechat.bean.IWeChatVO;
import ai.zzt.okx.message.wechat.bean.MarkdownMsgVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhouzhitong
 * @since 2025/1/3
 **/
@Service
@Slf4j
public class WeChatService implements INoticeService {

    @Resource
    private RestTemplate restTemplate;

    private static final HttpHeaders headers;

    static {
        // 设置请求头，指定 Content-Type 为 JSON
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }


    @Override
    public NoticeType type() {
        return NoticeType.WE_CHAT;
    }

    @Override
    public void sendMessage(Message message, String receiver) {
        if (message instanceof IWeChatVO) {
            send((IWeChatVO) message);
        } else {
            log.error("暂不支持的消息类型: {}", message);
            throw new TodoRuntimeException("暂不支持的消息类型");
        }
    }

    @Override
    public void sendMessage(String id, Message message, String receiver, long interval) {

    }

    private void send(IWeChatVO weChat) {
// 企业微信群聊机器人的 Webhook 地址
        String webhookUrl = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=824a1e8f-ef8d-428e-8ed4-2d05bd7c29c9";

        // 构建消息体，这里以发送文本消息为例
//        TextMsgVO msg = new TextMsgVO("这是一条来自企业微信群聊机器人的消息23");
        // 构建一个 markdown 消息
        MarkdownMsgVO msg = new MarkdownMsgVO("# 这是一个 Markdown 消息\n" +
                "这是 Markdown 消息的内容");


        // 创建 HttpEntity，将消息体和请求头封装在一起
        HttpEntity<?> requestEntity = new HttpEntity<>(msg, headers);

        // 发送 POST 请求
        String response = restTemplate.postForObject(webhookUrl, requestEntity, String.class);
        // 处理响应
        if (response != null) {
            log.info("发送成功：{}", response);
        } else {
            log.error("发送失败");
        }
    }

    //    @PostConstruct
    public void send() {
        // 企业微信群聊机器人的 Webhook 地址
        String webhookUrl = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=824a1e8f-ef8d-428e-8ed4-2d05bd7c29c9";

        // 构建消息体，这里以发送文本消息为例
//        TextMsgVO msg = new TextMsgVO("这是一条来自企业微信群聊机器人的消息23");
        // 构建一个 markdown 消息
        MarkdownMsgVO msg = new MarkdownMsgVO("# 这是一个 Markdown 消息\n" +
                "这是 Markdown 消息的内容");


        // 创建 HttpEntity，将消息体和请求头封装在一起
        HttpEntity<?> requestEntity = new HttpEntity<>(msg, headers);

        // 发送 POST 请求
        String response = restTemplate.postForObject(webhookUrl, requestEntity, String.class);
        // 处理响应
        if (response != null) {
            log.info("发送成功：{}", response);
        } else {
            log.error("发送失败");
        }
    }

}
