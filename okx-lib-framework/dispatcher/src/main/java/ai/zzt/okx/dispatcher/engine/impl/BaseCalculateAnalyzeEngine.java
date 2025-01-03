package ai.zzt.okx.dispatcher.engine.impl;

import ai.zzt.okx.calculate.context.AnalyzeContext;
import ai.zzt.okx.common.utils.K;
import ai.zzt.okx.okx_client.context.PositionContext;
import ai.zzt.okx.okx_client.serivce.OkxKLineApiService;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.dispatcher.engine.ICalculateAnalyzeEngine;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.Bar;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhouzhitong
 * @since 2024/10/25
 **/
@Slf4j
public abstract class BaseCalculateAnalyzeEngine implements ICalculateAnalyzeEngine {
    @Resource
    private OkxKLineApiService okxRestApiService;

    @Override
    public void init(TradeOrderRequest request) {
        String instId = request.getInstId();
        log.info("初始化计算分析引擎 instId: {}", instId);
        List<Bar> bars = request.getSettingsContext().getBars();
        Map<Bar, List<K>> kLinesMap = new HashMap<>();
        bars.sort((a, b) -> (int) (a.getScale() - b.getScale()));
        for (Bar bar : bars) {
            List<K> kLines = okxRestApiService.getKLines(instId, bar);
            kLines.sort(Comparator.comparingLong(K::getTs));
            kLinesMap.put(bar, kLines);
        }
        for (Bar bar : kLinesMap.keySet()) {
            List<K> ks = kLinesMap.get(bar);
            List<MarkPrice> mps = new ArrayList<>();
            for (K kLine : ks) {
                MarkPrice mp = new MarkPrice(null, instId, kLine.getC(), kLine.getTs());
                mps.add(mp);
            }
            request.getAnalyzeContext().init(bar, mps);
        }
        log.info("初始化计算分析引擎完成");
    }

    @Override
    public void analyze(TradeOrderRequest request) {
        MarkPrice mp = request.getMarkPrice();
        SettingsContext settingsContext = request.getSettingsContext();
        AnalyzeContext analyzeContext = request.getAnalyzeContext();
        PositionContext positionContext = request.getAccountContext().getPositionContext();
        analyzeContext.add(mp);
        // 分析整体趋势
        doAnalyze(analyzeContext, settingsContext);
        // 分析局部趋势（1m）, 并判断是否需要平仓
        doAnalyzeClose(analyzeContext, settingsContext, positionContext);
    }

    /**
     * 局部分析, 判断是否可以平仓
     *
     * @param analyzeContext  分析上下文
     * @param settingsContext 配置上下文
     * @param positionContext 仓位上下文
     */
    protected abstract void doAnalyzeClose(AnalyzeContext analyzeContext, SettingsContext settingsContext, PositionContext positionContext);

    /**
     * 分析趋势. 通过MACD和RSI分析结果, 判断当前趋势
     *
     * @param analyzeContext  分析上下文
     * @param settingsContext 配置上下文
     */
    protected abstract void doAnalyze(AnalyzeContext analyzeContext, SettingsContext settingsContext);

}
