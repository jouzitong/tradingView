package ai.zzt.okx.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author zhouzhitong
 * @since 2024/12/30
 **/
@Component
@Data
@ConfigurationProperties("auth")
public class AuthProperties {

    private Set<String> ignoreUrls;

}
