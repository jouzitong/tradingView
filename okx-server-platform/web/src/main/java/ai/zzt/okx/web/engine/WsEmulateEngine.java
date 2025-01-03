package ai.zzt.okx.web.engine;

import ai.zzt.okx.calculate.context.AnalyzeContext;
import ai.zzt.okx.calculate.indicator.BOLL;
import ai.zzt.okx.calculate.indicator.Indicator;
import ai.zzt.okx.calculate.indicator.KLine;
import ai.zzt.okx.calculate.indicator.MACD;
import ai.zzt.okx.calculate.indicator.RSI;
import ai.zzt.okx.calculate.indicator.face.base.BaseIndicatorFace;
import ai.zzt.okx.common.constant.WsOpCode;
import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.common.utils.ApplicationContextUtils;
import ai.zzt.okx.common.utils.K;
import ai.zzt.okx.common.utils.ThreadUtils;
import ai.zzt.okx.common.utils.json.JackJsonUtils;
import ai.zzt.okx.common.vo.RW;
import ai.zzt.okx.emulator.callBack.CustomerTaskDTO;
import ai.zzt.okx.emulator.callBack.DefaultTaskCallBack;
import ai.zzt.okx.emulator.engine.BaseEmulateEngine;
import ai.zzt.okx.emulator.serivce.ITaskService;
import ai.zzt.okx.emulator.type.TaskStatus;
import ai.zzt.okx.okx_client.context.OrderContext;
import ai.zzt.okx.settings.calculate.BaseCalculateSettings;
import ai.zzt.okx.settings.calculate.KSettings;
import ai.zzt.okx.settings.calculate.MacdSettings;
import ai.zzt.okx.settings.calculate.RsiSettings;
import ai.zzt.okx.settings.calculate.face.BaseCalculateSettingsFace;
import ai.zzt.okx.settings.calculate.face.KSettingsFace;
import ai.zzt.okx.settings.calculate.face.MacdSettingsFace;
import ai.zzt.okx.settings.calculate.face.RsiSettingsFace;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.settings.service.ISettingsService;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.v5.entity.ws.pri.Order;
import ai.zzt.okx.v5.enumeration.Bar;
import ai.zzt.okx.web.req.KLineResp;
import ai.zzt.okx.web.req.KLineVO;
import ai.zzt.okx.web.req.WsEmulatorReq;
import jakarta.websocket.Session;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouzhitong
 * @since 2024/8/2
 **/
@Setter
@Slf4j
public class WsEmulateEngine extends BaseEmulateEngine {

    /**
     * webSocket会话, 用于发送消息.
     */
    private Session session;

    /**
     * 展示的时间粒度. 默认 分钟
     */
    private Bar bar;

    private int interval;

    private LinkedBlockingQueue<KLineVO> queue = new LinkedBlockingQueue<>(10000);

    private WsEmulatorReq req;

    private static ISettingsService settingsService;

    private static ITaskService taskService;

    private Thread taskThread;

    static {
        settingsService = ApplicationContextUtils.getBean(ISettingsService.class);
        taskService = ApplicationContextUtils.getBean(ITaskService.class);
    }

    public WsEmulateEngine(Session session, WsEmulatorReq emulatorReq, @NotNull CustomerTaskDTO customerTaskDTO) {
        super(new DefaultTaskCallBack(customerTaskDTO));
        String scStr = emulatorReq.getSettingsContext();
        SettingsContext settingsContext = JackJsonUtils.toBean(scStr, SettingsContext.class);
        if (settingsContext != null) {
            task.setSettingsContext(settingsContext);
        }
        this.session = session;
        this.bar = emulatorReq.getBar();
        this.interval = emulatorReq.getInterval();
        if (bar == null) {
            this.bar = Bar.H_1;
        }
        this.req = emulatorReq;
    }

    @Override
    public void init() {
        super.init();
        taskThread = Thread.currentThread();
        SettingsContext settingsContext = task.getSettingsContext();

        if (settingsContext == null) {
            // TODO 前端目前不传配置
            settingsContext = settingsService.getSettings(req.getInstId());
//            settingsContext = taskService.getSettingsByTaskId("BTC-USDT-SWAPUmgT9IhbUAWIgvEE");
            task.setSettingsContext(settingsContext);
        }

        Map<IndicatorType, BaseCalculateSettingsFace<?>> calculateSettingsFaceMap = settingsContext.getCalculateSettingsFaceMap();
        if (!calculateSettingsFaceMap.containsKey(IndicatorType.K_LINE)) {
            KSettingsFace kSettingsFace = new KSettingsFace(task.getInstId());
            kSettingsFace.setEnable(false);
            calculateSettingsFaceMap.put(IndicatorType.K_LINE, kSettingsFace);
        }

        for (BaseCalculateSettingsFace<? extends BaseCalculateSettings> settingsFace : calculateSettingsFaceMap.values()) {
            if (!settingsFace.getBars().contains(bar)) {
                if (settingsFace instanceof MacdSettingsFace macdSettingsFace) {
                    macdSettingsFace.addSettings(bar, new MacdSettings(req.getInstId(), null));
                } else if (settingsFace instanceof RsiSettingsFace rsiSettingsFace) {
                    rsiSettingsFace.addSettings(bar, new RsiSettings(req.getInstId(), null));
                }
            }
        }

        KSettingsFace kSettingsFace = (KSettingsFace) calculateSettingsFaceMap.get(IndicatorType.K_LINE);
        Collection<Bar> bars = kSettingsFace.getBars();
        if (!bars.contains(bar)) {
            KSettings kSettings = new KSettings(settingsContext.getInstId(), null, bar);
            kSettingsFace.addSettings(bar, kSettings);
        }
        boolean autoPush = req.isAutoPush();
        if (autoPush) {
            log.debug("开启自动推送");
            ThreadUtils.runIO(this::autoPush);
        }
    }

    private long lastStartTime = 0;

    private int orderCount = 0;

    private KLineVO tempKLine;

    @Override
    public void handle(TradeOrderRequest request) throws InterruptedException {
//        super.handle(request);
        AnalyzeContext analyzeContext = request.getAnalyzeContext();
        List<BaseIndicatorFace> indicatorFaces = analyzeContext.getIndicatorFaces();
        Object[] data = new Object[11];
        data[0] = 0L;

        long startTime;
        long tempTime = lastStartTime;

        OrderContext orderContext = request.getAccountContext().getOrderContext();
        List<Order> orders = orderContext.getOrders(request.getInstId());

        if (tempKLine == null) {
            tempKLine = new KLineVO();
        }
        if (CollectionUtils.isNotEmpty(orders)) {
            orderCount += orders.size();
            tempKLine.addOrders(orders);
        }

        for (BaseIndicatorFace indicatorFace : indicatorFaces) {
            Indicator indicator = indicatorFace.get(bar);
            if (indicator instanceof KLine kLine) {
                K last;
                if (kLine.getVals().size() > 1) {
                    last = kLine.getVals().getSecLast();
                } else {
                    return;
                }
                startTime = last.getTs();
                data[0] = startTime;
                if (startTime > lastStartTime) {
                    lastStartTime = startTime;
                } else {
                    return;
                }
                data[1] = last.getO();
                data[2] = last.getC();
                data[3] = last.getH();
                data[4] = last.getL();
                data[5] = null;
                data[6] = null;
            } else if (indicator instanceof MACD macd) {
                data[7] = macd.getStickLine().getLast();
                data[8] = macd.getDifLine().getLast();
                data[9] = macd.getDea().getLast();
            } else if (indicator instanceof RSI rsi) {
                data[10] = rsi.get();
            } else if (indicator instanceof BOLL boll) {
                data[10] = boll.get();
            }
        }
        tempKLine.setData(data);

        if (tempTime < lastStartTime) {
            queue.put(tempKLine);
            tempKLine = null;
        }
    }

    @Override
    public void finish() {
        try {
            queue.put(tempKLine);
            tempKLine = null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        super.finish();
    }

    public List<KLineVO> take() throws InterruptedException {
        long limit = req.getLimit();
        List<KLineVO> res = new ArrayList<>();
        KLineVO take;
        for (int i = 0; i < limit; i++) {
            if (queue.isEmpty()) {
                if (i == 0) {
                    take = queue.take();
                    res.add(take);
                } else {
                    break;
                }
            }
            KLineVO kLineVO = queue.poll();
            res.add(kLineVO);
        }
        return res;
    }

    int count = 0;

    private void autoPush() {
        while (true) {
            if (state != TaskStatus.RUNNING && queue.isEmpty()) {
                break;
            }
            if (!session.isOpen()) {
                log.warn("websocket session closed");
                queue.clear();
                taskThread.interrupt();
                break;
            }
            try {
                List<KLineVO> dataList = take();
                count += dataList.size();
                KLineResp resp = new KLineResp();
                resp.setBar(req.getBar());
                resp.setInstId(req.getInstId());
                resp.setDataList(dataList);
                log.trace("自动推送数据: {}", resp);
                RW<KLineResp> res = RW.ok(WsOpCode.K_LINE_RESPONSE_PUSH, resp);
                session.getAsyncRemote().sendText(JackJsonUtils.toStr(res));
                ThreadUtils.sleep(interval, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                log.warn("自动推送数据失败", e);
            }
        }
        log.info("auto push finish. total count: {}. orderCount: {}", count, orderCount);
    }

}
