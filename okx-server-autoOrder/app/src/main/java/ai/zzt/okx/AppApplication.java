package ai.zzt.okx;

import ai.zzt.okx.common.Application;
import ai.zzt.okx.common.context.SystemContext;
import ai.zzt.okx.common.enums.SystemName;
import ai.zzt.okx.common.event.EventType;
import ai.zzt.okx.common.utils.EfficiencyMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@Slf4j
@SpringBootApplication(scanBasePackages = "ai.zzt.okx")
public class AppApplication extends Application {

    public static void main(String[] args) {
        EfficiencyMonitor.start(args, EventType.SYSTEM_START);
        SpringApplication.run(Application.class, args);
        SystemContext.setSystemName(SystemName.TRADE_VIEW);
        long end = EfficiencyMonitor.end();
        log.info("初始化完成, 系统已启动. 耗时 {} ms【{} 秒】", end, (end / 1000));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
