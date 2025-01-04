package ai.zzt.okx.dispatcher.service;

import ai.zzt.okx.common.enums.TrendType;
import ai.zzt.okx.common.properties.AppProperties;
import ai.zzt.okx.common.utils.InstUtils;
import ai.zzt.okx.common.utils.ThreadUtils;
import ai.zzt.okx.message.email.bean.DefaultMessage;
import ai.zzt.okx.message.service.INoticeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouzhitong
 * @since 2024/7/11
 **/
@Service
@Slf4j
public class EmailMessageService implements CommandLineRunner {

    @Resource
    private INoticeService noticeService;

    @Resource
    private AppProperties appProperties;

    private final Map<String, TrendType> trendTypeMap = new ConcurrentHashMap<>();

    /**
     * 记录最后通知时间, 避免频繁通知.
     */
    private final Map<String, Long> lastTimeMap = new ConcurrentHashMap<>();

    /**
     * 通知频率, 30分钟一次.
     */
    private final long t = 30 * 60 * 1000;

    public void addMessage(String instId, TrendType trendType) {
        instId = InstUtils.getBase(instId);
//        if (trendType == null || trendType == TrendType.FLUCTUATE) {
//            return;
//        }
        trendTypeMap.put(instId, trendType);
    }

    @Override
    public void run(String... args) throws Exception {
        if (appProperties.getMail().isEnable()) {
            int sleepTime = appProperties.getMail().getSendInterval();
            log.info("开启邮箱通知功能.");
            ThreadUtils.runTask(() -> {
                while (true) {
                    log.trace("trendTypeMap: {}", trendTypeMap);
                    if (trendTypeMap.isEmpty()) {
                        ThreadUtils.sleep(sleepTime);
                        continue;
                    }
                    List<String> upList = new ArrayList<>();
                    List<String> downList = new ArrayList<>();

                    trendTypeMap.forEach((instId, trendType) -> {
                        if (System.currentTimeMillis() - lastTimeMap.getOrDefault(instId + trendType, 0L) < t) {
                            return;
                        }
                        if (trendType == TrendType.UP) {
                            upList.add(instId);
                            lastTimeMap.put(instId + trendType, System.currentTimeMillis());
                        } else if (trendType == TrendType.DOWN) {
                            lastTimeMap.put(instId + trendType, System.currentTimeMillis());
                            downList.add(instId);
                        }
                    });
                    if (CollectionUtils.isEmpty(upList) && CollectionUtils.isEmpty(downList)) {
                        ThreadUtils.sleep(sleepTime);
                        continue;
                    }
                    try {
                        List<String> receivers = appProperties.getMail().getReceivers();
                        DefaultMessage message = new DefaultMessage(
                                "币种确实通知",
                                "币种趋势通知",
                                "涨: " + upList + ", 跌: " + downList,
                                "币种确实通知");

                        noticeService.sendMessage(message, receivers);
                    } catch (Exception e) {
                        log.error("邮箱通知异常: ", e);
                    }
                    // 等待一段时间, 再次发送通知
                    ThreadUtils.sleep(sleepTime);
                }
            });
        }
    }
}
