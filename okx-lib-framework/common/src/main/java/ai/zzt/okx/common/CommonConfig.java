package ai.zzt.okx.common;

import ai.zzt.okx.common.base.system.Initialization;
import ai.zzt.okx.common.base.system.Ordered;
import ai.zzt.okx.common.properties.AppProperties;
import ai.zzt.okx.common.utils.json.JackJsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author zhouzhitong
 * @since 2024/5/4
 **/
@Configuration
@Slf4j
@PropertySource("classpath:app.properties")
public class CommonConfig implements Initialization {

    @Resource
    private AppProperties appProperties;

    @Override
    public void initialize() {
        log.info("系统基础配置: {}", JackJsonUtils.toStr(appProperties));
    }

    @Override
    public int order() {
        return Ordered.HIGH;
    }


    //    /**
//     * 必须要有这个bean才能生效使用webSocketServer
//     */
//    @Bean
//    public ServerEndpointExporter serverEndpointExporter() {
//        return new ServerEndpointExporter();
//    }

}
