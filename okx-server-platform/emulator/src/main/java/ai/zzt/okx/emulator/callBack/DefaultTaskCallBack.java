package ai.zzt.okx.emulator.callBack;

import ai.zzt.okx.common.context.SystemContext;
import ai.zzt.okx.common.utils.ApplicationContextUtils;
import ai.zzt.okx.storage.config.KStoreProperties;
import ai.zzt.okx.emulator.serivce.EmulatorTaskManager;
import ai.zzt.okx.emulator.service.impl.EmulateTradeOrderEngine;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhouzhitong
 * @since 2024/7/2
 **/
@Slf4j
public class DefaultTaskCallBack implements TaskCallBack {

    private static final EmulateTradeOrderEngine tradeOrderEngine;

    private static final EmulatorTaskManager taskManager;

    private static final KStoreProperties kStoreProperties;

    static {
        tradeOrderEngine = ApplicationContextUtils.getBean(EmulateTradeOrderEngine.class, true);
        taskManager = ApplicationContextUtils.getBean(EmulatorTaskManager.class, true);
        kStoreProperties = ApplicationContextUtils.getBean(KStoreProperties.class, true);
    }

//    private final IKLineEngine kLineEngine;

    private final AtomicLong count = new AtomicLong(0L);

    @Getter
    private CustomerTaskDTO task;

    @Setter
    private BigDecimal lastProfit;

    private long startTime;

    public DefaultTaskCallBack(CustomerTaskDTO task) {
        this.task = task;
//        this.kLineEngine = new DefaultKLineEngine(kStoreProperties, null);
    }

    @Override
    public void run(List<MarkPrice> markPrices) {
        count.addAndGet(markPrices.size());
//        for (MarkPrice mp : markPrices) {
//            kLineEngine.addMp(mp);
//        }
        for (MarkPrice markPrice : markPrices) {
            tradeOrderEngine.dispatch(markPrice);
        }
    }

    @Override
    public void start() {
        startTime = SystemContext.currentTimeMillis();
        log.info("开始读取数据. task.getTaskId(): {}", task.getTaskId());
        taskManager.running(task.getTaskId());
    }

    @Override
    public void fail(Throwable e) {
        log.info("读取数据失败", e);
        taskManager.cancel(task.getTaskId());
    }

    @Override
    public void finish() {
        long end = SystemContext.currentTimeMillis();
        log.info("任务完成.task: {}, total: {}. 耗时: {} ms", task.getTaskId(), count.get(), end - startTime);
        taskManager.finish(task.getTaskId());

    }

    @Override
    public void cancel() {
        log.info("读取数据取消. task: {}. ", task.getTaskId());
        taskManager.cancel(task.getTaskId());
    }
}
