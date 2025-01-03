package ai.zzt.okx.okx_client.properties;

import ai.zzt.okx.common.utils.json.JackJsonUtils;
import ai.zzt.okx.v5.enumeration.Bar;
import ai.zzt.okx.v5.enumeration.ws.WsChannel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * okx 配置
 *
 * @since 2024/4/24
 */
@Data
@Component
@ConfigurationProperties(prefix = "okx")
public class OkxProperties {

    /**
     * 是否模拟账户, 默认为true
     */
    private Boolean simulated = true;

    /**
     * 是否开启 OKX 连接. 默认开启
     */
    @Deprecated
    private boolean enable = true;

    /**
     * 是否开启 clash 代理. 默认 false
     */
    private boolean proxy = false;

    /**
     * 是否开启定时任务: 收集历史数据. 默认 false
     */
    private boolean enableScheduledCollectHistoryData = false;

    /**
     * 真实账户
     */
    private Api api;

    /**
     * 模拟账户
     */
    private Api simulatedApi;

    /**
     * 支持的 ws 通道类型. 默认为 PUBLIC
     */
    private Set<WsChannel> supportWsChannelList = Set.of();

    /**
     * 支持计算的时间周期列表. 默认全部
     */
    private Set<Bar> supportBars = Set.of(Bar.M_1, Bar.M_15, Bar.M_30,
            Bar.H_1, Bar.H_2, Bar.H_4,
            Bar.D_1, Bar.W_1, Bar.M1);

    /**
     * k线计算周期
     */
    private int period = 10_0000;

    /**
     * 是否同步交易信息(订单, 仓位)
     */
    @Deprecated
    private boolean syncPrivateTrade = true;

    /**
     * 是否需要同步杠杆
     */
    private boolean syncLever = true;

    /**
     * 热门合约 json 文件
     */
    private Resource rateFile = new ClassPathResource("SWAP_HOT.json");

    /**
     * 每次交易金额比例(与最小交易金额比例)
     */
    private double tradeAmountRate = 1D;

    public Api getApi(boolean simulated) {
        return simulated ? simulatedApi : api;
    }

    public Api getApi() {
        return simulated ? simulatedApi : api;
    }

    @Data
    public static class Api {
        private String apiKey;
        private String secretKey;
        private String passPhrase;
    }

    @Data
    public static class CcyList {
        private Set<String> ccyList;
        private Set<String> instIdList;
    }

    /**
     * 获取热门合约
     *
     * @return 热门合约
     */
    public Set<String> getHotInstIdList() {
        return readResource(rateFile);
    }


    private Set<String> readResource(Resource resource) {
        if (!resource.exists()) {
            return Collections.emptySet();
        }
        try {
            return JackJsonUtils.readValueToList(resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
