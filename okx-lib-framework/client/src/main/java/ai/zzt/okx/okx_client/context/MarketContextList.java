package ai.zzt.okx.okx_client.context;

import ai.zzt.okx.common.utils.InstUtils;
import ai.zzt.okx.v5.entity.ws.pub.FundingRate;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 主要是用来存储市场数据
 *
 * @author zhouzhitong
 * @since 2024/5/4
 **/
@Component
@Slf4j
@Deprecated
public class MarketContextList {

    @Deprecated
    private final Map<String, FundingRate> fundingRateMap = new ConcurrentHashMap<>();

    private final Map<String, MarkPrice> markPriceMap = new ConcurrentHashMap<>();

    public void addFundingRate(FundingRate fr) {
        fundingRateMap.put(fr.getInstId(), fr);
    }

    public void add(MarkPrice mp) {
        String instId = InstUtils.getBase(mp.getInstId());
        markPriceMap.put(instId, mp);
    }

    public MarkPrice getMarkPrice(String instId) {
        return markPriceMap.get(InstUtils.getBase(instId));
    }

    public FundingRate getFundingRate(String instId) {
        // TODO 是否应该判断下手续费率时间
        return fundingRateMap.get(instId);
    }

}
