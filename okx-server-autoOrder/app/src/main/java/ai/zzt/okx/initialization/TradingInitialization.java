package ai.zzt.okx.initialization;

import ai.zzt.okx.common.base.system.Initialization;
import ai.zzt.okx.common.utils.ThreadUtils;
import ai.zzt.okx.okx_client.context.AccountContext;
import ai.zzt.okx.okx_client.context.OrderContext;
import ai.zzt.okx.okx_client.context.PositionContext;
import ai.zzt.okx.okx_client.properties.OkxProperties;
import ai.zzt.okx.okx_client.serivce.OkxWsApiServiceWrapper;
import ai.zzt.okx.okx_client.serivce.rest.AccountRestService;
import ai.zzt.okx.tradeOrder.serivce.TradingService;
import ai.zzt.okx.v5.api.factory.OkxRestApiFactory;
import ai.zzt.okx.v5.api.pri.AccountApi;
import ai.zzt.okx.v5.entity.rest.R;
import ai.zzt.okx.v5.entity.ws.biz.AlgoOrder;
import ai.zzt.okx.v5.entity.ws.pri.Positions;
import ai.zzt.okx.v5.enumeration.OrdType;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouzhitong
 * @since 2024/12/10
 **/
@Configuration
@Slf4j
public class TradingInitialization implements Initialization {

    @Resource
    private OkxProperties okxProperties;

    @Resource
    private AccountRestService accountRestService;

    @Resource
    private TradingService tradingService;

    @Resource
    private AccountApi accountApi;

    @Resource
    private AccountContext accountContext;

    @Resource
    private OkxWsApiServiceWrapper okxWsApiService;


    @Override
    public void initialize() {
//        initLever();
        syncPrivateTrade();
        initSubInstId();
    }

    /**
     * 同步 私有数据
     */
    private void syncPrivateTrade() {
        log.info("开始同步私有数据");

        // 获取持仓
        // 初始时间: 0s, 后续每3s更新一次
        int interval = 0;
        int delay = 3;
        TimeUnit unit = TimeUnit.SECONDS;
        // 获取账户仓位
        ThreadUtils.submitScheduled(this::syncPositions, interval, delay, unit);

        // 获取委托单
        ThreadUtils.submitScheduled(this::syncAlgoOrders, interval, delay, unit);
    }

    // TODO 同步杠杆, 先自己手动同步
    private void initLever() {
        Set<String> instIdList = okxProperties.getHotInstIdList();
        for (String instId : instIdList) {
            accountRestService.syncLever(instId, null);
        }
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
            // 【异步】同步其他平台数据
            ThreadUtils.runIO(() -> {
                try {
                    tradingService.add(instId);
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

    private void syncPositions() {
        log.debug("同步仓位信息");
        try {
            R<Positions> r = OkxRestApiFactory.get(accountApi.getPositions());
            if (r.isOk()) {
                PositionContext positionContext = accountContext.getPositionContext();
                positionContext.addAll(r.getData());
            }
        } catch (Exception e) {
            log.error("获取持仓失败.", e);
            ThreadUtils.sleep(1);
        }
    }

    /**
     * 全量同步 Algo Orders
     */
    private void syncAlgoOrders() {
        log.debug("同步 Algo Order 数据");
        for (OrdType value : OrdType.values()) {
            try {
                R<AlgoOrder> res = OkxRestApiFactory.get(
                        accountApi.getPendingAlgoOrders(value.value(), InstrumentType.SWAP.getValue()));
                if (res.isOk()) {
                    List<AlgoOrder> aos = res.getData();
                    OrderContext orderContext = accountContext.getOrderContext();
                    orderContext.addAlgoOrders(aos);
                }
            } catch (Exception e) {
                log.error("type: {} 获取委托单失败: {}", value, e.getMessage());
                ThreadUtils.sleep(1);
            }
        }
    }

}

