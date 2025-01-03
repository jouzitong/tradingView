package ai.zzt.okx.data;

import ai.zzt.okx.common.base.system.Initialization;
import ai.zzt.okx.common.utils.ThreadUtils;
import ai.zzt.okx.data.dispatcher.CollectionDispatcher;
import ai.zzt.okx.okx_client.properties.OkxProperties;
import ai.zzt.okx.okx_client.serivce.OkxWsApiServiceWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhouzhitong
 * @since 2024/6/20
 **/
@Service
@Slf4j
@AllArgsConstructor
public class DataCollectionInitialization implements Initialization {

    private final OkxProperties okxProperties;

    private final OkxWsApiServiceWrapper okxWsApiService;

    private final CollectionDispatcher dispatcher;


    @Override
    public void initialize() {
        initSubInstId();
    }

    /**
     * 收集数据
     */
    private void initSubInstId() {
        Set<String> instIdList = okxProperties.getHotInstIdList();
        // 同步手续费问题
        log.info("开始同步数据: {}", instIdList);
        CountDownLatch cb = new CountDownLatch(instIdList.size());

        // 订阅市场价格
        for (String instId : instIdList) {
            ThreadUtils.runIO(() -> {
                try {
//                    dispatcher.add(instId);
                    okxWsApiService.subMarkPrice(instId);
                } catch (Exception e) {
                    log.error("", e);
                    throw new RuntimeException(e);
                } finally {
                    cb.countDown();
                }
            });
        }

        try {
            cb.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("数据收集完毕.....");
    }

    @Override
    public int order() {
        return Initialization.LOW;
    }
}
