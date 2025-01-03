package ai.zzt.okx.emulator.propertoes;

import ai.zzt.okx.common.base.ConfigProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhouzhitong
 * @since 2024/7/30
 **/
@Data
@Component
@ConfigurationProperties(prefix = "emulator")
public class EmulatorProperties implements ConfigProperties {

    /**
     * 任务相关配置
     */
    private EmulatorTaskProperties task = new EmulatorTaskProperties();

    /**
     * 是否定时跑任务组. 默认 false
     */
    private boolean runScheduledTaskGroup = false;

}
