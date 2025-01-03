package ai.zzt.okx.calculate.storage;

import ai.zzt.okx.calculate.storage.model.InstMarketData;
import ai.zzt.okx.okx_client.properties.OkxProperties;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.Bar;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhouzhitong
 * @since 2024/12/1
 **/
@Service
@Slf4j
public class StorageService {

    @Resource
    private OkxProperties okxProperties;

    private final Map<String, InstMarketData> marketDataMap = new ConcurrentHashMap<>();

    public void add(MarkPrice mp) {
        String instId = mp.getInstId();
        InstMarketData instMarketData = marketDataMap.get(instId);
        if (instMarketData == null) {
            instMarketData = new InstMarketData(instId, okxProperties.getPeriod());
            marketDataMap.put(instId, instMarketData);
        }
        for (Bar bar : okxProperties.getSupportBars()) {
            instMarketData.add(bar, mp.getMarkPx(), mp.getTs());
        }
    }

    public BigDecimal getLastMp(String instId){
        return marketDataMap.get(instId).getLastMp();
    }

    public long getLastTs(String instId){
        return marketDataMap.get(instId).getLastTs();
    }

    public InstMarketData get(String instId) {
        return marketDataMap.get(instId);
    }

}
