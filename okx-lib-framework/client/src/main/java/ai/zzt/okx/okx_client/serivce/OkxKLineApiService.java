package ai.zzt.okx.okx_client.serivce;

import ai.zzt.okx.common.context.SystemContext;
import ai.zzt.okx.common.utils.DateTimeUtils;
import ai.zzt.okx.common.utils.K;
import ai.zzt.okx.common.utils.ThreadUtils;
import ai.zzt.okx.storage.service.KStoreService;
import ai.zzt.okx.v5.api.factory.OkxRestApiFactory;
import ai.zzt.okx.v5.api.pub.OpenDataApi;
import ai.zzt.okx.v5.entity.rest.R;
import ai.zzt.okx.v5.entity.rest.RO;
import ai.zzt.okx.v5.entity.ws.pub.Instruments;
import ai.zzt.okx.v5.enumeration.Bar;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zhouzhitong
 * @since 2024/6/2
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class OkxKLineApiService {

    private final OpenDataApi openDataApi;

    private final KStoreService kStoreService;

    // TODO 简单封装了下 历史K线数据的获取. 后期可以根据需求进行扩展
    public List<K> getKLines(String instId, Long after, Long before, Bar bar) {
        RO<List<String>> listRO = OkxRestApiFactory.get(openDataApi.getHistoryMarkPriceCandles(instId, after, before, bar.getValue(), 100));
        List<List<String>> dataList = listRO.getData();
        if (dataList == null) {
            return null;
        }
        return dataList.stream().map(K::parse).collect(Collectors.toList());
    }

    /**
     * 获取最近100条K线数据
     *
     * @param instId 产品Id
     * @param bar    时间粒度
     * @return 最近100条K线数据
     */
    public List<K> getKLines(String instId, Bar bar) {
        int count = 0;
        while (true) {
            try {
                RO<List<String>> listRO = OkxRestApiFactory.get(openDataApi.getMarkPriceCandles(instId, null, null, bar.getValue(), 100));
                List<List<String>> dataList = listRO.getData();
                return dataList.stream().map(K::parse).collect(Collectors.toList());
            } catch (Exception e) {
                log.error("[getKLines] getKLines error, instId:{}, bar:{}, count:{}", instId, bar.getValue(), (count++));
                if (count > 3) {
                    return null;
                }
                ThreadUtils.sleep(1);
            }
        }
    }


    Map<String, Long> instIdTimeMap = new HashMap<>();

    /**
     * 获取下一个时间戳
     *
     * @param instId
     * @param bar
     * @param pageSize
     * @return
     */
    private Long getAfter(String instId, Bar bar, int pageSize) {
        //如果第一次请求，清除这个instId查询的痕迹
        if (pageSize == 0) {
            instIdTimeMap.remove(instId);
            return null;
        }
        if (instIdTimeMap.containsKey(instId)) {
            //默认是100，第一次减去间隔*(100*(pageSize-1)+99)
            return instIdTimeMap.get(instId) - bar.getScale() * (100 * (pageSize - 1) + 99);
        }
        return null;
    }

    /**
     * 分页获取数据
     *
     * @param instId   产品Id
     * @param bar      时间粒度
     * @param pageSize 页数
     * @return 最近100条K线数据
     */
    public List<K> getKLinesPage(String instId, Bar bar, int pageSize) {
        int count = 0;
        while (true) {
            try {
                Long after = getAfter(instId, bar, pageSize);
                RO<List<String>> listRO = OkxRestApiFactory.get(openDataApi.getMarkPriceCandles(instId, after, null, bar.getValue(), 100));
                List<List<String>> dataList = listRO.getData();
                if (pageSize == 0) {
                    instIdTimeMap.put(instId, Long.valueOf(dataList.get(0).get(0)));
                }
                return dataList.stream().map(K::parse).collect(Collectors.toList());
            } catch (Exception e) {
                log.error("[getKLinesPage] getKLines error, instId:{}, bar:{}, count:{}", instId, bar.getValue(), (count++));
                if (count > 10) {
                    return null;
                }
                ThreadUtils.sleep(1);
            }
        }
    }


    private Instruments queryInstruments(String instId, InstrumentType instrumentType) {
        R<Instruments> result = OkxRestApiFactory.get(openDataApi.getInstruments(instrumentType, instId));
        if (result.isOk()) {
            return result.getData().get(0);
        }
        return null;
    }

    /**
     * 保存所有的历史数据
     *
     * @param instId
     * @param instrumentType
     * @param bar
     */
    public void saveAll(String instId, InstrumentType instrumentType, @NotNull Bar bar) {
        Instruments instruments = queryInstruments(instId, instrumentType);
        if (Objects.nonNull(instruments)) {
            Long before = instruments.getListTime();
            long l = DateTimeUtils.ofSecond("2022-01");
            if (before < DateTimeUtils.ofSecond("2022-01")) {
                before = l;
            }

            // 获取当前月份第一天的时间戳
//            long firstDayOfMonth = DateTimeUtils.getFirstTimeOfMonth();
            long now = SystemContext.currentTimeMillis();
            save(instId, bar, now, before);
        } else {
            log.warn("instId :{} query failed", instId);
        }
    }

    public void save(String instId, Bar bar, long end, long before) {
        log.info("{} [{}] 数据收集: {} - {}", instId, bar, DateTimeUtils.ofTime(before), DateTimeUtils.ofTime(end));
        int count = 0;
        while (true) {
            try {
                long after = before + bar.getScale() * 100;
                if (before >= end) {
                    return;
                }
                if (after > end) {
                    after = end;
                }
                List<K> kLines = getKLines(instId, after, before, bar);
                if (CollectionUtils.isEmpty(kLines)) {
                    log.info("找不到数据了, {} 数据收集结束.", instId);
                    return;
                }
                log.debug("{} 数据收集: {} - {}", instId, DateTimeUtils.ofTime(kLines.getLast().getOpenTs()), DateTimeUtils.ofTime(kLines.getFirst().getOpenTs()));

                before = kLines.getFirst().getOpenTs();
                int size = kLines.size();
                for (int i = size - 1; i >= 0; i--) {
                    kStoreService.store(instId, bar, kLines.get(i));
                }
                //如果不是100，就认为已经结束了
                if (after >= System.currentTimeMillis()) {
                    break;
                }
            } catch (Exception e) {
                log.error("", e);
                log.error("getKLines error, instId:{}, count:{}", instId, (count++));
                if (count > 10) {
                    break;
                }
                ThreadUtils.sleep(1);
            }
        }
    }

}
