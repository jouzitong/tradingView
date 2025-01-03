package ai.zzt.okx.data;

import ai.zzt.okx.common.Application;
import ai.zzt.okx.dispatcher.manage.DefaultRequestManager;
import ai.zzt.okx.dispatcher.manage.RequestManager;
import ai.zzt.okx.platform.api.service.SettingsServiceWeb;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author zhouzhitong
 * @since 2024/5/19
 **/
@SpringBootApplication(scanBasePackages = {"ai.zzt.okx"})
@Slf4j
@EnableScheduling
@EnableWebSocket
@Import(SettingsServiceWeb.class)
public class DataApplication extends Application {

    public static void main(String[] args) {
        SpringApplication.run(DataApplication.class);
    }

    /**
     * 必须要有这个bean才能生效使用webSocketServer
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }


    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public RequestManager requestManager() {
        return new DefaultRequestManager();
    }


}
