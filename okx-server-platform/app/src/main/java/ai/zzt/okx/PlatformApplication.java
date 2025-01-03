package ai.zzt.okx;

import ai.zzt.okx.common.Application;
import ai.zzt.okx.common.context.SystemContext;
import ai.zzt.okx.common.enums.SystemName;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/**
 * @author zhouzhitong
 * @since 2024/8/4
 **/
@EnableWebSocket
@SpringBootApplication(scanBasePackages = "ai.zzt.okx")
@EnableFeignClients(basePackages = {"ai.zzt.okx.trading.api", "ai.zzt.okx.common.web.api"})
public class PlatformApplication extends Application {

    public static void main(String[] args) {
        SpringApplication.run(PlatformApplication.class, args);
        SystemContext.setSystemName(SystemName.PLATFORM);
        SystemContext.open();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
