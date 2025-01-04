package ai.zzt.okx.data.service.impl;

import ai.zzt.okx.calculate.indicator.face.BollFace;
import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.common.enums.TrendType;
import ai.zzt.okx.common.properties.AppProperties;
import ai.zzt.okx.common.utils.K;
import ai.zzt.okx.message.email.bean.EmailMessage;
import ai.zzt.okx.message.service.INoticeService;
import ai.zzt.okx.okx_client.properties.OkxProperties;
import ai.zzt.okx.okx_client.serivce.OkxKLineApiService;
import ai.zzt.okx.settings.calculate.face.BollSettingsFace;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.settings.service.ISettingsService;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.Bar;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 定时任务
 *
 * @author zhouzhitong
 * @since 2024/10/6
 **/
@Service
@Slf4j
public class ScheduledTaskService {

    @Resource
    private ISettingsService settingsService;

    @Resource
    private OkxProperties okxProperties;

    @Resource
    private AppProperties appProperties;

    @Resource
    private INoticeService noticeService;

    @Resource
    private OkxKLineApiService okxRestApiService;

    // 每半个小时运行一次
    @Scheduled(cron = "0 0,30 * * * ?")
    public void runTask() {
        List<String> receivers = appProperties.getMail().getReceivers();
        if (CollectionUtils.isEmpty(receivers)) {
            log.info("没有配置邮箱接收人, 无法发送邮件.");
            return;
        }
        Set<String> hotInstIdList = okxProperties.getHotInstIdList();
        Map<String, TrendType> trendTypeMap = new HashMap<>();
        for (String instId : hotInstIdList) {
            TrendType trendType = doAnalyze(instId);
            trendTypeMap.put(instId, trendType);
        }
        List<String> upList = new ArrayList<>();
        List<String> downList = new ArrayList<>();

        trendTypeMap.forEach((instId, trendType) -> {
            if (trendType == TrendType.UP) {
                upList.add(instId);
            } else if (trendType == TrendType.DOWN) {
                downList.add(instId);
            }
        });

        // TODO 邮件发送, 应该还要支持 wechat 等
        EmailMessage message = new EmailMessage() {
            @Override
            public String id() {
                return "Okx 币种涨跌";
            }

            @Override
            public String content() {
                return "币种涨:  " + upList + " </p> 跌的币种: " + downList;
            }
        };

        noticeService.sendMessage(message, receivers);
    }

    private TrendType doAnalyze(String instId) {
        SettingsContext settings = settingsService.getSettings(instId);
        Bar bar = Bar.H_1;
        List<K> ks = okxRestApiService.getKLines(instId, bar);
        BollFace bollFace = new BollFace((BollSettingsFace) settings.getCalculateSettingsFaceMap().get(IndicatorType.BOLL));
        for (K kLine : ks) {
            MarkPrice mp = new MarkPrice(null, instId, kLine.getC(), kLine.getTs());
            bollFace.add(mp);
        }
        return bollFace.getTrend(bar);
    }

}
