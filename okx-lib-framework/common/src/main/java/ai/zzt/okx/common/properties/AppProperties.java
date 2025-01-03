package ai.zzt.okx.common.properties;

import ai.zzt.okx.common.base.ConfigProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 系统的所有配置都应该放在这里.
 * <p>
 * FIXME 但是有些变量是临时写成的配置, 将来要改成数据的, 这类配置独立管理, 后期迁移到数据库中.
 *
 * @author zhouzhitong
 * @since 2024/7/28
 **/
@ConfigurationProperties(prefix = "app")
@Component
@Data
public class AppProperties implements ConfigProperties {

    /**
     * 是否进行初始化. 默认为true
     */
    private boolean init = true;

    /**
     * 系统版本. 主要是为了解决
     */
    private String version = "v1.0.0";

    /**
     * 系统名称
     */
    private String name = "okx-trade-default-name";

    /**
     * 是否模拟交易. 默认为true
     */
    private boolean simulated = true;

    /**
     * FIXME 临时密钥
     */
    @JsonIgnore
    private String key = "root@okx";

    /**
     * 邮箱配置
     */
    private MailProperties mail = new MailProperties();

}
