package ai.zzt.okx.settings;

import ai.zzt.okx.common.base.system.Initialization;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhouzhitong
 * @since 2024/6/1
 **/
@Configuration
public class SettingsConfig implements Initialization {

    @Override
    public void initialize() {

    }

    @Override
    public int order() {
        return Initialization.HIGH;
    }
}
