package ai.zzt.okx.auth;

import ai.zzt.okx.auth.filter.TokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhouzhitong
 * @since 2024/12/30
 **/
@Configuration
public class AuthConfig {



//    @Bean
    public FilterRegistrationBean<TokenFilter> loggingFilter() {
        FilterRegistrationBean<TokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TokenFilter());
        registrationBean.addUrlPatterns("/api/*");  // 只过滤 /api/* 路径的请求
        return registrationBean;
    }

}
