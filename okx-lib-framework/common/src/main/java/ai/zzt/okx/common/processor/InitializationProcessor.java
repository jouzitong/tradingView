package ai.zzt.okx.common.processor;

import ai.zzt.okx.common.properties.AppProperties;
import ai.zzt.okx.common.base.system.Initialization;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * 初始化组件处理器
 *
 * @author zhouzhitong
 * @since 2024/6/20
 **/
@Slf4j
@Component
public class InitializationProcessor implements CommandLineRunner {

    @Resource
    private AppProperties appProperties;

    @Autowired(required = false)
    private List<Initialization> initializations;

    @Override
    public void run(String... args) {
        if (!appProperties.isInit()) {
            log.debug("系统初始化功能已关闭");
            return;
        }
        try {
            if (initializations == null) {
                log.info("没有需要初始化的组件");
                return;
            }
            log.info("ioc 启动完成, 开始初始化, 共{}个初始化器", initializations.size());
            initializations.sort(Comparator.comparingInt(Initialization::order));
            for (Initialization initialization : initializations) {
                initialization.initialize();
            }
        } catch (Exception e) {
            log.error("初始化失败", e);
            throw e;
        }

    }

}
