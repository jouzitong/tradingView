package ai.zzt.okx.tradeOrder;

import ai.zzt.okx.dispatcher.engine.OrderEngine;
import ai.zzt.okx.dispatcher.manage.DefaultRequestManager;
import ai.zzt.okx.dispatcher.manage.RequestManager;
import ai.zzt.okx.platform.api.service.SettingsServiceWeb;
import ai.zzt.okx.settings.service.ISettingsService;
import ai.zzt.okx.tradeOrder.engine.TradeOrderEngine;
import ai.zzt.okx.tradeOrder.engine.order.OrderEngineV4;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 量化交易配置类
 *
 * @author zhouzhitong
 * @since 2024/5/28
 **/

@Slf4j
@Configuration
@AllArgsConstructor
public class TradeOrderConfig {

    @Bean
    public TradeOrderEngine tradeOrderEngine() {
        return new TradeOrderEngine();
    }

    @Bean
    public OrderEngine orderEngine() {
        return new OrderEngineV4();
    }

    @Bean
    public RequestManager requestManager() {
        return new DefaultRequestManager();
    }

//    @Bean
//    @Autowired
//    public IKLineEngine IKLineEngine(KStoreProperties kStoreProperties, KStoreService kStoreService) {
//        return new DefaultKLineEngine(kStoreProperties, kStoreService);
//    }

    @Bean
    public ISettingsService settingsService() {
        return new SettingsServiceWeb();
    }


}
