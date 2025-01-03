package ai.zzt.okx.data.service.impl;

import ai.zzt.okx.common.context.SystemContext;
import ai.zzt.okx.common.utils.DateTimeUtils;
import ai.zzt.okx.common.utils.ThreadUtils;
import ai.zzt.okx.data.service.IKLineCollectionService;
import ai.zzt.okx.okx_client.properties.OkxProperties;
import ai.zzt.okx.okx_client.serivce.OkxKLineApiService;
import ai.zzt.okx.storage.service.KStoreService;
import ai.zzt.okx.v5.enumeration.Bar;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouzhitong
 * @since 2024/10/3
 **/
@Service
@Slf4j
public class KLineCollectionService implements IKLineCollectionService {

    @Resource
    private OkxKLineApiService okxRestApiService;

    @Resource
    private KStoreService kStoreService;

    @Resource
    private OkxProperties okxProperties;

    // 存储正在处理的 instId 和 bar 组合
    private final ConcurrentHashMap<String, Boolean> processingMap = new ConcurrentHashMap<>();

    // 每个月一号早上8点执行
    @Scheduled(cron = "0 0 2 1 * ?")
    public void runScheduled() {
        if (okxProperties.isEnableScheduledCollectHistoryData()) {
            collectHistoryData();
        }
    }

    @Override
    public void collectHistoryData() {
        Set<String> hotInstIdList = okxProperties.getHotInstIdList();
        for (String instId : hotInstIdList) {
            ThreadUtils.runIO(() -> collectHistoryData(instId, Bar.M_1));
        }
    }

    @Override
    public void collectHistoryData(String instId, Bar bar) {
        String key = instId + "_" + bar.toString();
        // 检查是否已经在处理中
        if (processingMap.putIfAbsent(key, Boolean.TRUE) != null) {
            return;
        }
        try {
            log.info("开始收集数据: {} - {}", instId, bar);
            // 删除最新一个月的数据，是为了确保当前月份的数据全都在
            kStoreService.deleteLastMonth(instId, bar);
            List<String> list = kStoreService.getList(instId, bar);
            String lastFileName;
            if (list.isEmpty()) {
                okxRestApiService.saveAll(instId, InstrumentType.SWAP, bar);
                return;
            } else {
                lastFileName = list.getLast();
            }
            long before = DateTimeUtils.parseAndNextMonth(lastFileName);
            long after = SystemContext.currentTimeMillis();
            log.info("开始收集 {} - {} 数据: from: {} to {}", instId, bar, DateTimeUtils.ofTime(before), DateTimeUtils.ofTime(after));
            if (after == before) {
                log.warn("{} 数据已经是最新的了", instId);
                return;
            }
            okxRestApiService.save(instId, bar, after, before);
        } finally {
            // 处理完成后移除标记
            processingMap.remove(key);
        }
    }
}
