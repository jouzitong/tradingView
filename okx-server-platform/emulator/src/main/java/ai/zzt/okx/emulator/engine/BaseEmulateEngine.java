package ai.zzt.okx.emulator.engine;

import ai.zzt.okx.common.utils.json.JackJsonUtils;
import ai.zzt.okx.storage.bean.Customer;
import ai.zzt.okx.common.utils.ApplicationContextUtils;
import ai.zzt.okx.common.utils.DateTimeUtils;
import ai.zzt.okx.emulator.callBack.CustomerTaskDTO;
import ai.zzt.okx.emulator.callBack.DefaultTaskCallBack;
import ai.zzt.okx.emulator.dto.TaskResult;
import ai.zzt.okx.emulator.entity.ProfitRecordEntity;
import ai.zzt.okx.emulator.repository.ProfitRecordRepository;
import ai.zzt.okx.emulator.type.TaskStatus;
import ai.zzt.okx.emulator.utils.creator.PositionsUtils;
import ai.zzt.okx.storage.engine.impl.DefaultKLineEngine;
import ai.zzt.okx.okx_client.context.AccountContext;
import ai.zzt.okx.okx_client.context.OrderContext;
import ai.zzt.okx.okx_client.context.PositionContext;
import ai.zzt.okx.okx_client.context.bean.PositionFace;
import ai.zzt.okx.okx_client.context.bean.PositionsWrapper;
import ai.zzt.okx.okx_client.context.bean.ProfitVO;
import ai.zzt.okx.okx_client.utils.OkxOrderUtils;
import ai.zzt.okx.okx_client.utils.ProfitUtils;
import ai.zzt.okx.storage.service.KStoreService;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.v5.entity.rest.account.Balance;
import ai.zzt.okx.v5.entity.ws.pri.Order;
import ai.zzt.okx.v5.entity.ws.pri.Positions;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.OrderState;
import ai.zzt.okx.v5.enumeration.OrderType;
import ai.zzt.okx.v5.enumeration.PositionsSide;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * TODO 后期稳定之后, 要把日志注释上, 千万别删除. 或者把日志级别调低
 *
 * @author zhouzhitong
 * @since 2024/8/2
 **/
@Slf4j
public class BaseEmulateEngine implements Callable<TaskResult>, Runnable {

    /**
     * 任务编号
     */
    protected final static ThreadLocal<TradeOrderRequest> requestThreadLocal = new ThreadLocal<>();

    protected static final KStoreService kStoreService;

    protected static final DefaultKLineEngine kLineEngine;

    protected static final ProfitRecordRepository profitRecordRepository;

    static {
        // 初始化基本服务
        kStoreService = ApplicationContextUtils.getBean(KStoreService.class);
        profitRecordRepository = ApplicationContextUtils.getBean(ProfitRecordRepository.class);
        kLineEngine = ApplicationContextUtils.getBean(DefaultKLineEngine.class);
    }

    @Getter
    protected final CustomerTaskDTO task;
    protected final DefaultTaskCallBack callBack;


    // 程序临时变量: 存一些程序允许时产生的新数据

    /**
     * 开仓总次数
     */
    private int openPositionsCount = 0;

    /**
     * 任务状态
     */
    protected TaskStatus state = TaskStatus.NEW;

    /**
     * 最终最大回撤金额
     */
    private BigDecimal lastMaxRetrace = BigDecimal.ZERO;

    /**
     * 当前交易的价格
     */
    MarkPrice markPrice = null;

    /**
     * 将交易记录缓存起来, 统一存储到数据库中
     */
    private final List<ProfitRecordEntity> recordEntities = new LinkedList<>();

    public BaseEmulateEngine(@NotNull DefaultTaskCallBack taskCallBack) {
        this.callBack = taskCallBack;
        this.task = taskCallBack.getTask();
    }

    @Override
    public void run() {
        try {
            call();
        } catch (Exception e) {
            log.error("任务异常. ", e);
        }
    }

    public void init() {
        // do nothing!!!
    }

    /**
     * FIXME 此处拉取数据需要优化数据复用问题
     *
     * @return
     * @throws Exception
     */
    @Override
    public TaskResult call() throws Exception {
        log.debug("EmulateTradeTask call <===========");
        log.debug("task: {} starting. settingContext: {}", getTask().getTaskId(), JackJsonUtils.toStr(getTask().getSettingsContext()));
        start();
        requestThreadLocal.set(task.buildRequest());
        try (Customer<MarkPrice> customer = kStoreService.createCustomer(task.getBar(), task.getInstId(), task.getStartTime())) {
            //线程是否被中断
            requestThreadLocal.set(task.buildRequest());
            while (true) {
                if (customer.isEnd()) {
                    log.debug("task:{} 没有数据.", task.getTaskId());
                    finish();
                    return result();
                }
                if (Thread.currentThread().isInterrupted()) {
                    log.warn("task:{} 被中断.", task.getTaskId());
                    cancel();
                    return result();
                }
                if (state == TaskStatus.CANCELLED) {
                    log.warn("task: {} 被取消.", task.getTaskId());
                    return result();
                }
                if (customer.isEmpty()) {
                    continue;
                }

                // 保证一定能取到数据
                markPrice = customer.take();
                // TODO 下面的业务逻辑, 不应该是由任务实现
                TradeOrderRequest request = getRequest();

                try {
                    Long ts = markPrice.getTs();
                    if (task.getStartTime() >= ts || task.getEndTime() <= ts) {
                        continue;
                    }

//                    // FIXME 临时添加, 需要删除的
//                    kLineEngine.addMp(markPrice);

                    // 执行调度任务
                    callBack.run(Collections.singletonList(markPrice));

                    // 让子类实现其他逻辑. 例如把计算的结果通过ws发送给前端
                    handle(request);

                    // 检查是否爆仓
                    if (bloomPosition(markPrice, request)) {
                        finish();
                        return result();
                    }

                    OrderContext orderContext = request.getAccountContext().getOrderContext();
                    // 检查是否有待处理订单
                    List<Order> orders = orderContext.getOrders(markPrice.getInstId());
                    if (CollectionUtils.isEmpty(orders)) {
                        continue;
                    }

                    for (Order order : orders) {
                        // 判断时间是否满足下单条件
                        boolean cPos = markPrice.getTs() >= order.getCTime() + task.getTaskSettings().getDelay();

                        if (cPos && checkMarkPrice(markPrice, order)) {
                            if (OkxOrderUtils.isOpenPositions(order.getPosSide(), order.getSide())) {
                                openPosition(markPrice, request, order);
                            } else if (checkClosePosition(request, order)) {
                                handleClosePosition(request, order);
                                closePosition(markPrice, request, order);

                            }
                        }
                    }
                    if (ts > task.getEndTime()) {
                        finish();
                        //返回只应该是表示运行状态的值，而不是简单的true or false
                        return result();
                    }
                } finally {
                    customer.commit(markPrice);
                }
            }
        } catch (Exception e) {
            log.error("未知异常. ", e);
            fail(e);
        } finally {
            requestThreadLocal.remove();
        }
        return result();
    }

    private boolean openPosition(MarkPrice markPrice, TradeOrderRequest request, Order order) {
        String instId = markPrice.getInstId();
        PositionContext positionContext = request.getAccountContext().getPositionContext();

        PositionFace positionFace = request.getAccountContext().getPositionContext().getFace(instId);
        Positions positions;
        PositionsSide posSide = order.getPosSide();
        if ((positions = positionFace.get(posSide)) == null) {
            positions = PositionsUtils.newPosition(markPrice);
            positions.setLever(order.getLever());
            positions.setMarkPx(markPrice.getMarkPx());
            positions.setPosSide(order.getPosSide());
            positions.setLast(markPrice.getMarkPx());
            positions.setTradeId(order.getOrdId());

            // 开仓数量
            positions.setPos(order.getSz());
            //开仓平均价格
            positions.setAvgPx(markPrice.getMarkPx());
        } else {
            // 开仓平均价格: (cash1 + cash2) / (size1+size2)
            BigDecimal totalCost = positions.getAvgPx().multiply(positions.getPos()).add(order.getSz().multiply(markPrice.getMarkPx()));
            positions.setAvgPx(totalCost.divide(positions.getPos().add(order.getSz()), 4, BigDecimal.ROUND_HALF_UP));
            // 开仓数量
            positions.setPos(positions.getPos().add(order.getSz()));
            positions.setUTime(markPrice.getTs());
        }

        BigDecimal availPos = positions.getAvailPos();
        positions.setAvailPos(availPos.add(order.getSz()));

        AccountContext accountContext = request.getAccountContext();
        Balance balance = accountContext.getBalance();
        // 开仓金额
        BigDecimal cash = markPrice.getMarkPx().multiply(order.getSz());

        balance.setUTime(markPrice.getTs());

        order.setState(OrderState.FILLED);
        positionContext.add(positions);

        OrderContext orderContext = accountContext.getOrderContext();
        // TODO 删除订单
        orderContext.remove(order);

        log.debug("开仓时间: {} 方向: {}. 当前价格: {} 【总共开仓次数: {}】", DateTimeUtils.ofTime(markPrice.getTs()),
                positions.getPosSide(), markPrice.getMarkPx(), (++openPositionsCount));
        return true;
    }

    private boolean closePosition(MarkPrice markPrice, TradeOrderRequest request, @Nullable Order order) {
        // 计算利润
        PositionContext positionContext = request.getAccountContext().getPositionContext();

        List<PositionsWrapper> positionsList = positionContext.getPositionWrapper(request.getInstId());
        for (PositionsWrapper wrapper : positionsList) {
            Positions positions = wrapper.positions;
            ProfitVO recentMaxProfit = wrapper.getFirstMaxProfit();

            BigDecimal maxProfit = wrapper.maxProfit.getProfit();
            long maxProfitTs = wrapper.maxProfit.getTs();
            // 回撤金额
            BigDecimal retrace = wrapper.retrace.getProfit();
            long retraceTs = wrapper.retrace.getTs();
            BigDecimal mpx = markPrice.getMarkPx();

            if (order != null) {
                PositionsSide posSide = order.getPosSide();
                if (positions.getPosSide() != posSide) {
                    continue;
                }
                if (CollectionUtils.isNotEmpty(order.getAttachAlgoOrds())) {
                    BigDecimal tpOrdPx = order.getAttachAlgoOrds().getFirst().getTpOrdPx();
                    if (tpOrdPx != null) {
                        mpx = tpOrdPx;
                    }
                }
            }
            positions.setLast(markPrice.getMarkPx());
            //手续费
            positions.setFee(BigDecimal.ZERO);

            // 计算利润
            BigDecimal profit = ProfitUtils.getProfit(positions, mpx, task.getTaskSettings().getFeeRate());

            AccountContext accountContext = request.getAccountContext();
            Balance balance = accountContext.getBalance();
            BigDecimal totalEq = balance.getTotalEq();
            totalEq = totalEq.add(profit);
            balance.setTotalEq(totalEq);

            balance.setUTime(markPrice.getTs());

            //TODO 删除订单
            OrderContext orderContext = accountContext.getOrderContext();
            List<Order> orders = orderContext.getOrders(markPrice.getInstId());
            if (!orders.isEmpty()) {
                // TODO 删除所有订单
                orderContext.clear();
            } else {
                log.warn("没有待平仓订单.");
            }

            LocalDateTime closeTime = DateTimeUtils.ofTime(markPrice.getTs());

            saveProfitRecord(positions, profit, maxProfit, retrace, markPrice, closeTime);

            log.debug("首次最大收益率: {}% [时间: {}]", recentMaxProfit.getProfit().multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP), DateTimeUtils.ofTime(recentMaxProfit.getTs()));

            Long ot = positions.getCTime();
            long ct = markPrice.getTs();
            long diffTime = ct - ot;
            if (diffTime > 1000L * 60 * 30 && retrace.compareTo(task.getTaskSettings().getRetrace().multiply(positions.getLever())) <= 0) {
                log.warn("【错误, 时间差: {}】平仓时间: {}. 平仓价格: {}. 利润: {}. 最高利润率: {}%[时间: {}], 回撤率: {}%[时间: {}]",
                        DateTimeUtils.takeToStr(diffTime), closeTime, mpx,
                        profit, wrapper.maxProfit.getProfitPercentage(), DateTimeUtils.ofTime(maxProfitTs),
                        wrapper.retrace.getProfitPercentage(), DateTimeUtils.ofTime(retraceTs));
            } else {
                log.debug("【耗时: {}】平仓时间: {}. 平仓价格: {}. 利润: {}. 最高利润率: {}%[时间: {}], 回撤率: {}%[时间: {}]",
                        DateTimeUtils.takeToStr(diffTime), closeTime, mpx,
                        profit, wrapper.maxProfit.getProfitPercentage(), DateTimeUtils.ofTime(maxProfitTs),
                        wrapper.retrace.getProfitPercentage(), DateTimeUtils.ofTime(retraceTs));
            }

            log.debug("当前资金: {}", balance.getTotalEq());
            log.debug("==========================================");
            request.setOverTime(false);
            positionContext.remove(request.getInstId(), positions.getPosSide());
        }
        return true;
    }

    private void saveProfitRecord(Positions positions, BigDecimal profit, BigDecimal maxProfit, BigDecimal retrace,
                                  MarkPrice markPrice, LocalDateTime closeTime) {
        if (!task.getTaskSettings().isStoreEnable()) {
            return;
        }
        ProfitRecordEntity recordEntity = new ProfitRecordEntity();
        StringBuilder buffer = new StringBuilder();
        buffer.append(markPrice.getInstId()).append("_").append(markPrice.getTs());

        recordEntity.setId(buffer.toString());
        recordEntity.setProfit(profit);
        recordEntity.setMaxProfit(maxProfit);
        recordEntity.setRetrace(retrace);
        recordEntity.setOpenTime(DateTimeUtils.ofTime(positions.getCTime()));
        recordEntity.setCloseTime(closeTime);
        recordEntity.setOpenMarketPrice(positions.getMarkPx());
        recordEntity.setCloseMarkPrice(markPrice.getMarkPx());
        recordEntity.setTaskId(task.getTaskId());
        recordEntity.setOrderId(positions.getTradeId());
        recordEntity.setOpenPositionsCount(openPositionsCount);
        recordEntities.add(recordEntity);
    }

    /**
     * 爆仓逻辑
     *
     * @param markPrice
     * @param request
     * @return
     */
    private boolean bloomPosition(MarkPrice markPrice, TradeOrderRequest request) {
        if (request == null) {
            return false;
        }
        PositionContext positionContext = request.getAccountContext().getPositionContext();

        List<PositionsWrapper> positionsList = request.getPositionsWrapper(request.getInstId());
        if (CollectionUtils.isEmpty(positionsList)) {
//            log.warn("没有仓位, 无法进入爆仓逻辑. 当前价格: {}. 时间 {}", markPrice.getMarkPx(), DateTimeUtils.of(markPrice.getTs()));
            return false;
        }

        for (PositionsWrapper wrapper : positionsList) {
            Positions positions = wrapper.positions;
            if (positions == null || positions.getAvailPos() == null) {
                continue;
            }

            BigDecimal maxProfit = wrapper.maxProfit.getProfit();
            long maxProfitTs = wrapper.maxProfit.getTs();
            BigDecimal retrace = wrapper.retrace.getProfit();


            positions.setLast(markPrice.getMarkPx());
            //手续费
            positions.setFee(BigDecimal.ZERO);

            BigDecimal profit = wrapper.profitRate;

            AccountContext accountContext = request.getAccountContext();
            Balance balance = accountContext.getBalance();

            // 最高利润和回撤率检查
            if (lastMaxRetrace.compareTo(retrace) >= 0) {
                lastMaxRetrace = retrace;
            }

            // 判断是否开启爆仓检查
            if (!task.getTaskSettings().isBloomEnable()) {
                return false;
            }

            if (profit.compareTo(BigDecimal.ZERO) > 0 || profit.add(balance.getTotalEq()).compareTo(BigDecimal.ZERO) >= 0) {
                return false;
            }


            balance.setTotalEq(BigDecimal.ZERO);
            balance.setUTime(markPrice.getTs());

            // 删除订单
            // TODO 爆仓，为啥需要删除订单
            OrderContext orderContext = accountContext.getOrderContext();
            orderContext.clear();
//            List<AlgoOrder> algoOrders = orderContext.getAlgoOrders(markPrice.getInstId());
//            if (!algoOrders.isEmpty()) {
//                algoOrders.removeFirst();
//            }
            // 删除仓位
            positionContext.remove(request.getInstId(), positions.getPosSide());

            log.error("亏损: {}. 最高盈利: {} - 时间: {}", profit, maxProfit, DateTimeUtils.ofTime(maxProfitTs));
            log.error("爆仓时间: {}. 爆仓价格: {}.", DateTimeUtils.ofTime(markPrice.getTs()), markPrice.getMarkPx());
            log.error("==========================================");
            return true;
        }
        return false;
    }

    /**
     * PositionSide相同的仓位可以平仓
     *
     * @param request
     * @param order
     * @return
     */
    private boolean checkClosePosition(TradeOrderRequest request, Order order) {
        Positions positions = request.getAccountContext().getPositionContext().getPosition(request.getInstId(), order.getPosSide());
        if (positions == null || BigDecimal.ZERO.compareTo(positions.getAvailPos()) == 0) {
            return false;
        }

        if (positions.getPosSide() != order.getPosSide()) {
            return false;
        }

        return true;
    }

    /**
     * 市价单无需考虑价格，即可生成仓位
     * 限价单判断markPrice是否小于等于限定价格，小于等于可以生成仓位
     *
     * @param markPrice
     * @param order
     * @return
     */
    private boolean checkMarkPrice(MarkPrice markPrice, Order order) {
        OrderType ordType = order.getOrdType();

        if (ordType == null || ordType == OrderType.MARKET) {
            return true;
        }
        if (ordType == OrderType.LIMIT) {
            // 处理止盈止损单
            PositionsSide posSide = order.getPosSide();
            if (CollectionUtils.isNotEmpty(order.getAttachAlgoOrds())) {
                // FIXME 当前取第一个订单, 如果有多个订单, 则需要处理
                Order.AttachAloOrder aao = order.getAttachAlgoOrds().getFirst();
                BigDecimal tpOrdPx = aao.getTpOrdPx();
                if (posSide == PositionsSide.LONG && markPrice.getMarkPx().compareTo(tpOrdPx) <= 0) {
                    return true;
                } else if (posSide == PositionsSide.SHORT && markPrice.getMarkPx().compareTo(tpOrdPx) >= 0) {
                    return true;
                }
            }
//            int com = markPrice.getMarkPx().compareTo(order.getTpOrdPx());
//            if (posSide == PositionsSide.LONG && com <= 0) {
//                return true;
//            } else if (posSide == PositionsSide.SHORT && com >= 0) {
//                return true;
//            }
//            return true;
        }
        return false;
    }

    public void handle(TradeOrderRequest request) throws InterruptedException {
//        request.getStatisticsContext()
//                .handle(request.getMarkPrice(), request.getAnalyzeContext().getAnalyzeTrend());
    }

    protected void handleClosePosition(TradeOrderRequest request, Order order) {

    }

    public static TradeOrderRequest getRequest() {
        return requestThreadLocal.get();
    }

    public void start() {
        state = TaskStatus.RUNNING;
        init();
        callBack.start();
    }

    public void fail(Throwable e) {
        callBack.fail(e);
        state = TaskStatus.FAILED;
    }

    public void finish() {
        closePosition(markPrice, getRequest(), null);

        if (!recordEntities.isEmpty()) {
            log.debug("保存利润记录. 数量: {}", recordEntities.size());
            profitRecordRepository.saveAll(recordEntities);
        }
        if (!recordEntities.isEmpty()) {
            log.debug("保存利润记录. 数量: {}", recordEntities.size());
            profitRecordRepository.saveAll(recordEntities);
        }

        Balance balance = getRequest().getAccountContext().getBalance();
        callBack.setLastProfit(balance.getTotalEq());
        callBack.finish();
        state = TaskStatus.SUCCESS;
    }

    public void cancel() {
        state = TaskStatus.CANCELLED;
        callBack.cancel();
    }

    public TaskResult result() {
        BigDecimal totalEq = getRequest().getAccountContext().getBalance().getTotalEq();
        return TaskResult.builder()
                .maxProfit(null)
                .lastProfit(totalEq)
                .maxRetrace(lastMaxRetrace)
                .build();
    }

    @Override
    public String toString() {
        return task.toString();
    }

}
