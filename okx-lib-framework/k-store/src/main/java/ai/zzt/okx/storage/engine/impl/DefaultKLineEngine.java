package ai.zzt.okx.storage.engine.impl;

import ai.zzt.okx.common.base.system.Initialization;
import ai.zzt.okx.common.exception.TodoRuntimeException;
import ai.zzt.okx.common.utils.InstUtils;
import ai.zzt.okx.common.utils.K;
import ai.zzt.okx.common.utils.SlideList;
import ai.zzt.okx.common.utils.ThreadUtils;
import ai.zzt.okx.storage.config.KStoreProperties;
import ai.zzt.okx.storage.engine.IKLineEngine;
import ai.zzt.okx.storage.engine.param.ScrollParams;
import ai.zzt.okx.storage.engine.utis.KList;
import ai.zzt.okx.storage.service.KStoreService;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouzhitong
 * @since 2024/11/2
 **/
@Slf4j
@Service
public class DefaultKLineEngine implements IKLineEngine, Initialization {

    /**
     * FIXME 这个数据结构缺陷, 无法查询到太久远的数据
     */
    private final Map<String, Map<Bar, KList>> instKLineMap;

    private final Map<String, Map<Bar, BlockingDeque<K>>> storableKLineMap;

    private final Map<String, SlideList<MarkPrice>> instKMap;

    private final KStoreProperties kStoreProperties;

    private final KStoreService kStoreService;

    public DefaultKLineEngine(KStoreProperties kStoreProperties, KStoreService kStoreService) {
        this.kStoreProperties = kStoreProperties;
        this.kStoreService = kStoreService;
        this.instKLineMap = new ConcurrentHashMap<>();
        this.instKMap = new ConcurrentHashMap<>();
        if (kStoreProperties.isStorageAble()) {
            this.storableKLineMap = new ConcurrentHashMap<>();
        } else {
            this.storableKLineMap = null;
        }
    }

    @Override
    public void addMp(MarkPrice mp) {
        log.trace("添加标记价格: {}", mp);
        String instId = InstUtils.getBase(mp.getInstId());
        instKMap.computeIfAbsent(instId,
                        _ -> new SlideList<>(kStoreProperties.getPeriod()))
                .add(mp);
//
//        Map<Bar, KList> barKListMap = instKLineMap.computeIfAbsent(instId, _ -> new ConcurrentHashMap<>());
//        synchronized (SyncUtils.sync(instId, SyncType.MARKET_DATA)) {
//            Set<Bar> supportBars = kStoreProperties.getSupportBars();
//            for (Bar bar : supportBars) {
//                KList kList = barKListMap.computeIfAbsent(bar, _ -> new KList(kStoreProperties.getPeriod(), bar));
//                K k = kList.add(mp);
//                if (kStoreProperties.isStorageAble() && k != null
//                        && kStoreProperties.getStorageBars().contains(bar)) {
//                    Map<Bar, BlockingDeque<K>> map = storableKLineMap.computeIfAbsent(instId, k1 -> new ConcurrentHashMap<>());
//                    map.computeIfAbsent(bar, _ -> new LinkedBlockingDeque<>(kStoreProperties.getPeriod())).push(k);
//                }
//            }
//        }
    }

    @Override
    public BigDecimal getMaxDiffRate(String instId, long period) {
        instId = InstUtils.getBase(instId);
        SlideList<MarkPrice> markPrices = instKMap.get(instId);

        BigDecimal maxPx = markPrices.getLast().getMarkPx();
        BigDecimal minPx = maxPx;
        long ts = markPrices.getLast().getTs();

        for (MarkPrice mp : markPrices) {
            if (mp.getTs() < ts - period) {
                break;
            }
            BigDecimal px = mp.getMarkPx();
            if (px.compareTo(maxPx) > 0) {
                maxPx = px;
            }
            if (px.compareTo(minPx) < 0) {
                minPx = px;
            }
        }
        return maxPx.subtract(minPx).divide(minPx, 4, RoundingMode.HALF_UP);
    }

    @Override
    public List<K> getKLines(ScrollParams params) {
        String instId = params.getInstId();
        instId = InstUtils.getBase(instId);
        Bar bar = params.getBar();
        log.trace("获取K线数据: params: {}", params);
        KList kList = getKList(instId, bar);
        if (kList == null) {
            return List.of();
        }
        return kStoreService.list(params.getSt(), params.getEt());
    }

    @Override
    public K getLastK(String id, Bar bar) {
        KList kList = getKList(id, bar);
        if (kList == null) {
            log.error("未知异常. 找不到 {} 的 {} 数据", id, bar);
            throw new TodoRuntimeException();
        }
        return kList.getLast();
    }

    protected KList getKList(String instId, Bar bar) {
        Map<Bar, KList> barKListMap = instKLineMap.get(instId);
        if (barKListMap == null) {
            return null;
        }
        KList kList = barKListMap.get(bar);
        if (kList == null) {
            log.warn("找不到 {} 的 {} 数据", instId, bar);
            return null;
        }
        return kList;
    }

    @Override
    public void flush() {
        if (!kStoreProperties.isStorageAble()) {
            return;
        }
        for (String instId : storableKLineMap.keySet()) {
            Map<Bar, BlockingDeque<K>> barBlockingDequeMap = storableKLineMap.get(instId);
            if (barBlockingDequeMap == null) {
                continue;
            }
            for (Bar bar : kStoreProperties.getStorageBars()) {
                BlockingDeque<K> ks = barBlockingDequeMap.get(bar);
                if (CollectionUtils.isEmpty(ks)) {
                    continue;
                }
                K k = ks.poll();
                if (k == null) {
                    continue;
                }
                kStoreService.store(instId, bar, k);
            }
        }

    }

    @Override
    public void initialize() {
        if (!kStoreProperties.isStorageAble()) {
            return;
        }
        ThreadUtils.submitScheduled(this::flush, 30, 30, TimeUnit.SECONDS);
    }
}
