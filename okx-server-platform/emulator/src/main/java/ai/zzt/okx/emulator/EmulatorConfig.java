package ai.zzt.okx.emulator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author zhouzhitong
 * @since 2024/5/19
 **/
@Configuration
@EnableWebSocket
public class EmulatorConfig {

//    @Primary
//    @Bean(value = "emulateTradeOrderEngine")
//    public TradeOrderEngine tradeOrderEngine() {
//        return new EmulateTradeOrderEngine();
//    }

    /**
     * 必须要有这个bean才能生效使用webSocketServer
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
