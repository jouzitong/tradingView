package ai.zzt.okx.dispatcher.manage;

import ai.zzt.okx.calculate.context.StatisticsContext;
import ai.zzt.okx.common.utils.InstUtils;
import ai.zzt.okx.okx_client.context.AccountContext;
import ai.zzt.okx.okx_client.context.InstrumentContext;
import ai.zzt.okx.okx_client.context.MarketContextList;
import ai.zzt.okx.okx_client.vo.InstFamily;
import ai.zzt.okx.settings.service.ISettingsService;
import ai.zzt.okx.dispatcher.request.TradeOrderRequest;
import ai.zzt.okx.v5.entity.ws.pub.Instruments;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 下单管理器
 * <pre>
 * 主要功能:
 * 1. 管理下单的配置
 * 2. 管理计算结果
 * </pre>
 *
 * @author zhouzhitong
 * @since 2024/6/1
 **/
@Getter
@Setter
public class DefaultRequestManager implements RequestManager {

    @Resource
    private ISettingsService settingsService;

    @Resource
    private AccountContext accountContext;

    @Resource
    private MarketContextList marketContextList;

    /**
     * 币的请求缓存
     */
    private final Map<String, TradeOrderRequest> requestMap = new ConcurrentHashMap<>();

    @Override
    public TradeOrderRequest buildRequest(MarkPrice mp) {
        String instId = mp.getInstId();
        String ccy = InstUtils.getBase(instId);
        TradeOrderRequest request = getRequest(mp);
        if (request != null) {
            return request;
        }
        synchronized (ccy.intern()) {
            request = getRequest(mp);
            if (request != null) {
                return request;
            }
            request = new TradeOrderRequest(mp);
            request.setSettingsContext(settingsService.getSettings(ccy));

            request.setStatisticsContext(new StatisticsContext());
            request.setAccountContext(accountContext);
            requestMap.put(ccy, request);
            return request;
        }
    }

    public TradeOrderRequest getRequest(MarkPrice mp) {
        String instId = mp.getInstId();
        String ccy = InstUtils.getBase(instId);
        TradeOrderRequest request = requestMap.get(ccy);
        if (request != null) {
            request.clear();
            request.getOrderRequest().clear();
            request.setMarkPrice(mp);
            return request;
        }
        return null;
    }

    @Override
    public boolean remove(String instId) {
        return requestMap.remove(instId) != null;
    }

    @Override
    public TradeOrderRequest get(String instId) {
        if (requestMap.containsKey(instId)) {
            return requestMap.get(instId);
        }
        String ccy = InstUtils.getBase(instId);
        InstFamily instFamily = InstrumentContext.getMap().get(ccy);
        for (Instruments instruments : instFamily.getSpotFamilyMap().values()) {
            if (requestMap.containsKey(instruments.getInstId())) {
                return requestMap.get(instruments.getInstId());
            }
        }
        return requestMap.get(instId);
    }

}
