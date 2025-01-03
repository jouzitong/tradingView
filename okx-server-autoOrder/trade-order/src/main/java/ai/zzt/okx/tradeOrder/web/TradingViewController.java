package ai.zzt.okx.tradeOrder.web;

import ai.zzt.okx.calculate.context.AnalyzeContext;
import ai.zzt.okx.calculate.indicator.face.base.BaseIndicatorFace;
import ai.zzt.okx.common.enums.IndicatorType;
import ai.zzt.okx.common.enums.TrendType;
import ai.zzt.okx.common.utils.InstUtils;
import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.settings.context.SettingsContext;
import ai.zzt.okx.tradeOrder.serivce.TradingService;
import ai.zzt.okx.dispatcher.manage.DefaultRequestManager;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.trading.api.TradingRequestApi;
import ai.zzt.okx.trading.dto.InstDetailDTO;
import ai.zzt.okx.trading.dto.InstInfoDTO;
import ai.zzt.okx.v5.enumeration.Bar;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TradingView API
 *
 * @author zhouzhitong
 * @since 2024/10/8
 **/
@Slf4j
@RestController
@RequestMapping("/api/v1/trading")
public class TradingViewController implements TradingRequestApi {

    @Resource
    private DefaultRequestManager requestManager;

    @Resource
    private TradingService tradingService;

    @Value("${app.key:root@okx}")
    private String key;

    @GetMapping("/{instId}")
    public TradeOrderRequest get(@PathVariable("instId") String instId) {
        return requestManager.get(instId);
    }

    @GetMapping("/settings/{instId}")
    public R<SettingsContext> getSettings(@PathVariable("instId") String instId) {
        TradeOrderRequest tradeOrderRequest = requestManager.get(instId);
        if (tradeOrderRequest == null) {
            return R.ok(null);
        }
        return R.ok(tradeOrderRequest.getSettingsContext());
    }


    @PostMapping("/enablePlaceOrder/{instId}/{enable}")
    public R<Void> updatePlaceOrder(@PathVariable("instId") String instId,
                                    @RequestParam("key") String key,
                                    @PathVariable("enable") Boolean enablePlaceOrder) {
        log.info("{} 下单配置更新: {}", instId, enablePlaceOrder);
        if (!this.key.equals(key)) {
            log.warn("[updatePlaceOrder] key error: {}", key);
            return R.fail("认证不通过");
        }
        requestManager.get(instId).getSettingsContext()
                .getPlaceOrderSettings()
                .setEnablePlaceOrder(enablePlaceOrder);
        log.info("{} 下单配置更新成功", instId);
        return R.ok();
    }


    @PutMapping("/{instId}")
    public R<Void> update(@PathVariable("instId") String instId,
                          @RequestParam("key") String key) {
        log.info("更新产品配置: {}. [key: {}]", instId, key);
        if (!this.key.equals(key)) {
            log.warn("[update] key error: {}", key);
            return R.fail();
        }
        tradingService.update(instId);
        return R.ok();
    }

    @PostMapping("/{instId}")
    public R<Void> add(@PathVariable("instId") String instId,
                       @RequestParam("key") String key) {
        log.info("新增产品配置: {}. [key: {}]", instId, key);
        if (!this.key.equals(key)) {
            log.warn("[add] key error: {}", key);
            return R.fail();
        }
        tradingService.add(instId);
        return R.ok();
    }

    @DeleteMapping("/{instId}")
    public R<Void> delete(@PathVariable("instId") String instId,
                          @RequestParam("key") String key) {
        log.info("删除产品配置: {}. [key: {}]", instId, key);
        if (!this.key.equals(key)) {
            log.warn("[delete] key error: {}", key);
            return R.fail();
        }
        tradingService.remove(instId);
        return R.ok();
    }

    @Override
    @GetMapping("/list")
    public R<List<InstInfoDTO>> list() {
        List<InstInfoDTO> res = new ArrayList<>();
        Set<String> instIds = requestManager.getRequestMap().keySet();
        for (String instId : instIds) {
            InstInfoDTO dto = new InstInfoDTO();
            dto.setInstId(InstUtils.getBase(instId));
            dto.setEnablePlaceOrder(requestManager.get(instId).getSettingsContext().getPlaceOrderSettings().isEnablePlaceOrder());
            res.add(dto);
        }
        return R.ok(res);
    }

    @Override
    public R<InstDetailDTO> getTrendDetail(String instId) {
        TradeOrderRequest request = requestManager.get(instId);
        AnalyzeContext analyzeContext = request.getAnalyzeContext();
        InstDetailDTO res = new InstDetailDTO();
        res.setInstId(instId);

        TrendType analyzeTrend = analyzeContext.getAnalyzeTrend();
        res.setTrend(analyzeTrend);

        List<Bar> bars = request.getSettingsContext().getBars();

        List<BaseIndicatorFace> indicatorFaces = analyzeContext.getIndicatorFaces();

        Map<Bar, Map<IndicatorType, TrendType>> indicatorMap = new HashMap<>();
        for (Bar bar : bars) {
            Map<IndicatorType, TrendType> map = new HashMap<>();
            for (BaseIndicatorFace<?> indicatorFace : indicatorFaces) {
                TrendType trend = indicatorFace.getTrend(bar);
                map.put(indicatorFace.type(), trend);
            }
            indicatorMap.put(bar, map);
        }
        res.setIndicatorTrendMap(indicatorMap);
        return R.ok(res);
    }
}
