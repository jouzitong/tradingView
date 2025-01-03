package ai.zzt.okx.data.service.impl;

import ai.zzt.okx.common.utils.K;
import ai.zzt.okx.common.utils.ThreadUtils;
import ai.zzt.okx.storage.service.KStoreService;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.Bar;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author zhouzhitong
 * @since 2024/5/4
 **/
@Service
@Slf4j
public class MarkPriceService {

    private final KStoreService kStoreService;

    private final BlockingQueue<List<MarkPrice>> queue = new LinkedBlockingQueue<>(100000);

    private volatile Boolean flag = false;

    private long count = 0;

    public MarkPriceService(KStoreService kStoreService) {
        this.kStoreService = kStoreService;
    }

    public void addAll(List<MarkPrice> mpList) {
        try {
            queue.put(mpList);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (!flag) {
            flag = true;
            log.info("开始异步存储数据");
            ThreadUtils.runIO(this::test);
        }
    }

    @SneakyThrows
    private void test() {
        Bar bar = Bar.s1;
        Map<String, K> kMap = new HashMap<>();
        while (true) {
            try {
                count++;
                if (count % 10000 == 0) {
                    log.debug("已经存储了: {} 次 秒级数据", count);
                }
                List<MarkPrice> mps = queue.take();
                for (MarkPrice mp : mps) {
                    K k = kMap.get(mp.getInstId());
                    if (k == null) {
                        k = new K(mp.getMarkPx(), bar, mp.getTs());
                        kMap.put(mp.getInstId(), k);
                    } else {
                        if (!k.update(mp.getMarkPx(), mp.getTs())) {
                            // 更新失败, 需要新增一个, 现在这个就存储到磁盘中
                            kStoreService.store(mp.getInstId(), bar, k);
                            k = new K(mp.getMarkPx(), bar, mp.getTs());
                            kMap.put(mp.getInstId(), k);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("数据存储异常: ", e);
            }
        }

    }

}
